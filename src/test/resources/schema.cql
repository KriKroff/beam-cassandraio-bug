CREATE KEYSPACE IF NOT EXISTS demo_ks WITH REPLICATION = {'class': 'SimpleStrategy', 'replication_factor': 1 };

USE demo_ks;

CREATE TABLE IF NOT EXISTS demo
(
    id                uuid,
    name              text,
    PRIMARY KEY (id)
);
