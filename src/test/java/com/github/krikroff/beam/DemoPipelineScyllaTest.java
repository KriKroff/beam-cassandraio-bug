package com.github.krikroff.beam;

import org.apache.beam.repackaged.core.org.apache.commons.lang3.RandomUtils;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.options.ValueProvider;
import org.apache.beam.sdk.testing.PAssert;
import org.apache.beam.sdk.testing.TestPipeline;
import org.apache.beam.sdk.transforms.Count;
import org.apache.beam.sdk.values.PCollection;
import org.junit.Rule;
import org.junit.Test;

public class DemoPipelineScyllaTest {

    @Rule
    public final transient TestPipeline pipeline = TestPipeline.create();

    @Rule
    public final CassandraCustomContainer scyllaCustomContainer = new ScyllaCustomContainer();

    @Test
    public void cassandraIOReadShouldFail() {
        final long nbToCreate = RandomUtils.nextLong(300L, 500L);
        scyllaCustomContainer.createElements(nbToCreate);

        final PCollection<Long> count = DemoPipeline.applyPipeline(pipeline, buildOptions()).apply("Count", Count.globally());
        PAssert.thatSingleton(count).isEqualTo(nbToCreate);
        
        pipeline.run();
    }


    @Test
    public void cassandraIOReadAllWorkAroundShouldWork() {
        final long nbToCreate = RandomUtils.nextLong(300L, 500L);
        scyllaCustomContainer.createElements(nbToCreate);

        final PCollection<Long> count =
            DemoPipeline.applyWorkAroundPipeline(pipeline, buildOptions()).apply("Count", Count.globally());
        PAssert.thatSingleton(count).isEqualTo(nbToCreate);

        pipeline.run();
    }


    private PipelineOptions buildOptions() {
        final PipelineOptions options = PipelineOptionsFactory.as(PipelineOptions.class);

        options.setCassandraInputHostnames(ValueProvider.StaticValueProvider.of(scyllaCustomContainer.getHost()));
        options.setCassandraInputPort(ValueProvider.StaticValueProvider.of(scyllaCustomContainer.getPort()));
        options.setCassandraInputUsername(ValueProvider.StaticValueProvider.of(scyllaCustomContainer.getUsername()));
        options.setCassandraInputPassword(ValueProvider.StaticValueProvider.of(scyllaCustomContainer.getPassword()));
        options.setCassandraInputKeyspace(ValueProvider.StaticValueProvider.of(scyllaCustomContainer.getKeyspace()));
        options.setCassandraInputMinSplits(ValueProvider.StaticValueProvider.of(32));

        return options;
    }
}
