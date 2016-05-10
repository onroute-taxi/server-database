package com.onroute.database.models.base;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import lombok.Data;
import lombok.Getter;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.NotInTransactionException;


/**
 * This class is used to provide common functions to all models.
 */
@Data
public abstract class Neo4jModel implements Model {
    @Getter protected Node node;
    @Expose protected long id;


    /**
     * Find a unique node uniquely identifying this model.
     * <p/>
     * The function should search in the DB applying the right filters and must return the
     * required Node.
     *
     * @param db The GraphDatabase instance to use for querying.
     * @return The node that matches
     * @throws NotInTransactionException Thrown if the function is not wrapped in a transaction.
     */
    public final Node getNode(GraphDatabaseService db) throws NotInTransactionException {
        // First check our cache.
        if (node != null) return node;

        // If not found, then check by id!
        if (id > 0) return db.getNodeById(id);

        // If there was no id set, then get the node from our 'weak' get. This function has to be implemented by the
        // inheriting class.
        Node node = getNodeWeak(db);

        // Cache the node and return
        if (node != null) {
            id = node.getId();
            this.node = node;
        }
        return node;
    }


    /**
     * A helper function to safely extract properties from a node.
     *
     * @param node     The target node to get properties from
     * @param property The property to extract
     * @return The object returned. Null if nothing was set.
     */
    public Object getNodeProperty(Node node, String property) {
        if (node.hasProperty(property)) return node.getProperty(property);
        return null;
    }


    /**
     * A helper function to safely set a property to a node.
     *
     * @param node     The node to set properties to
     * @param property The property's key
     * @param data     The property's data
     */
    protected void setNodeProperty(Node node, String property, Object data) {
        if (node != null && data != null) node.setProperty(property, data);
    }


    /**
     * This function is used to do a 'weak' get for a Node. It is weak, because it does not look in
     * the cache nor does it fetch the node by id.
     *
     * Ideally, you would query the db using the id or the cache but if none of that exists, then you query using
     * your own parameters. So this function should implement a query with those custom parameters. See implementations
     * for examples.
     *
     * @param db The GraphDatabase instance to use for querying.
     * @return The node that matches
     * @throws NotInTransactionException Thrown if the function is not wrapped in a transaction.
     */
    protected abstract Node getNodeWeak(GraphDatabaseService db) throws NotInTransactionException;

    public String toJSON(Gson gson) {
        return gson.toJson(this);
    }
}