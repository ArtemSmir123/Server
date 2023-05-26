package objects;

import org.json.simple.JSONObject;

public class City implements Objects {
    private final int id_city;
    private StringBuilder name;


    public City (int id_city, StringBuilder name){
        this.id_city = id_city;
        this.name = name;
    }
    public City (int id_city, String  name){
        this.id_city = id_city;
        this.name = new StringBuilder(name);
    }

    @Override
    public JSONObject toJSONObjectSingleOb() {
        JSONObject res = new JSONObject();
        res.put("id_city", this.id_city);
        res.put("name", this.name);
        JSONObject res1 = new JSONObject();
        res1.put("City", res);
        return res1;
    }

    @Override
    public JSONObject toJSONObject() {
        JSONObject res = new JSONObject();
        res.put("id_city", this.id_city);
        res.put("name", this.name);
        return res;
    }

    public int getId_city() {
        return id_city;
    }
    public StringBuilder getName() {
        return name;
    }
}
