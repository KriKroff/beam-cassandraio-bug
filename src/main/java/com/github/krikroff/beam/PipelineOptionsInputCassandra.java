package com.github.krikroff.beam;

import org.apache.beam.runners.dataflow.options.DataflowPipelineOptions;
import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.ValueProvider;

interface PipelineOptionsInputCassandra extends DataflowPipelineOptions {

    @Description("Getter: Cassandra Input hostnames")
    ValueProvider<String> getCassandraInputHostnames();

    @Description("Setter: Cassandra Input hostnames")
    void setCassandraInputHostnames(ValueProvider<String> hostnames);

    @Description("Getter: Cassandra Input port")
    ValueProvider<Integer> getCassandraInputPort();

    @Description("Setter: Cassandra Input port")
    void setCassandraInputPort(ValueProvider<Integer> port);

    @Description("Getter: Cassandra Input username")
    ValueProvider<String> getCassandraInputUsername();

    @Description("Setter: Cassandra Input username")
    void setCassandraInputUsername(ValueProvider<String> username);

    @Description("Getter: Cassandra Input password")
    ValueProvider<String> getCassandraInputPassword();

    @Description("Setter: Cassandra Input password")
    void setCassandraInputPassword(ValueProvider<String> password);

    @Description("Getter: Cassandra Input keyspace")
    ValueProvider<String> getCassandraInputKeyspace();

    @Description("Setter: Cassandra Input keyspace")
    void setCassandraInputKeyspace(ValueProvider<String> keyspace);

    @Description("Getter: Cassandra Input min splits")
    ValueProvider<Integer> getCassandraInputMinSplits();

    @Description("Setter: Cassandra Input min splits")
    void setCassandraInputMinSplits(ValueProvider<Integer> minSplits);
}
