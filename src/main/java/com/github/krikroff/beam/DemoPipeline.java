package com.github.krikroff.beam;

import com.datastax.driver.mapping.annotations.Table;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.coders.SerializableCoder;
import org.apache.beam.sdk.io.cassandra.CassandraIO;
import org.apache.beam.sdk.io.cassandra.CassandraIO.Read;
import org.apache.beam.sdk.io.cassandra.RingRange;
import org.apache.beam.sdk.transforms.Create;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.TypeDescriptor;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class DemoPipeline {


    public static PCollection<CassandraModel> applyPipeline(
        final Pipeline pipeline, final PipelineOptions options
    ) {

        return pipeline.apply("ReadCassandra", getCassandraReader(options));
    }

    public static PCollection<CassandraModel> applyWorkAroundPipeline(
        final Pipeline pipeline, final PipelineOptions options
    ) {

        return pipeline
                   .apply(Create.of(getCassandraReader(options).withRingRanges(Set.of(RingRange.of(BigInteger.valueOf(
                       Long.MIN_VALUE), BigInteger.valueOf(Long.MAX_VALUE))))))
                   .setCoder(SerializableCoder.of(new TypeDescriptor<Read<CassandraModel>>() {
                   })).apply(
                "ReadAll",
                CassandraIO.<CassandraModel>readAll().withCoder(SerializableCoder.of(CassandraModel.class))
            );
    }

    private static Read<CassandraModel> getCassandraReader(final PipelineOptions options) {
        return CassandraIO.<CassandraModel>read().withHosts(getCassandraHostnames(options))
                   .withPort(getCassandraPort(options)).withUsername(getCassandraUsername(options))
                   .withPassword(getCassandraPassword(options)).withKeyspace(getCassandraKeyspace(options))
                   .withTable(getCassandraTableName(CassandraModel.class)).withEntity(CassandraModel.class)
                   .withCoder(SerializableCoder.of(CassandraModel.class));
    }


    private static List<String> getCassandraHostnames(final PipelineOptions options) {
        return List.of(options.getCassandraInputHostnames().get().split(","));
    }

    private static Integer getCassandraPort(final PipelineOptions options) {
        return options.getCassandraInputPort().get();
    }

    private static String getCassandraUsername(final PipelineOptions options) {
        return options.getCassandraInputUsername().get();
    }

    private static String getCassandraPassword(
        final PipelineOptions options
    ) {
        return options.getCassandraInputPassword().get();
    }

    private static String getCassandraKeyspace(final PipelineOptions options) {
        return options.getCassandraInputKeyspace().get();
    }

    private static String getCassandraTableName(final Class<?> modelClass) {
        return Arrays.stream(modelClass.getAnnotations()).filter(Table.class::isInstance).map(Table.class::cast)
                   .map(Table::name).findFirst().orElse(null);
    }

    private static Integer getCassandraMinSplits(final PipelineOptions options) {
        return options.getCassandraInputMinSplits().get();
    }

}
