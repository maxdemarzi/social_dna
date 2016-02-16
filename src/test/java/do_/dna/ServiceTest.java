package do_.dna;

import org.junit.Rule;
import org.junit.Test;
import org.neo4j.harness.junit.Neo4jRule;
import org.neo4j.test.server.HTTP;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.assertArrayEquals;

public class ServiceTest {
    @Rule
    public Neo4jRule neo4j = new Neo4jRule()
            .withFixture(TEST_DATA)
            .withExtension("/v1", Service.class);

    @Test
    public void shouldGetRecommendations() {
        HTTP.Response response = HTTP.POST(neo4j.httpURI().resolve("/v1/service/recommend").toString(),
                QUERY_MAP);
        ArrayList actual = response.content();
        assertArrayEquals(EXPECTED.toArray(), actual.toArray());
    }

    public static HashMap<String, Object> QUERY_MAP = new HashMap<String, Object>(){{
        put("source_user_id", "u1");
        put("target_country_code", "RU");
        put("source_gender", "f");
        put("target_gender", "f");
    }};

    static HashMap<String, Object> R1 = new HashMap<String, Object>(){{
        put("userId","r1");
        put("birthday", 39);
        put("countryCode", "RU");
        put("gender", "f");
    }};

    static HashMap<String, Object> R2 = new HashMap<String, Object>(){{
        put("userId","r2");
        put("birthday", 29);
        put("countryCode", "RU");
        put("gender", "f");
    }};

    static HashMap<String, Object> R3 = new HashMap<String, Object>(){{
        put("userId","r3");
        put("birthday", 28);
        put("countryCode", "RU");
        put("gender", "f");
    }};

    public static ArrayList<HashMap> EXPECTED = new ArrayList<HashMap>(){{
        add(R1);
        add(R2);
        add(R3);
    }};

    public static final String TEST_DATA =
            new StringBuilder()
                    .append("CREATE (u1:User {userId:'u1', birthday:30, countryCode:'RU', gender:'m'})")
                    .append("CREATE (u2:User {userId:'u2', birthday:40, countryCode:'RU', gender:'f'})")
                    .append("CREATE (u3:User {userId:'u3', birthday:20, countryCode:'RU', gender:'f'})")
                    .append("CREATE (u4:User {userId:'u4', birthday:32, countryCode:'RU', gender:'f'})")
                    .append("CREATE (u5:User {userId:'u5', birthday:41, countryCode:'RU', gender:'f'})")
                    .append("CREATE (s1:User {userId:'s1', birthday:24, countryCode:'RU', gender:'f'})")
                    .append("CREATE (s2:User {userId:'s2', birthday:29, countryCode:'RU', gender:'m'})")
                    .append("CREATE (s3:User {userId:'s3', birthday:32, countryCode:'RU', gender:'f'})")
                    .append("CREATE (r1:User {userId:'r1', birthday:39, countryCode:'RU', gender:'f'})")
                    .append("CREATE (r2:User {userId:'r2', birthday:29, countryCode:'RU', gender:'f'})")
                    .append("CREATE (r3:User {userId:'r3', birthday:28, countryCode:'RU', gender:'f'})")
                    .append("CREATE (u1)-[:LIKE]->(u2)")
                    .append("CREATE (u1)-[:DISLIKE]->(u3)")
                    .append("CREATE (u1)-[:BLOCK]->(u4)")
                    .append("CREATE (u1)-[:MATCH]->(u5)")
                    .append("CREATE (s1)-[:LIKE]->(u2)")
                    .append("CREATE (s1)-[:LIKE]->(u3)")
                    .append("CREATE (s1)-[:LIKE]->(r1)")
                    .append("CREATE (s1)-[:LIKE]->(r2)")
                    .append("CREATE (s1)-[:LIKE]->(r3)")
                    .append("CREATE (s2)-[:LIKE]->(u5)")
                    .append("CREATE (s3)-[:LIKE]->(u2)")
                    .append("CREATE (s3)-[:DISLIKE]->(u3)")
                    .append("CREATE (s3)-[:LIKE]->(u4)")
                    .append("CREATE (s3)-[:BLOCK]->(u5)")
                    .toString();
}
