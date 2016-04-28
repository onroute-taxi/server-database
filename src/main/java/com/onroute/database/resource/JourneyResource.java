package com.onroute.database.resource;

import com.onroute.database.enums.neo4j.Labels;
import com.onroute.database.enums.neo4j.RelationshipTypes;
import com.onroute.database.models.JourneyModel;
import com.onroute.database.models.SessionModel;
import com.onroute.database.resource.base.BaseResource;
import com.onroute.database.websocket.Response;
import org.neo4j.graphdb.*;

import java.util.Date;

/**
 * This resource interacts with a Journey.
 */
public class JourneyResource extends BaseResource {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "journey";
    }


    public JourneyModel beginJourney(SessionModel session) {
        Relationship relationship1, relationship2;

        // Prepare the journey model
        JourneyModel journey = new JourneyModel();
        journey.setCreatedAt(new Date().getTime());
        journey.setSource(session.getTablet().getLocation());

        try (Transaction tx = graphDb.beginTx()) {
            Node tabletNode = session.getTablet().getNode(graphDb);
            Node passengerNode = session.getPassenger().getNode(graphDb);

            // First create the journey node
            Node journeyNode = graphDb.createNode(Labels.Journey);
            journey.clonePropertiesToNode(journeyNode);

            // Then attach the passenger node to this journey node
            relationship1 = journeyNode.createRelationshipTo(passengerNode, RelationshipTypes.HAS_PASSENGER);
            relationship1.setProperty("createdAt", new Date().getTime());

            // Then attach the journey node to the tablet node
            relationship2 = journeyNode.createRelationshipTo(tabletNode, RelationshipTypes.USING_TABLET);
            relationship2.setProperty("createdAt", new Date().getTime());

            // Make sure to save the id from the journey node!
            journey.setId(journeyNode.getId());

            tx.success();
        }

        return journey;
    }


    public JourneyModel endJourney(SessionModel session) throws Exception {
        // Prepare the journey model
        JourneyModel journey = session.getJourney();
        journey.setFinishedAt(new Date().getTime());
        journey.setDestination(session.getTablet().getLocation());

        try (Transaction tx = graphDb.beginTx()) {
            Node journeyNode = journey.getNode(graphDb);

            if (journeyNode == null) throw new Exception("no journey node found!");

            journey.clonePropertiesToNode(journeyNode);
            tx.success();
        }

        return journey;
    }


    /**
     * @see BaseResource
     */
    @Override
    public Response handleRequest(String function, SessionModel session, String data) throws Exception {
        switch (function) {
            case "begin":
                session.setJourney(beginJourney(session));
                break;

            case "update":
                break;

            case "end":
                session.setJourney(endJourney(session));
                break;

            default:
                throw new NotFoundException("no function found");
        }

        return Response.returnSuccess(session);
    }
}
