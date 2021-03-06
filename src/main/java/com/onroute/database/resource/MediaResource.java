package com.onroute.database.resource;


import com.onroute.database.engines.movies.MovieRecommendationEngine;
import com.onroute.database.enums.neo4j.RelationshipTypes;
import com.onroute.database.enums.TabletActions;
import com.onroute.database.models.PassengerModel;
import com.onroute.database.models.SessionModel;
import com.onroute.database.models.media.MovieModel;
import com.onroute.database.resource.base.BaseResource;
import com.onroute.database.websocket.Response;
import com.graphaware.reco.generic.config.SimpleConfig;
import com.graphaware.reco.generic.result.Recommendation;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.NotFoundException;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The media resource is responsible for handling all interactions with the media in the tablet. All movies, music,
 * videos get managed here.
 * <p/>
 * This resource also uses a recommendation engine of it's own to recommend content.
 */
public class MediaResource extends BaseResource {
    private static final Logger logger = LoggerFactory.getLogger(MediaResource.class);

    MovieRecommendationEngine movieRecommendationEngine = new MovieRecommendationEngine();


    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "media";
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Response handleRequest(String function, SessionModel session,
                                  String data) throws Exception {
        switch (function) {
            case "get_recommended_movies":
                List<MovieModel> movies = getRecommendedMovies(session);
                session.addCommand(TabletActions.UPDATE_MOVIE_LIST, gson.toJson(movies));
                break;

            case "watch_movie":
                watch_movie(session, data);
                break;

            default:
                throw new NotFoundException("no function found");
        }
        return Response.returnSuccess(session);
    }


    void watch_movie(SessionModel session, String movieJSON) {
        MovieModel movie = gson.fromJson(movieJSON, MovieModel.class);
        try (Transaction tx = graphDb.beginTx()) {
            Node movieNode = movie.getNode(graphDb);
            Node passengerNode = session.getPassenger().getNode(graphDb);

            Relationship relationship = passengerNode.createRelationshipTo(movieNode,
                    RelationshipTypes.WATCHED_MOVIE);
            relationship.setProperty("createdAt", new Date().getTime());

            tx.success();
        }
    }


    /**
     * TODO: make all function static and db variable globally static as well
     */
    public List<MovieModel> getRecommendedMovies(SessionModel session) {
        List<MovieModel> results = new ArrayList();
        int MAX_MOVIES = 10;

        try (Transaction tx = graphDb.beginTx()) {
            PassengerModel passenger = session.getPassenger();
            Node passengerNode = passenger.getNode(graphDb);

            logger.debug("getting recommendations " + passengerNode.toString());

            // Call the Movies recommendation engines!
            List<Recommendation<Node>> movieRecommendations;
            movieRecommendations = movieRecommendationEngine.recommend(passengerNode,
                    new SimpleConfig(MAX_MOVIES));

            logger.debug("got recommendations" + movieRecommendations.size());

            // Take every recommendation and put it into a model so that it can be converted into
            // JSON.
            for (Recommendation<Node> recommendation : movieRecommendations) {
                results.add(MovieModel.fromNode(recommendation.getItem()));
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
//            throw e;
        }

        return results;
    }
}