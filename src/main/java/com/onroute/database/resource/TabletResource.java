package com.onroute.database.resource;

import com.onroute.database.enums.neo4j.Labels;
import com.onroute.database.enums.neo4j.RelationshipTypes;
import com.onroute.database.models.SessionModel;
import com.onroute.database.models.TabletModel;
import com.onroute.database.resource.base.BaseResource;
import com.onroute.database.websocket.Response;
import org.neo4j.gis.spatial.Layer;
import org.neo4j.graphdb.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;


/**
 * This service endpoint is used to interact with data on the Tablet.
 */
public class TabletResource extends BaseResource {
    private static final Logger logger = LoggerFactory.getLogger(TabletResource.class);
    Layer locationLayer;


    private Layer getLocationLayer() {
        if (locationLayer == null) {
            logger.debug("created 'tablet_location' spatial index");
            locationLayer = spatialDb.createSimplePointLayer("tablet_location");
        }

        return locationLayer;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "tablet";
    }


    /**
     * Find a tablet given it's MAC address.
     *
     * @param mac MAC address of the tablet.
     * @return TabletModel instance of the tablet.
     * @throws NotFoundException if the tablet could not be found.
     */
    private TabletModel getByMAC(String mac) throws NotFoundException {
        Node node;
        TabletModel model = new TabletModel();

        try (Transaction tx = graphDb.beginTx()) {
            node = graphDb.findNode(Labels.Tablet, "mac", mac);

            // if node was not found return NotFound
            if (node == null) throw new NotFoundException("Tablet not found");

            // if node was found, then fetch a TabletModel instance out of it.
            model.clonePropertiesFromNode(node);
            tx.success();
        }

        return model;
    }


    /**
     * This function checks-in a tablet. If a table does not exist, it creates one.
     * <p/>
     * Once a tablet is found, it is registered into the system and the checkin activity is logged.
     *
     * @return TabletModel instance of the tablet.
     */
    private TabletModel checkin(TabletModel model) {
        try (Transaction tx = graphDb.beginTx()) {
            Node node = graphDb.findNode(Labels.Tablet, "mac", model.getMac());

            if (node == null) {
                // Create the node here
                node = graphDb.createNode(Labels.Tablet);
                model.clonePropertiesToNode(node);
                tx.success();
            }
        }

        return model;
    }


    /**
     * The tablet at every moment updates it's status with the server. This is called a heartbeat. This method is
     * called with the tablet's current status.
     * <p/>
     * For now the location of the tablet just gets recorded.
     *
     * @param model The tablet that is sending the heartbeat
     * @return TabletModel instance of the tablet.
     */
    private TabletModel heartbeat(TabletModel model) {
        try (Transaction tx = graphDb.beginTx()) {
            Node locationNode = graphDb.createNode(Labels.Location);
            locationNode.setProperty("latitude", model.getStatus().getLatitude());
            locationNode.setProperty("longitude", model.getStatus().getLongitude());
            locationNode.setProperty("createdAt", new Date().getTime());

            // Get the location changeset node, and add the location node to it.
            Node locationChangesetNode = model.getLocationChangesetNode(graphDb);
            Relationship relationship = locationNode.createRelationshipTo(locationChangesetNode,
                    RelationshipTypes.CHANGESET);
            relationship.setProperty("createdAt", new Date().getTime());

            // Finally, add the node to the spatial index
            getLocationLayer().add(locationNode);
            tx.success();
        }
        return model;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Response handleRequest(String function, SessionModel session, String data) throws Exception {
        TabletModel tablet;
        switch (function) {
            case "getByMAC":
                tablet = getByMAC(session.getTablet().getMac());
                break;
            case "checkin":
                tablet = checkin(session.getTablet());
                break;
            case "heartbeat":
                tablet = heartbeat(session.getTablet());
                break;
            default:
                throw new NotFoundException("no function found");
        }

        session.setTablet(tablet);
        return Response.returnSuccess(session);
    }
}