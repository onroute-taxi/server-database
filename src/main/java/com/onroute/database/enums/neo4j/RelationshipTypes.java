package com.onroute.database.enums.neo4j;

import org.neo4j.graphdb.RelationshipType;

public enum RelationshipTypes implements RelationshipType {
    //    FRIENDS,
//    HAS_DEVICE,
//    DRIVES_CAR,
    HAS_PASSENGER,
    USING_TABLET,
    //    LOCATION_ON_JOURNEY_FINISH,
//    LOCATION_ON_START,
//    LOCATED_AT,
//    OPENED_APP,

    SEEN_ADVERTISEMENT,
    INTERACTED_ADVERTISEMENT,

    WATCHED_MOVIE,
    //    LISTENED_MUSIC,
    HAS_CHANGESET,
    CHANGESET,


    FRIEND_OF,
    LIVES_IN
}