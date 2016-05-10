package com.onroute.database.models.base;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.NotInTransactionException;


public interface Model {
    /**
     * Copies the properties from the given node to the model
     *
     * @param node The source node where the properties are copied from.
     * @throws NotInTransactionException Thrown if the function is not wrapped in a transaction.
     */
    void clonePropertiesFromNode(Node node) throws NotInTransactionException;


    /**
     * This function should map all the properties of the current class to the given Node.
     *
     * @param node The desitination node where the properties need to be copied to.
     * @throws NotInTransactionException Thrown if the function is not wrapped in a transaction.
     */
    void clonePropertiesToNode(Node node) throws NotInTransactionException;
}
