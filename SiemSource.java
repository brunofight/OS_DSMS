package demo.eventTime;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;
import scala.Int;

import java.util.Random;

public class SiemSource extends RichSourceFunction<SiemEvent> {

    boolean isRunning = true;
    long startTime;

    long[][] outOfOrderEventTimes = { {534, 2}, {332, 2}, {965, 2}, {1345, 2}, {1234, 2}, {876, 2}, {954, 2},
            {1111, 2}, {1876, 2}, {667, 2}, {3223, 2}, {3110, 2}, {3230, 2}, {78, 2}};
    int index = -1;

    @Override
    public void run(SourceContext<SiemEvent> ctx) throws Exception {

        Random rng = new Random();
        startTime = System.currentTimeMillis();

        Thread.sleep(3000);

        while (isRunning) {

            Tuple2<SiemEvent,Long> nextEvent = getNextEvent();

            ctx.collectWithTimestamp(nextEvent.f0, startTime + nextEvent.f1);
            Thread.sleep(100);

        }

        Thread.sleep(2000);
    }

    private Tuple2<SiemEvent,Long> getNextEvent() {
        index++;

        if (index == outOfOrderEventTimes.length - 1) {
            isRunning = false;
        }

        return new Tuple2<SiemEvent, Long>(new SiemEvent((int) outOfOrderEventTimes[index][1], outOfOrderEventTimes[index][0]),
                outOfOrderEventTimes[index][0]);
    }


    @Override
    public void cancel() {
        this.isRunning = false;
    }
}
