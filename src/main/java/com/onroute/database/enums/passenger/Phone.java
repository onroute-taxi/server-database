package com.onroute.database.enums.passenger;


/**
 * Need to correlate all this data together
 */
public interface Phone {
    /**
     * TODO: The carrier can help us understand more about the user using public data.
     */
    enum Carriers {
        AIRCEL,
        AIRTEL_INDIA,
        BSNL_CELLONE_CDMA,
        BSNL_CELLONE_GSM,
        MTNL_DOLPHIN,
        IDEA,
        MTS_INDIA,
        PING_CDMA_HFCL_INFOTEL,
        RELIANCE_MOBILE_CDMA,
        RELIANCE_MOBILE_GSM,
        RELIANCE_JIO_INFOCOMM,
        TATA_DOCOMO,
        TATA_INDICOM,
        TELENOR,
        VODAFONE_INDIA,
        VIDEOCON,
    }

    /**
     * TODO: The brand can also help us understand more about the user using public data.
     */
    enum Brand {
        APPLE,
        HTC,
        MICROMAX,
        NOKIA,
        SAMSUNG,
        ACER,
        BIRD,
        BLACKBERRY,
        CELKON,
        ERICSSON,
        FLY,
        G_FONE,
        GIONEE,
        HAIER,
        HP,
        HUAWEI,
        IBALL,
        IMATE,
        INQ,
        INTEX,
        IPAQ,
        KARBONN,
        LAVA,
        LEMON,
        LENOVO,
        LEPHONE,
        LG,
        MAXX,
        MITSUBISHI,
        MOTOROLA,
        MTS,
        O2,
        ONIDA,
        PALM_ONE,
        PANASONIC,
        RELIANCE,
        SAGEM,
        SIEMENS,
        SONY,
        SPICE,
        TATA,
        TCL,
        VIDEOCON,
        VOX,
        XIAOMI,
        XOLO,
        OTHER,
    }

    /**
     * This is particlulary useful when an advertiser wants to target an ad based on a particular
     * Operating system.
     */
    enum OS {
        ANDROID,
        APPLE,
        BLACKBERRY,
        WINDOWS,
        OTHER
    }
}