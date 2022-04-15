package demo.windowing;

import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;

public class ProcessingTimeWindowing {

    public static void main(String[] args) throws Exception {
        // set up the streaming execution environment
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStream<Tuple3<Integer, Integer, String>> stream = env.addSource(new SiemSource())
                .flatMap(new Splitter())
                .keyBy(t -> t.f0)
                .window(TumblingProcessingTimeWindows.of(Time.seconds(1)))
                .reduce((t1,t2) -> {t1.f1 += t2.f1; t1.f2 += t2.f2; return t1;})
                ;

        stream.print();

        // execute program
        env.execute("SIEM Analysis");
    }
}
