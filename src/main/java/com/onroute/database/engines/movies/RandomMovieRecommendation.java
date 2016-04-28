package com.onroute.database.engines.movies;

import com.onroute.database.enums.neo4j.Labels;
import com.graphaware.common.policy.BaseNodeInclusionPolicy;
import com.graphaware.common.policy.NodeInclusionPolicy;
import com.graphaware.reco.generic.context.Context;
import com.graphaware.reco.generic.policy.ParticipationPolicy;
import com.graphaware.reco.neo4j.engine.RandomRecommendations;
import org.neo4j.graphdb.Node;


public class RandomMovieRecommendation extends RandomRecommendations {
    @Override
    public String name() {
        return RandomMovieRecommendation.class.getSimpleName();
    }


    @Override
    protected NodeInclusionPolicy getPolicy() {
        return new BaseNodeInclusionPolicy() {
            @Override
            public boolean include(Node node) {
                return node.hasLabel(Labels.Movie);
            }
        };
    }


    @Override
    public ParticipationPolicy<Node, Node> participationPolicy(Context context) {
        return ParticipationPolicy.IF_MORE_RESULTS_NEEDED_AND_ENOUGH_TIME;
    }
}