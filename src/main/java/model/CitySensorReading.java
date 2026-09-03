package model;

public class CitySensorReading {

    private String type;
    private double latitude;
    private double longitude;
    private String status;
    private int value;

    public CitySensorReading(String type, double latitude, double longitude, String status, int value) {
        this.type = type;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getStatus() {
        return status;
    }

    public int getValue() {
        return value;
    }
}
