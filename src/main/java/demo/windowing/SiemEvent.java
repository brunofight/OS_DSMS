package demo.windowing;

public class SiemEvent {

    public int value;
    public long eventTime = 0;

    public SiemEvent() {

    }

    public SiemEvent(int _value, long _eventTime) {
        this.eventTime = _eventTime;
        this.value = _value;
    }

}
