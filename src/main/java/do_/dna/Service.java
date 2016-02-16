package do_.dna;

import org.codehaus.jackson.map.ObjectMapper;
import org.neo4j.graphdb.*;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.*;

@Path("/service")
public class Service {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final ReverseComparator REVERSE_COMPARATOR = new ReverseComparator();

    @POST
    @Path("/recommend")
    public Response Recommend(String body, @Context GraphDatabaseService db) throws IOException {
        List<Map<String, Object>> results = new ArrayList<>();
        Set<Long> likedUsers = new HashSet<>();
        Set<Long> dislikedUsers = new HashSet<>();

        // Validate our input or exit right away
        HashMap input = Validators.getValidCypherStatements(body);
        Integer skip_similar_person = (Integer)input.get("skip_similar_person");
        Integer limit_similar_person = (Integer)input.get("limit_similar_person");
        Integer skip_person = (Integer)input.get("skip_person");
        Integer limit_person = (Integer)input.get("limit_person");

        try (Transaction tx = db.beginTx()) {
            final Node user = db.findNode(Labels.User, "userId", input.get("source_user_id"));

            if (user != null) {
                for (Relationship rel : user.getRelationships(Direction.OUTGOING, RelationshipTypes.MATCH, RelationshipTypes.LIKE)) {
                    likedUsers.add(rel.getEndNode().getId());
                }

                for (Relationship rel : user.getRelationships(RelationshipTypes.BLOCK, RelationshipTypes.DISLIKE)) {
                    dislikedUsers.add(rel.getEndNode().getId());
                }

                ArrayList<Node> similar = new ArrayList<>();
                Result result = db.execute("MATCH (similar:User)" +
                        "USING INDEX similar:User(birthday)\n" +
                        "WHERE similar.birthday >= {target_age_gte} AND\n" +
                        "      similar.birthday <= {target_age_lte} AND\n" +
                        "      similar.countryCode = {target_country_code} AND\n" +
                        "      similar.gender = {source_gender}" +
                        "RETURN similar", input);
                Iterator<Node> nodes = result.columnAs("similar");
                while (nodes.hasNext()) {
                    similar.add(nodes.next());
                }

                // Get up to X Similar Users
                HashMap<Node, int[]> otherUsers = countSimilarLikes(similar, likedUsers);
                List<Map.Entry<Node, int[]>> otherUserList = new ArrayList<>(otherUsers.entrySet());

                Collections.sort(otherUserList, REVERSE_COMPARATOR);
                otherUserList = otherUserList.subList(skip_similar_person, Math.min(otherUserList.size(), skip_similar_person + limit_similar_person));

                HashMap<Node, int[]> recommendCounts = new HashMap<>();
                for (Map.Entry<Node, int[]> entry : otherUserList) {
                    Node similarUser = entry.getKey();
                    for (Relationship rel : similarUser.getRelationships(RelationshipTypes.MATCH, RelationshipTypes.LIKE)) {
                        Node node = rel.getOtherNode(similarUser);
                        if (node.getProperty("gender").equals(input.get("target_gender")) &&
                                (Long)node.getProperty("birthday", 120L) >= (Integer)input.get("recommendation_age_gte") &&
                                (Long)node.getProperty("birthday", 0L) <= (Integer)input.get("recommendation_age_lte")) {
                            int[] count = recommendCounts.get(node);
                            if (count == null) {
                                recommendCounts.put(node, new int[]{1});
                            } else {
                                count[0]++;
                            }
                        }
                    }
                }

                List<Map.Entry<Node, int[]>> recommendUserList = new ArrayList<>(recommendCounts.entrySet());
                Collections.sort(recommendUserList, REVERSE_COMPARATOR);
                recommendUserList = recommendUserList.subList(skip_person, Math.min(recommendUserList.size(), skip_person + limit_person));
                for (Map.Entry<Node, int[]> entry : recommendUserList) {
                    Long nodeId = entry.getKey().getId();
                    if (!likedUsers.contains(nodeId) && !dislikedUsers.contains(nodeId)) {
                        results.add(entry.getKey().getAllProperties());
                    }
                }
            }
        }
        return Response.ok().entity(objectMapper.writeValueAsString(results)).build();
    }

    private static HashMap<Node, int[]> countSimilarLikes(ArrayList<Node> similarUsers, Set<Long> likedUsers) {
        HashMap<Node, int[]> similarCounts = new HashMap<>();
        for (Node similarUser : similarUsers) {
            for (Relationship rel : similarUser.getRelationships(Direction.OUTGOING,
                    RelationshipTypes.MATCH, RelationshipTypes.LIKE)) {
                if (likedUsers.contains(rel.getEndNode().getId())) {
                    int[] count = similarCounts.get(similarUser);
                    if (count == null) {
                        similarCounts.put(similarUser,new int[]{1});
                    } else {
                        count[0]++;
                    }
                };
            }

        }
        return similarCounts;
    }


}
