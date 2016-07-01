package domain;

/**
 * Created by 212303924 on 2016.02.07..
 */
public class DifferenceDataPoint {

    long onward;
    long sideways;
    long timeStamp;

    public long getOnward() {
        return onward;
    }

    public long getSideways() {
        return sideways;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public DifferenceDataPoint( long onward, long sideways, long timeStamp ) {
        this.onward = onward;
        this.sideways = sideways;
        this.timeStamp = timeStamp;
    }
}
