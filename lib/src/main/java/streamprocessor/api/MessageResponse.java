package streamprocessor.api;

import java.io.Serializable;
import java.util.Date;

public class MessageResponse implements Serializable {
    public String status;
    public String message;
    public Date timestamp = new Date();

    public MessageResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "MessageResponse{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }

}
