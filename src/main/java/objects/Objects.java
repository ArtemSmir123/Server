package objects;

import org.json.simple.JSONObject;

public interface Objects {
    public JSONObject toJSONObjectSingleOb();
    public JSONObject toJSONObject();

    public static Objects fromJSONObject(JSONObject object) {
        System.out.println("нереализованный метод");
        return null;
    }
}