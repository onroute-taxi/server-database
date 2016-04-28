package com.onroute.database.models;

import com.onroute.database.enums.RideType;
import com.onroute.database.enums.neo4j.Labels;
import com.onroute.database.enums.neo4j.RelationshipTypes;
import com.onroute.database.models.base.Neo4jModel;
import com.google.gson.annotations.Expose;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.graphdb.*;


/**
 * This class represents a tablet model.
 * <p/>
 * Created by Steven Enamakel on 12/2/15.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TabletModel extends Neo4jModel {
    @Expose String device;
    @Expose String mac;
    @Expose String model;
    @Expose String product;
    @Expose int sdkVersion;
    @Expose public TabletStatus status = new TabletStatus();
    @Expose RideType rideType;

    @Data
    public class TabletStatus {
        @Expose double latitude;
        @Expose double longitude;
        @Expose double batteryPercent;
        @Expose boolean charging;
    }


    public LocationModel getLocation() {
        return new LocationModel(status.latitude, status.longitude);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void clonePropertiesFromNode(Node node) throws NotInTransactionException {
        this.node = node;
        mac = (String) node.getProperty("mac");
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Node getNodeWeak(GraphDatabaseService db) throws NotInTransactionException {
        node = db.findNode(Labels.Tablet, "mac", this.mac);
        return node;
    }


    /**
     * @see Neo4jModel
     */
    @Override
    public void clonePropertiesToNode(Node node) throws NotInTransactionException {
        setNodeProperty(node, "device", device);
        setNodeProperty(node, "mac", mac);
        setNodeProperty(node, "model", model);
        setNodeProperty(node, "product", product);
        setNodeProperty(node, "sdkVersion", sdkVersion);
    }


    public Node getLocationChangesetNode(GraphDatabaseService db) {
        Node tabletNode = this.getNode(db);

        if (tabletNode == null) return null;

        // Find the appropriate changeset node!
        for (Relationship relationship : tabletNode.getRelationships(Direction.OUTGOING,
                RelationshipTypes.HAS_CHANGESET)) {
            Node changesetNode = relationship.getOtherNode(tabletNode);
            if (changesetNode.getProperty("type").equals("location")) return changesetNode;
        }


        // If the node was not found then we create it!
        Node changesetNode = db.createNode(Labels.Changeset);
        changesetNode.setProperty("type", "location");
        tabletNode.createRelationshipTo(changesetNode, RelationshipTypes.HAS_CHANGESET);
        return changesetNode;
    }
}