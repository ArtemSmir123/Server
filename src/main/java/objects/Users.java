package objects;
public abstract class Users {
    private final String login;
    private String password;
    private final String role;
    private String name;
    private String lastname;
    protected Users(String login, String password, String role, String name, String lastname){
        this.login = login;
        this.password = password;
        this.role = role;
        this.name = name;
        this.lastname = lastname;
    }

    public String getLogin() {
        return login;
    }
    public String getLastname() {
        return lastname;
    }
    public String getName() {
        return name;
    }
    public String getPassword() {
        return password;
    }
    public String getRole() {
        return role;
    }
    @Override
    public String toString(){
        return "Логин:" + this.login + ", \nимя: " + this.name + ", фамилия: " + this.lastname;
    }
}


