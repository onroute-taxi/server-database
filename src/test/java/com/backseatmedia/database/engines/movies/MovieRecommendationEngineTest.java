package com.backseatmedia.database.engines.movies;


import com.graphaware.test.integration.WrappingServerIntegrationTest;
import org.junit.Ignore;
import org.junit.Test;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

@Ignore
public class MovieRecommendationEngineTest extends WrappingServerIntegrationTest {
//    MovieRecommendationEngine recommendationEngine;
//    RecommendationsRememberingLogger rememberingLogger;


    @Override
    public void setUp() throws Exception {
        super.setUp();
//        recommendationEngine = new MovieRecommendationEngine();
//
//        rememberingLogger = new RecommendationsRememberingLogger();
//        rememberingLogger.clear();
    }


    @Override
    protected void populateDatabase(GraphDatabaseService database) {
        database.execute(
                "CREATE " +
                        "(m:Person:Male {name:'Michal', age:30})," +
                        "(d:Person:Female {name:'Daniela', age:20})," +
                        "(v:Person:Male {name:'Vince', age:40})," +
                        "(a:Person:Male {name:'Adam', age:30})," +
                        "(l:Person:Female {name:'Luanne', age:25})," +
                        "(b:Person:Male {name:'Christophe', age:60})," +
                        "(j:Person:Male {name:'Jim', age:38})," +

                        "(lon:City {name:'London'})," +
                        "(mum:City {name:'Mumbai'})," +
                        "(br:City {name:'Bruges'})," +

                        "(m)-[:FRIEND_OF]->(d)," +
                        "(m)-[:FRIEND_OF]->(l)," +
                        "(m)-[:FRIEND_OF]->(a)," +
                        "(m)-[:FRIEND_OF]->(v)," +
                        "(d)-[:FRIEND_OF]->(v)," +
                        "(b)-[:FRIEND_OF]->(v)," +
                        "(j)-[:FRIEND_OF]->(v)," +
                        "(j)-[:FRIEND_OF]->(m)," +
                        "(j)-[:FRIEND_OF]->(a)," +
                        "(a)-[:LIVES_IN]->(lon)," +
                        "(d)-[:LIVES_IN]->(lon)," +
                        "(v)-[:LIVES_IN]->(lon)," +
                        "(m)-[:LIVES_IN]->(lon)," +
                        "(j)-[:LIVES_IN]->(lon)," +
                        "(c)-[:LIVES_IN]->(br)," +
                        "(l)-[:LIVES_IN]->(mum)");
    }


    @Test
    public void shouldRecommendRealTime() {
        try (Transaction tx = getDatabase().beginTx()) {

//            List<Recommendation<Node>> recoForVince = recommendationEngine.recommend(getPersonByName("Vince"), new SimpleConfig(2));
//
//            String expectedForVince = "Computed recommendations for Vince: (Adam {total:41.99417, ageDifference:-5.527864, friendsInCommon: {value:27.522034, {value:1.0, name:Jim}, {value:1.0, name:Michal}}, sameGender:10.0, sameLocation: {value:10.0, {value:10.0, location:London}}}), (Luanne {total:7.856705, ageDifference:-7.0093026, friendsInCommon: {value:14.866008, {value:1.0, name:Michal}}})";
//            System.out.println(rememberingLogger.toString(getPersonByName("Vince"), recoForVince, null));

//            assertEquals(2, recoForVince.size());
//            ScoreUtils.assertScoresEqual(recommendedAdam(), recoForVince.get(0));
//            ScoreUtils.assertScoresEqual(recommendedLuanne(), recoForVince.get(1));

            tx.success();
        }
    }


    private Node getPersonByName(String name) {
        return getDatabase().findNode(DynamicLabel.label("Person"), "name", name);
    }
}