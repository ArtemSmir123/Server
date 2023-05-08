package objects;

import org.json.simple.JSONObject;

public class Admin extends Users {
    public Admin(String login, String password, String role, String name, String lastname) {
        super(login, password, role, name, lastname);
    }

}
