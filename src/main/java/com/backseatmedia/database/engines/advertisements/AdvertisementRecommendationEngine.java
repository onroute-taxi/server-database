package com.backseatmedia.database.engines.advertisements;

import com.backseatmedia.database.models.SessionModel;
import com.graphaware.reco.generic.engine.TopLevelDelegatingRecommendationEngine;
import com.graphaware.reco.generic.post.PostProcessor;
import org.neo4j.graphdb.Node;

import java.util.Arrays;
import java.util.List;

/**
 * The advertising recommendation engine is the main component which calculates which advertisement
 * is right for the current session.
 */
public class AdvertisementRecommendationEngine extends TopLevelDelegatingRecommendationEngine<Node, SessionModel> {
    @Override
    protected List<PostProcessor<Node, SessionModel>> postProcessors() {
        return Arrays.<PostProcessor<Node, SessionModel>>asList(
                new RewardForLocation()
        );
    }
}