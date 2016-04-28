package com.onroute.database.engines.movies.utils;

import com.graphaware.reco.generic.result.PartialScore;
import com.graphaware.reco.generic.result.Recommendation;
import com.graphaware.reco.generic.result.Score;
import org.neo4j.graphdb.Node;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ScoreUtils {
    public static void assertScoresEqual(Recommendation<Node> expected, Recommendation<Node> recommendation) {
        Score recommendationScore = recommendation.getScore();
        Score expectedScore = expected.getScore();
        assertEquals(expectedScore.getTotalScore(), recommendationScore.getTotalScore(), 0);
        assertEquals(expectedScore.getScoreParts().size(), recommendationScore.getScoreParts().size());
        for (String partialScoreName : expectedScore.getScoreParts().keySet()) {
            PartialScore recommendedPartialScore = recommendationScore.getScoreParts().get(partialScoreName);
            PartialScore expectedPartialScore = expectedScore.getScoreParts().get(partialScoreName);
            assertNotNull(recommendedPartialScore);
            assertEquals(expectedPartialScore.getValue(), recommendedPartialScore.getValue(), 0);
            assertEquals(expectedPartialScore.getReasons(), recommendedPartialScore.getReasons());
        }
    }
}