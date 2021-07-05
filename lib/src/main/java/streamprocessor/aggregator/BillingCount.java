package streamprocessor.aggregator;

import java.io.Serializable;
import java.util.Date;

public class BillingCount implements Serializable {
    Long start;
    Long end;
    Long count;


    public BillingCount(Long start, Long end, Long count) {
        this.start = start;
        this.end = end;
        this.count = count;
    }

    public Long getStart() {
        return start;
    }

    public void setStart(Long start) {
        this.start = start;
    }

    public Long getEnd() {
        return end;
    }

    public void setEnd(Long end) {
        this.end = end;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "BillingCount{" +
                "start=" + new Date(start) +
                ", end=" + new Date(end) +
                ", count=" + count +
                '}';
    }
}
