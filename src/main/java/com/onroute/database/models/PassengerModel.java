package com.onroute.database.models;

import com.onroute.database.enums.RideType;
import com.onroute.database.enums.interests.AdvertisingInterest;
import com.onroute.database.enums.interests.MovieInterest;
import com.onroute.database.enums.interests.MusicInterest;
import com.onroute.database.enums.neo4j.Labels;
import com.onroute.database.enums.passenger.EducationLevel;
import com.onroute.database.enums.passenger.IncomeLevel;
import com.onroute.database.enums.passenger.Language;
import com.onroute.database.enums.passenger.Phone;
import com.onroute.database.models.base.Neo4jModel;
import com.google.gson.annotations.Expose;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.NotInTransactionException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This model repesents a passenger.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PassengerModel extends Neo4jModel {
    /* The MAC address and the phoneNumber are used to uniquely identify the passenger in the
     * system */
    @Expose String macAddress;
    @Expose String phoneNumber;

    /* Other general information */
    @Expose String fullName;
    @Expose String email;
    @Expose int age;
    @Expose boolean isMale;
    @Expose long createdAt;
    @Expose IncomeLevel incomeLevel;
    @Expose EducationLevel educationLevel;
    @Expose Language language;

    /* Information about the passenger's ride */
    @Expose RideType preferredRide;
    @Expose float averageRideTime;
    @Expose float averageRideDistance;

    /* Information about the passenger's phone */
    @Expose Phone.Brand phoneBrand;
    @Expose Phone.OS phoneOS;
    @Expose Phone.Carriers phoneCarrier;

    /* the different probabilities of the passenger. These probabilities gradually improve as the
     * passenger keeps using the system (and we learn more about the passenger). */
    @Expose float ageConfidence;
    @Expose float isMaleConfidence;
    @Expose float isFemaleConfidence;
    @Expose float incomeLevelConfidence;
    @Expose float educationLevelConfidence;
    @Expose float languageConfidence;

    /* These lists represents the interests of the passenger. We keep two arrays because neo4j
     * doesn't support key-val arrays.
     *
     * Gradually this list will grow plotting the user's interests. */
    @Expose List<MovieInterest> movieInterests;
    @Expose List<MusicInterest> musicInterests;
    @Expose List<AdvertisingInterest> advertisingInterests;
    @Expose List<Float> movieInterestScores;
    @Expose List<Float> musicInterestScores;
    @Expose List<Float> advertisingInterestScores;


    public void resetDefault() {
        isMaleConfidence = 0f;
        movieInterests = new ArrayList<>();
        movieInterestScores = new ArrayList<>();
        musicInterests = new ArrayList<>();
        musicInterestScores = new ArrayList<>();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void clonePropertiesFromNode(Node node) throws NotInTransactionException {
        this.node = node;

        macAddress = (String) getNodeProperty(node, "macAddress");
        email = (String) getNodeProperty(node, "email");
        fullName = (String) getNodeProperty(node, "fullName");
        phoneNumber = (String) getNodeProperty(node, "phoneNumber");
        createdAt = (long) getNodeProperty(node, "createdAt");
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Node getNodeWeak(GraphDatabaseService db) throws NotInTransactionException {
        if (macAddress != null) return db.findNode(Labels.Passenger, "macAddress", macAddress);
        else return db.findNode(Labels.Passenger, "phoneNumber", phoneNumber);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void clonePropertiesToNode(Node node) throws NotInTransactionException {
        setNodeProperty(node, "phoneNumber", phoneNumber);
        setNodeProperty(node, "macAddress", macAddress);
        setNodeProperty(node, "fullName", fullName);
        setNodeProperty(node, "email", email);
        setNodeProperty(node, "age", age);

        if (createdAt > 0) setNodeProperty(node, "createdAt", createdAt);
        else setNodeProperty(node, "createdAt", new Date().getTime());
    }
}