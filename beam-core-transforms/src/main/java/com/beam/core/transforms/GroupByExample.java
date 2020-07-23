package com.beam.core.transforms;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.GroupByKey;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.SimpleFunction;
import org.apache.beam.sdk.values.KV;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GroupByExample {

	public static void main(String[] args) {

		 Pipeline p = Pipeline.create(
			        PipelineOptionsFactory.fromArgs(args).withValidation().create());
		 
		 p.apply(TextIO.read().from("/src/main/resources/groupBySample.txt"))
		 .apply("ConvertToKV", MapElements.via(
				    new SimpleFunction<String, KV<String, Integer>>() {
				        @Override
				        public KV<String, Integer> apply(String input) {
				            String[] split = input.split(",");
				            if (split.length < 2) {
				                return null;
				            }
				            String key = split[0];
				            Integer value = Integer.valueOf(split[1]);
				            return KV.of(key, value);
				        }
				    }
				))
		 .apply(GroupByKey.<String, Integer>create())
		 .apply("AddingValuesByKey", ParDo.of(new DoFn<KV<String, Iterable<Integer>>, String>() {

			    @ProcessElement
			    public void processElement(ProcessContext context) {
			        Integer total = 0;
			        String branch = context.element().getKey();
			        Iterable<Integer> sales = context.element().getValue();
			        for (Integer amount : sales) {
			        	total += amount;
			        }
			        context.output(branch + ": " + total);
			    }
			}))
		 .apply(TextIO.write().to("./src/main/resources/groupByOutput.txt").withoutSharding());
		 
		 p.run().waitUntilFinish();
	}

}
