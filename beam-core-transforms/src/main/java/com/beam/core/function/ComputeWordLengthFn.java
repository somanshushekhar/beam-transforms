package com.beam.core.function;

import org.apache.beam.sdk.transforms.DoFn;

public class ComputeWordLengthFn extends DoFn<String, Integer> {

	@ProcessElement
	public void process(@Element String word,OutputReceiver<Integer> out) {
		System.out.println(word+":"+word.length());
		out.output(word.length());
	}
}
