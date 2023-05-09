package ClientAPI;

import objects.*;
import org.jetbrains.annotations.NotNull;

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
    protected static boolean findLogin(StringBuilder login){
        connect();
        ResultSet s;
        try {
            s = stmt.executeQuery("SELECT * FROM public.\"Users\" WHERE login = '" + login + "'");

            if (s.next()) return true;
        } catch (SQLException e) {
            return false;
        }
        return false;
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
    protected static boolean findPlane(StringBuilder model){
        connect();
        ResultSet s;
        try {
            s = stmt.executeQuery("SELECT model FROM public.\"Planes\" WHERE model = '" + model + "'"); // переделать
            disconnect();
            s.next();
            if (s.getRow() != 0) return false;
            else return true;
        } catch (SQLException ex){
            return true;
        }
    } // найти самолет, если нашли то true
    protected static boolean deletePlane(Integer id){
        connect();
        int v = 0;
        try {
            v = stmt.executeUpdate("DELETE FROM public.\"Planes\" WHERE id_planes = " + id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return v == 1;
    } // удалить самолет
    protected static boolean savePlane(String plane1, String plane2, int plane3) {
        connect();
        int v = 0;
        try {
            v = stmt.executeUpdate("INSERT INTO public.\"Planes\" (model,fullTitle, numberOfSeats)\n" +
                    "VALUES ('" + plane1 + "', '" + plane2 + "', " + plane3 + ")");
            disconnect();
        } catch (SQLException e) {
            System.out.println("хрень");
            throw new RuntimeException(e);
        }
        return v == 1;
    } // сохранить самолет в БД
    protected static boolean updatePlane(@NotNull Plane plane){
        connect();
        int v = 0;
        try {
            v = stmt.executeUpdate("UPDATE public.\"Planes\"" + " SET model = '" + plane.getModel() +  "', fulltitle = '" + plane.getFullTitle() +  "', numberofseats = " + plane.getNumberOfSeats() +  " WHERE id_planes = " + plane.getId_plane());
            disconnect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return v == 1;
    } // редактирование самолета в БД

    protected static boolean saveModer(Moder moder){
        connect();
        int v = 0;
        try {
            v = stmt.executeUpdate("INSERT INTO public.\"Users\"" + " VALUES ( '" + moder.getLogin() +  "', '" + moder.getPassword() + "', '" + moder.getRole() + "', '" + moder.getName() + "' , '" + moder.getLastname() + "')");
            disconnect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return v == 1;
    } // сохранение модератора
    protected static ArrayList<Moder> findModers(){
        connect();
        ResultSet s;
        ArrayList<Moder> result = new ArrayList<>();
        try {
            s = stmt.executeQuery("SELECT * FROM public.\"Users\" WHERE role = 'moder'" );
            connection.close();
            while (s.next()) {
                result.add(new Moder(s.getString("login"), s.getString("password"), s.getString("role"), s.getString("name"), s.getString("lastname")));
            }
        } catch (SQLException e) {
        }
        return result;
    } // ищем модеров для выгрузки
    protected static boolean editModerQuery(Moder user, String name, String lastname) {
        connect();
        int v = 0;
        try {
            v = stmt.executeUpdate("UPDATE public.\"Users\"" + " SET name = '" + name + "', lastname = '" + lastname + "' WHERE login = '" + user.getLogin() + "'");
            disconnect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return v == 1;
    } // редактирование модератора
}
