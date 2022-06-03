package com.github.krikroff.beam;

import org.apache.beam.runners.dataflow.options.DataflowPipelineOptions;

public interface PipelineOptions extends PipelineOptionsInputCassandra, DataflowPipelineOptions {
}
