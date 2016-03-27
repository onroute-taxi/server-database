package com.backseatmedia.database.models;


import com.backseatmedia.database.enums.DestinationType;
import com.backseatmedia.database.enums.advertisement.AdvertisementType;
import com.backseatmedia.database.enums.interests.AdvertisingInterest;
import com.backseatmedia.database.enums.passenger.AgeGroup;
import com.backseatmedia.database.enums.passenger.Countries;
import com.backseatmedia.database.enums.passenger.IncomeLevel;
import com.backseatmedia.database.enums.passenger.Phone;
import com.backseatmedia.database.models.base.Neo4jModel;
import com.google.gson.annotations.Expose;
import lombok.Data;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.NotInTransactionException;

import java.util.List;


/**
 * TODO: Have target time of day/week/month/season/year implemented.
 */
@Data
public class AdvertisementModel extends Neo4jModel {
    @Expose double funds;
    @Expose double costPerImpression;
    @Expose double costPerClick;
    @Expose long impressions;
    @Expose long clicks;
    @Expose long bids;
    @Expose long bidsWon;


    /**
     * This variables contains any extra data that will be used by the tablet to help display
     * the ad.
     */
    @Expose String data;

    /**
     * If the ad has a video, then this variable will describe the duration of it.
     */
    @Expose long duration;

    /**
     * The target interests we want to cater this advertisement to.
     * <p/>
     * TODO: Have a way to relate the Movie & Music interest to the advertising interets.
     */
    @Expose List<AdvertisingInterest> interests;
    @Expose List<Float> interestValues;

    /**
     * Choose the target income level we want the advertisement to be targetted to. If
     * 'incomeLevelStrict' is set to true, then the ad won't be shown to anyone not fitting in the
     * income range.
     */
    @Expose IncomeLevel preferredIncomeLevel;
    @Expose boolean incomeLevelStrict;

    /**
     * These values decide if an ad can be showed to a passenger who is either a male/female..
     * If 'genderStrict' is true, then the system won't recommend a content to a person it thinks
     * is not in the preferred gender.
     */
    @Expose boolean femalePreferred;
    @Expose boolean malePreferred;
    @Expose boolean genderStrict;

    /**
     * These values note the preferred age group that the advertiser wants to target.
     */
    @Expose List<AgeGroup> preferredAgeGroup;
    @Expose boolean ageStrict;

    /**
     * This list contains the indexes of location nodes that the advertiser wants to focus on. This
     * allows us to have ads that are geo-specific.
     */
    @Expose List<Integer> preferredLocationNodes;

    /**
     * This list contains the preferred personas/destinations that the advertiser might want to
     * target to.
     */
    @Expose List<DestinationType> preferredDestinations;
    @Expose boolean destinationStrict;

    /**
     * This list contains the target phone operating system that the person wants to target to.
     */
    @Expose List<Phone.OS> preferredPhoneOs;
    @Expose boolean phoneOsStrict;

    /**
     * This list contains the countries that the person want to target the ad to.
     */
    @Expose List<Countries> preferredCountries;
    @Expose boolean countryStrict;

    /**
     * This list contains the nationalities that the person want to target the ad to.
     */
    @Expose List<Countries> preferredNationalities;
    @Expose boolean nationalityStrict;

    /**
     * The type of advertisement this is.
     */
    @Expose AdvertisementType type;


    /**
     * {@inheritDoc}
     */
    @Override
    public void clonePropertiesFromNode(Node node) throws NotInTransactionException {

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Node getNodeWeak(GraphDatabaseService db) throws NotInTransactionException {
        return null;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void clonePropertiesToNode(Node node) throws NotInTransactionException {

    }
}