package model;

import java.io.Serializable;

/**
 * Created by User on 25/11/2015.
 */
public class Ubicacion implements Serializable{

    double latitude;
    double longitude;

    public Ubicacion(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
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
