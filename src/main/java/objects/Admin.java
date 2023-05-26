package objects;

import org.json.simple.JSONObject;

public class Admin extends Users {
    public Admin(String login, String password, String role, String name, String lastname) {
        super(login, password, role, name, lastname);
    }

    @Override
    public JSONObject toJSONObjectSingleOb() {
        JSONObject result = new JSONObject();
        result.put("login", this.getLogin());
        result.put("password", this.getPassword());
        result.put("role", this.getRole());
        result.put("name", this.getName());
        result.put("lastname", this.getLastname());
        JSONObject result1 = new JSONObject();
        result1.put("Admin", result);
        return result1;
    }
    @Override
    public JSONObject toJSONObject() {
        JSONObject result = new JSONObject();
        result.put("login", this.getLogin());
        result.put("password", this.getPassword());
        result.put("role", this.getRole());
        result.put("name", this.getName());
        result.put("lastname", this.getLastname());
        return result;
    }
}
