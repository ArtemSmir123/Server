package objects;

import org.json.simple.JSONObject;

public class Plane {
    private final Integer id_plane;
    private String model;
    private String fullTitle;
    private Integer numberOfSeats;
    public Plane (int id_plane, String model, String fullTitle, int numberOfSeats){
        this.id_plane = id_plane;
        this.model = model;
        this.fullTitle = fullTitle;
        this.numberOfSeats = numberOfSeats ;
    }
    @Override
    public String toString() {
        return " " + id_plane.toString() + " " + model.toString() + " " + fullTitle.toString() + " " + numberOfSeats.toString() ;
    }
    public String getModel() {
        return model;
    }
    public Integer getNumberOfSeats() {
        return numberOfSeats;
    }
    public String getFullTitle() {
        return fullTitle;
    }
    public Integer getId_plane() {
        return id_plane;
    }

}
