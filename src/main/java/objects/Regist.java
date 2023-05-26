package objects;

import org.json.simple.JSONObject;

import java.util.Date;
import java.util.UUID;

public class Regist implements Objects {
    private Date regTime = new Date();
    private UUID uuid;
    private String login;
    public Regist(UUID uuid, String login){
        this.uuid = uuid;
        this.login = login;
    }

    public Date getRegTime() {
        return (Date) regTime.clone();
    }

    public UUID getUuid() {
        String re = uuid.toString();
        return UUID.fromString(re);
    }

    public String getLogin() {
        return new String (login);
    }

    @Override
    public String toString() {
        return uuid + " " + login + " " + regTime;
    }

    @Override
    public JSONObject toJSONObjectSingleOb() {
        JSONObject result = new JSONObject();
        result.put("regTime", this.getRegTime());
        result.put("uuid", this.getUuid());
        result.put("login", this.getLogin());
        JSONObject result1 = new JSONObject();
        result1.put("Regist", result);
        return result1;
    }
    @Override
    public JSONObject toJSONObject() {
        JSONObject result = new JSONObject();
        result.put("regTime", this.getRegTime().toString());
        result.put("uuid", this.getUuid().toString());
        result.put("login", this.getLogin());
        return result;
    }
}
