package com.backseatmedia.database.models.media;

import com.backseatmedia.database.enums.neo4j.Labels;
import com.backseatmedia.database.enums.interests.MovieInterest;
import com.backseatmedia.database.models.base.Neo4jModel;
import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;
import org.neo4j.graphdb.*;


public class MovieModel extends Neo4jModel {
    @Expose @Getter @Setter String title;
    @Expose @Getter @Setter String studio;
    @Expose @Getter @Setter String rating;
    @Expose @Getter @Setter long year;
    @Expose @Getter @Setter MovieInterest genre;


    public MovieModel() {

    }


    public MovieModel(String title, String studio, String rating, int year, MovieInterest genre) {
        this.title = title;
        this.studio = studio;
        this.rating = rating;
        this.year = year;
        this.genre = genre;
    }


    /**
     * @see Neo4jModel
     */
    @Override
    public void clonePropertiesFromNode(Node node) throws NotInTransactionException {
        genre = MovieInterest.valueOf((String) getNodeProperty(node, "genre"));
        rating = (String) getNodeProperty(node, "rating");
        studio = (String) getNodeProperty(node, "studio");
        title = (String) getNodeProperty(node, "title");
//        year = (long) getNodeProperty(node, "year");
    }


    /**
     * @see Neo4jModel
     */
    @Override
    public Node getNodeWeak(GraphDatabaseService db) throws NotInTransactionException {
        return db.findNode(Labels.Movie, "title", title);
    }


    /**
     * @see Neo4jModel
     */
    @Override
    public void clonePropertiesToNode(Node node) throws NotInTransactionException {
        node.setProperty("genre", genre);
        node.setProperty("rating", rating);
        node.setProperty("studio", studio);
        node.setProperty("title", title);
        node.setProperty("year", year);
    }


    public static MovieModel fromNode(Node node) {
        MovieModel movieModel = new MovieModel();
        movieModel.clonePropertiesFromNode(node);
        return movieModel;
    }
}