package demo.eventTime;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.util.Collector;

public class Splitter implements FlatMapFunction<SiemEvent, Tuple3<Integer, Integer, String>> {

    @Override
    public void flatMap(SiemEvent siemEvent, Collector<Tuple3<Integer, Integer, String>> collector) throws Exception {
        collector.collect(new Tuple3<Integer, Integer,String>(1,siemEvent.value, siemEvent.eventTime + ", "));
    }
}
