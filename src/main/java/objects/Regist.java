package objects;

import org.apache.commons.io.output.StringBuilderWriter;

import java.sql.Time;
import java.util.Date;
import java.util.UUID;

public class Regist {
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
}
