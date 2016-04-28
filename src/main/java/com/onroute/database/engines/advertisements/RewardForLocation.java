package com.onroute.database.engines.advertisements;

import com.onroute.database.models.SessionModel;
import com.graphaware.reco.generic.context.Context;
import com.graphaware.reco.generic.post.BasePostProcessor;
import com.graphaware.reco.generic.result.Recommendations;
import org.neo4j.graphdb.Node;

public class RewardForLocation extends BasePostProcessor<Node, SessionModel> {

    @Override
    protected String name() {
        return null;
    }


    @Override
    protected void doPostProcess(Recommendations<Node> recommendations, SessionModel input, Context<Node, SessionModel> context) {

    }
}
