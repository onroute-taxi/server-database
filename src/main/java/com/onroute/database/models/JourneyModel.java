package com.onroute.database.models;

import com.onroute.database.enums.DestinationType;
import com.onroute.database.models.base.Neo4jModel;
import com.google.gson.annotations.Expose;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.NotInTransactionException;


/**
 * This class represents a Journey.
 * <p/>
 * A journey is used to represent a journey a passenger takes. It takes care of tracking the user's location and details
 * about the trip.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class JourneyModel extends Neo4jModel {
    @Expose long createdAt;
    @Expose long finishedAt;
    @Expose LocationModel source;
    @Expose LocationModel destination;
    @Expose int passengers;

    @Expose DestinationType destinationType;
    @Expose long estimatedRideLength;
    @Expose long actualRideLength;

    @Expose float destinationTypeConfidence;
    @Expose float rideLengthConfidence;


    /**
     * {@inheritDoc}
     */
    @Override
    public void clonePropertiesFromNode(Node node) throws NotInTransactionException {
        id = node.getId();
        source.setLatitude((Double) node.getProperty("sourceLatitude"));
        source.setLongitude((Double) node.getProperty("sourceLongitude"));
        destination.setLatitude((Double) node.getProperty("destinationLatitude"));
        destination.setLongitude((Double) node.getProperty("destinationLongitude"));
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Node getNodeWeak(GraphDatabaseService db) throws NotInTransactionException {
        return null; // We only identify journey nodes by id..
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void clonePropertiesToNode(Node node) throws NotInTransactionException {
        setNodeProperty(node, "createdAt", createdAt);
        setNodeProperty(node, "finishedAt", finishedAt);

        if (source != null) {
            setNodeProperty(node, "sourceLatitude", source.getLatitude());
            setNodeProperty(node, "sourceLongitude", source.getLongitude());
        }

        if (destination != null) {
            setNodeProperty(node, "destinationLatitude", destination.getLatitude());
            setNodeProperty(node, "destinationLongitude", destination.getLongitude());
        }
    }
}