package com.onroute.database.resource;

import com.onroute.database.enums.TabletActions;
import com.onroute.database.enums.neo4j.Labels;
import com.onroute.database.enums.neo4j.RelationshipTypes;
import com.onroute.database.models.AdvertisementModel;
import com.onroute.database.models.SessionModel;
import com.onroute.database.resource.base.BaseResource;
import com.onroute.database.websocket.Response;
import org.neo4j.graphdb.*;

import java.util.Date;


public class AdvertisementResource extends BaseResource {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "advertisement";
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Response handleRequest(String function, SessionModel session, String data) throws Exception {
        switch (function) {
            case "get":
                AdvertisementModel ad = getInteractiveAd(session);
                session.addCommand(TabletActions.SET_ADVERTISEMENT, ad.toJSON(gson));
                break;
            case "watched":
                watchedAnAdertisement(session, gson.fromJson(data, AdvertisementModel.class));
                break;
            case "interacted":
                interactedWithAdvertisement(session, gson.fromJson(data, AdvertisementModel.class));
                break;
            default:
                throw new NotFoundException("no function found");
        }

        return Response.returnSuccess(session);
    }


    /**
     * Given a particular session, this function should return the most relevant ad to the user.
     * This AD must be an interactive one.
     * TODO: Call advertisement recommendation engine here..
     *
     * @param session The current session of the tablet. Should contain information about the
     *                passenger as well.
     * @return The best suited ad for the user.
     */
    AdvertisementModel getInteractiveAd(SessionModel session) {
        AdvertisementModel bestAdvertisement = null;

        try (Transaction tx = graphDb.beginTx()) {
            ResourceIterator<Node> iterator = graphDb.findNodes(Labels.Advertisement);
            Node passengerNode = session.getPassenger().getNode(graphDb);

            while (iterator.hasNext()) {
                Node advertisementNode = iterator.next();

                AdvertisementModel model = new AdvertisementModel();
                model.clonePropertiesFromNode(advertisementNode);

                // Don't consider ads with no funds.
                if (model.getFunds() <= 0) continue;

                // Don't consider ads that have already been seen by the passenger.
                if (passengerNode != null &&
                        passengerNode.hasRelationship(Direction.BOTH,
                                RelationshipTypes.SEEN_ADVERTISEMENT))
                    continue;

                if (bestAdvertisement == null || (bestAdvertisement.getCostPerImpression() <
                        model.getCostPerImpression()))
                    bestAdvertisement = model;
            }

            tx.close();
        }

        return bestAdvertisement;
    }


    /**
     * Set the given advertisement as 'watched' by the passenger.
     * TODO: Make sure that the user data is the same as the server data..
     *
     * @param session The current session of the tablet. Should contain information about the
     *                passenger as well.
     * @param ad      The advertisement that was 'watched'
     */
    void watchedAnAdertisement(SessionModel session, AdvertisementModel ad) {
        try (Transaction tx = graphDb.beginTx()) {
            Node advertisementNode = ad.getNode(graphDb);
            Node passengerNode = session.getPassenger().getNode(graphDb);

            // Create the "seen ad" relationship first
            Relationship relationship = passengerNode.createRelationshipTo(advertisementNode,
                    RelationshipTypes.SEEN_ADVERTISEMENT);
            relationship.setProperty("createdAt", (new Date()).getTime());

            // Subtract the CPI from the ads.
            // TODO: User can manipulate the fields to update the final balance. Ensure this is done only in the backend.
            advertisementNode.setProperty("funds", ad.getFunds() - ad.getCostPerImpression());

            // Increase the impressions counter.
            ad.setImpressions(ad.getImpressions() + 1);

            // Save everything into the DB.
            tx.success();
        }
    }


    /**
     * Set the given advertisement as 'interacted' by the passenger.
     * TODO: understand the user's interests from this ad.
     *
     * @param session The current session of the tablet. Should contain information about the
     *                passenger as well.
     * @param ad      The advertisement that was 'interacted'
     */
    void interactedWithAdvertisement(SessionModel session, AdvertisementModel ad) {
        try (Transaction tx = graphDb.beginTx()) {
            Node advertisementNode = ad.getNode(graphDb);
            Node passengerNode = session.getPassenger().getNode(graphDb);

            // Create the "interacted with ad" relationship first
            Relationship relationship = passengerNode.createRelationshipTo(advertisementNode,
                    RelationshipTypes.INTERACTED_ADVERTISEMENT);
            relationship.setProperty("createdAt", (new Date()).getTime());

            // Subtract the CPC from the ads.
            // TODO: User can manipulate the fields to update the final balance. Ensure this is done only in the backend.
            advertisementNode.setProperty("funds", ad.getFunds() - ad.getCostPerClick());

            // Increase the clicks counter.
            ad.setClicks(ad.getClicks() + 1);

            // Save everything into the DB.
            tx.success();
        }
    }
}