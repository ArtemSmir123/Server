package ClientAPI;

import objects.*;

import java.sql.*;
import java.util.ArrayList;

class SQL {
    private static Connection connection;
    private static Statement stmt;
    private static final String USER = "postgres";
    private static final String PASS = "1234";
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/Flights?currentSchema=public&user=" + USER + "&password=" + PASS ;

    private static void connect(){
        try {
            connection = DriverManager.getConnection(DB_URL);
            stmt = connection.createStatement();
        } catch (SQLException e) {
            System.out.println("Подключение не удалось");
        }
    } // коннект
    private static void disconnect(){
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    } // дисконнект

    protected static Users findUser(Autorit autorit){
        SQL.connect();
        ResultSet s;
        try {
            s = stmt.executeQuery("SELECT * FROM public.\"Users\" WHERE login = '" + autorit.getLogin() + "' AND password = '" + autorit.getPass() + "'");
            s.next();
            disconnect();
            if (s.getString("role").equals("moder"))
                return new Moder(s.getString("login"), s.getString("password"), s.getString("role"), s.getString("name"), s.getString("lastname"));
            else if (s.getString("role").equals("admin"))
                return new Admin(s.getString("login"), s.getString("password"), s.getString("role"), s.getString("name"), s.getString("lastname"));
            else return null;
        } catch (SQLException ex){
            return null;
        }
    } // найти пользователя для login
    protected static boolean findLogin(int login){
        connect();
        ResultSet s;
        try {
            s = stmt.executeQuery("SELECT login FROM public.\"Users\" WHERE login = '" + String.valueOf(login)+ "'");

            if (s.next()) return true;
            else {
                return false;
            }
        } catch (SQLException e) {
            return false;
        }
    }
    protected static ArrayList<Plane> findPlanes(){
        connect();
        ResultSet s;
        ArrayList<Plane> result = new ArrayList<>();
        try {
            s = stmt.executeQuery("SELECT * FROM public.\"Planes\"" );
            connection.close();
            while (s.next()) {
                result.add(new Plane(s.getInt(1), s.getString("model"), s.getString("fullTitle"), s.getInt("numberOfSeats")));
            }
        } catch (SQLException e) {
        }
        return result;
    } // ищем самолеты для выгрузки
}
