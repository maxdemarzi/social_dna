Medium Unmanaged Extension
================================

This is an unmanaged extension translating a Cypher query.

1. Build it:

        mvn clean package

2. Copy target/target/recommendation-1.0.jar to the plugins/ directory of your Neo4j server.


3. Configure Neo4j by adding a line to conf/neo4j-server.properties:

        org.neo4j.server.thirdparty_jaxrs_classes=do_.dna=/v1

4. Start Neo4j server.

5. Create some sample data:

        CREATE (u1:User {userId:'u1', birthday:30, countryCode:'RU', gender:'m'})
        CREATE (u2:User {userId:'u2', birthday:40, countryCode:'RU', gender:'f'})
        CREATE (u3:User {userId:'u3', birthday:20, countryCode:'RU', gender:'f'})
        CREATE (u4:User {userId:'u4', birthday:32, countryCode:'RU', gender:'f'})
        CREATE (u5:User {userId:'u5', birthday:41, countryCode:'RU', gender:'f'})
        CREATE (s1:User {userId:'s1', birthday:24, countryCode:'RU', gender:'f'})
        CREATE (s2:User {userId:'s2', birthday:29, countryCode:'RU', gender:'m'})
        CREATE (s3:User {userId:'s3', birthday:32, countryCode:'RU', gender:'f'})
        CREATE (r1:User {userId:'r1', birthday:39, countryCode:'RU', gender:'f'})
        CREATE (r2:User {userId:'r2', birthday:29, countryCode:'RU', gender:'f'})
        CREATE (r3:User {userId:'r3', birthday:28, countryCode:'RU', gender:'f'})
        CREATE (u1)-[:LIKE]->(u2)
        CREATE (u1)-[:DISLIKE]->(u3)
        CREATE (u1)-[:BLOCK]->(u4)
        CREATE (u1)-[:MATCH]->(u5)
        CREATE (s1)-[:LIKE]->(u2)
        CREATE (s1)-[:LIKE]->(u3)
        CREATE (s1)-[:LIKE]->(r1)
        CREATE (s1)-[:LIKE]->(r2)
        CREATE (s1)-[:LIKE]->(r3)
        CREATE (s2)-[:LIKE]->(u5)
        CREATE (s3)-[:LIKE]->(u2)
        CREATE (s3)-[:DISLIKE]->(u3)
        CREATE (s3)-[:LIKE]->(u4)
        CREATE (s3)-[:BLOCK]->(u5)

5. Query it over HTTP:

        :POST /v1/service/recommend {"source_user_id": "u1", "target_country_code": "RU", "source_gender": "f","target_gender": "f"}
