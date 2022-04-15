package demo.syslog;


import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Properties;

public class SyslogStream {

    public static void main(String[] args) throws Exception {
        // set up the streaming execution environment
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        Properties props = new Properties();
        props.setProperty("bootstrap.servers", "127.0.0.1:9092");
        props.setProperty("group.id", "syslog-test");
        props.setProperty("enable.auto.commit", "false");
        props.setProperty("auto.offset.reset", "earliest");
        props.setProperty(
                "max.partition.fetch.bytes",
                "256");

        FlinkKafkaConsumer<String> consumer = new FlinkKafkaConsumer<String>("syslog", new SimpleStringSchema(), props);

        DataStream<String> stream = env.addSource(consumer);
        stream.print();

        // execute program
        env.execute("Syslog");
    }

}
