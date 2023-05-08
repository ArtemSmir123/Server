package objects;

public class Autorit {
    private String login;
    private String pass;
    public Autorit(String login, String pass){
        this.login = login;
        this.pass = pass;
    }

    public String getLogin() {
        return login;
    }
    public String getPass() {
        return pass;
    }
}
