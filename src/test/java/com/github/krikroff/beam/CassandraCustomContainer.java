package com.github.krikroff.beam;

import com.datastax.driver.core.Session;
import com.datastax.driver.core.utils.UUIDs;
import org.testcontainers.containers.CassandraContainer;
import org.testcontainers.utility.DockerImageName;

import java.net.InetAddress;
import java.util.stream.LongStream;

import static com.github.krikroff.beam.CassandraModel.TABLE_NAME;

public class CassandraCustomContainer extends CassandraContainer {

    private static final String SCYLLA_IMAGE = "scylladb/scylla:4.4.4";

    private static final String KEYSPACE = "demo_ks";

    public CassandraCustomContainer() {
        withInitScript("schema.cql");
    }

    protected CassandraCustomContainer(final DockerImageName dockerImageName) {
        super(dockerImageName);
        withInitScript("schema.cql");
    }

    @Override
    public String getHost() {
        final String host = super.getHost();
        if ("localhost".equals(host)) { // Deals with IPv4/IPv6 resolution that randomly fails with Cassandra/TestContainers.
            return InetAddress.getLoopbackAddress().getHostAddress();
        }

        return host;
    }


    public Integer getPort() {
        return getMappedPort(CQL_PORT);
    }

    public String getKeyspace() {
        return KEYSPACE;
    }


    public void createElements(long nbElements) {
        try (final Session session = getCluster().newSession()) {
            LongStream.range(0, nbElements).forEach(idx -> session.execute(String.format(
                "INSERT INTO %s.%s (id,name) " + "VALUES (%s, '%s');",
                KEYSPACE,
                TABLE_NAME,
                UUIDs.timeBased(),
                "Elt-" + idx
            )));
        }
    }

}
