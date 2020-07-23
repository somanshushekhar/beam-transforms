package com.beam.core;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;

import com.beam.core.options.MyOptions;

public class CommandLineArgsDemo {

	public static void main(String[] args) {
		
		PipelineOptionsFactory.register(MyOptions.class);
		PipelineOptions options =
				PipelineOptionsFactory.fromArgs(args).withValidation().as(MyOptions.class);
		
		for(String arg:args) {
			System.out.println(arg);
		}
		
		Pipeline pipeline = Pipeline.create(options);
		pipeline.run(options).waitUntilFinish();
	}

}
