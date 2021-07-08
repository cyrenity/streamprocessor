package streamprocessor;

import java.io.Serializable;

public class Message implements Serializable {

    private Long to = null;
    private String telco = null;
    private String service = null;
    private String message = null;

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getTelco() {
        return telco;
    }

    public void setTelco(String telco) {
        this.telco = telco;
    }

    public Long getTo() {
        return to;
    }

    public void setTo(Long to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Message{" +
                "to=" + to +
                ", message='" + message + '\'' +
                '}';
    }
}
