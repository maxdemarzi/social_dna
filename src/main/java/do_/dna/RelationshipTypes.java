package do_.dna;

import org.neo4j.graphdb.RelationshipType;

public enum RelationshipTypes implements RelationshipType {
    LIKE,
    MATCH,
    BLOCK,
    DISLIKE
}
