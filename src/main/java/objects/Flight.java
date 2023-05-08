package objects;

import java.util.Date;

public class Flight {
    private Integer id_flight;
    final private String id_user;
    final private Date creation_date;
    private Date departure_date;
    private Date arrival_date;
    private String departure_city;
    private String arrival_city;

    public Flight(Integer id_flight, String id_user, Date creation_date, Date departure_date, Date arrival_date, String departure_city, String arrival_city) {
        this.id_flight = id_flight;
        this.id_user = id_user;
        this.creation_date = creation_date;
        this.departure_date = departure_date;
        this.arrival_date = arrival_date;
        this.departure_city = departure_city;
        this.arrival_city = arrival_city;
    }
//    public Flight(String id_user, Date creation_date, Date departure_date, Date arrival_date, String departure_city, String arrival_city) {
//        this.id_user = id_user;
//        this.creation_date = creation_date;
//        this.departure_date = departure_date;
//        this.arrival_date = arrival_date;
//        this.departure_city = departure_city;
//        this.arrival_city = arrival_city;
//    }
    public String getId_user() {
        return id_user;
    }
    public Date getCreation_date() {
        return creation_date;
    }
    public Date getDeparture_date() {
        return departure_date;
    }
    public Date getArrival_date() {
        return arrival_date;
    }
    public String getArrival_city() {
        return arrival_city;
    }
    public String getDeparture_city() {
        return departure_city;
    }
    public Integer getId_flight() {
        return id_flight;
    }
}

