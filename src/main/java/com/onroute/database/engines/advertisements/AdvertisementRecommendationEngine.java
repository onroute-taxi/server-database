package com.onroute.database.engines.advertisements;

import com.onroute.database.models.SessionModel;
import com.graphaware.reco.generic.engine.TopLevelDelegatingRecommendationEngine;
import com.graphaware.reco.generic.post.PostProcessor;
import org.neo4j.graphdb.Node;

import java.util.Collections;
import java.util.List;

/**
 * The advertising recommendation engine is the main component which calculates which advertisement
 * is right for the current session.
 */
public class AdvertisementRecommendationEngine extends TopLevelDelegatingRecommendationEngine<Node, SessionModel> {
    @Override
    protected List<PostProcessor<Node, SessionModel>> postProcessors() {
        return Collections.<PostProcessor<Node, SessionModel>>singletonList(
                new RewardForLocation()
        );
    }
}