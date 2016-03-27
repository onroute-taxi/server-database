package com.backseatmedia.database.engines.movies;

import com.backseatmedia.database.enums.neo4j.RelationshipTypes;
import com.graphaware.reco.generic.config.Config;
import com.graphaware.reco.generic.filter.BlacklistBuilder;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import java.util.HashSet;
import java.util.Set;

/**
 * This {@link BlacklistBuilder} creates a blacklist of all movies watched by the given user.
 */
public class WatchedMovieBlacklists implements BlacklistBuilder<Node, Node> {
    @Override
    public Set<Node> buildBlacklist(Node passengerNode, Config config) {
        Set<Node> watchedMoviesNodes = new HashSet<>();

        Direction direction = Direction.OUTGOING;
        RelationshipTypes relationshipType = RelationshipTypes.WATCHED_MOVIE;

        // Traverse and find all the watched movies
        for (Relationship r1 : passengerNode.getRelationships(direction, relationshipType)) {
            // Get the node and add it to our list
            Node watchedMovie = r1.getOtherNode(passengerNode);
            watchedMoviesNodes.add(watchedMovie);
        }

        return watchedMoviesNodes;
    }
}
