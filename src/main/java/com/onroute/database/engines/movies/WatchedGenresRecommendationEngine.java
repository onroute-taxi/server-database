package com.onroute.database.engines.movies;

import com.onroute.database.enums.neo4j.Labels;
import com.onroute.database.enums.neo4j.RelationshipTypes;
import com.onroute.database.enums.interests.MovieInterest;
import com.graphaware.reco.generic.context.Context;
import com.graphaware.reco.generic.engine.SingleScoreRecommendationEngine;
import com.graphaware.reco.generic.result.PartialScore;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import java.util.*;



public class WatchedGenresRecommendationEngine extends SingleScoreRecommendationEngine<Node, Node> {
    @Override
    protected Map<Node, PartialScore> doRecommendSingle(Node passengerNode, Context<Node, Node> context) {
        Map<Node, PartialScore> result = new HashMap<>();
        List<MovieInterest> watchedGenres = new ArrayList();
        List<Node> watchedMovies = new ArrayList<>();
        GraphDatabaseService graphDb = passengerNode.getGraphDatabase();

        // First get a list of all the watched genres (Ideally it'll be preferable if this was kept
        // as a JSON within the node itself so that we can avoid this traversal.
        for (Relationship r1 : passengerNode.getRelationships(Direction.OUTGOING,
                RelationshipTypes.WATCHED_MOVIE)) {
            // Get the node
            Node watchedMovie = r1.getOtherNode(passengerNode);

            // Save the genre for later use.
            watchedGenres.add(MovieInterest.valueOf((String) watchedMovie.getProperty("genre")));
            watchedMovies.add(watchedMovie);
        }

        // Now that we have caculated the genres, we give each movie a score based on how often
        // particular genre has been watched.
        Iterator<Node> movieNodes = graphDb.findNodes(Labels.Movie);
        int count = context.config().limit();
        while (movieNodes.hasNext() && count-- > 0) {
            // Get the node
            Node movieNode = movieNodes.next();

            // Check if the node is allowed by our context
            if (!context.allow(movieNode, name())) {
                // If not, then rewind our counter and skip this node..
                count++;
                continue;
            }

            // Get the genre of the movie
            MovieInterest genre = MovieInterest.valueOf((String) movieNode.getProperty("genre"));

            // Calculate a partial score for it and add it to the results. We give the partial score
            // on the basis of how many times the particular genre has been watched.
            int genreWatchCount = Collections.frequency(watchedGenres, genre);
            addToResult(result, movieNode, score(genreWatchCount));
        }

        return result;
    }


    /**
     * Helper function to calculate the score of the movie given the genre watched.
     *
     * @param genreWatchCount
     * @return
     */
    PartialScore score(int genreWatchCount) {
        return new PartialScore(genreWatchCount * 10);
    }


    @Override
    public String name() {
        return WatchedGenresRecommendationEngine.class.getSimpleName();
    }
}