package com.backseatmedia.database.models.base;

import org.neo4j.graphdb.GraphDatabaseService;
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
     * This function is used to do a 'weak' get for a Node. It is weak, because it does not look in
     * the cache nor does it fetch the node by id.
     *
     * @param db The GraphDatabase instance to use for querying.
     * @return The node that matches
     * @throws NotInTransactionException Thrown if the function is not wrapped in a transaction.
     */
    Node getNodeWeak(GraphDatabaseService db) throws NotInTransactionException;


    /**
     * This function should map all the properties of the current class to the given Node.
     *
     * @param node The desitination node where the properties need to be copied to.
     * @throws NotInTransactionException Thrown if the function is not wrapped in a transaction.
     */
    void clonePropertiesToNode(Node node) throws NotInTransactionException;
}
