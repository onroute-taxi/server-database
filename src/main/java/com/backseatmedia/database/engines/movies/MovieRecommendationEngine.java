package com.backseatmedia.database.engines.movies;

import com.graphaware.reco.generic.engine.RecommendationEngine;
import com.graphaware.reco.generic.engine.TopLevelDelegatingRecommendationEngine;
import com.graphaware.reco.generic.filter.BlacklistBuilder;
import org.neo4j.graphdb.Node;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * This recommendation engines is used to recommend movies based on the ones that the user has
 * watched.
 */
public class MovieRecommendationEngine extends TopLevelDelegatingRecommendationEngine<Node, Node> {
    @Override
    protected List<RecommendationEngine<Node, Node>> engines() {
        return Arrays.<RecommendationEngine<Node, Node>>asList(
                new WatchedGenresRecommendationEngine(),
                new RandomMovieRecommendation()
        );
    }


    @Override
    protected List<BlacklistBuilder<Node, Node>> blacklistBuilders() {
        return Collections.<BlacklistBuilder<Node, Node>>singletonList(
                new WatchedMovieBlacklists()
        );
    }
}