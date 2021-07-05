package streamprocessor;

import java.io.Serializable;

public class VehicleLocation implements Serializable {

    private int vehicleId; //unique id for each vehicle
    private boolean online; //weather vehilce is online or offline
    private boolean available; // is vehilcle ready to take bookings or not
    private double latitude;
    private double longitude;
    private String ipAddress;

    public String getCountry() {
        return country;
    }

    private String country;

    public void setCountry(String country) {
        this.country = country;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public VehicleLocation(int vehicleId, boolean online, boolean available, double latitude, double longitude, String ipAddress) {
        this.vehicleId = vehicleId;
        this.online = online;
        this.available = available;
        this.latitude = latitude;
        this.longitude = longitude;
        this.ipAddress = ipAddress;
    }

    public VehicleLocation() {}

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

}
