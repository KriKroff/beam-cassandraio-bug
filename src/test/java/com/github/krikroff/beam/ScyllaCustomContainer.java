package com.github.krikroff.beam;

import org.testcontainers.utility.DockerImageName;

public class ScyllaCustomContainer extends CassandraCustomContainer {

    private static final String SCYLLA_IMAGE = "scylladb/scylla:4.4.4";

    public ScyllaCustomContainer() {
        super(DockerImageName.parse(SCYLLA_IMAGE).asCompatibleSubstituteFor("cassandra"));
        withCommand("--smp", "1");
        withInitScript("schema.cql");
    }
}
