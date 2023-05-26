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
    protected static boolean deleteModer(Moder user){
        connect();
        int v = 0;
        try{
            v = stmt.executeUpdate("DELETE FROM public.\"Users\" WHERE login = '"+ user.getLogin().toString() + "'");
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return v == 1;
    } // удалить модера
    protected static ArrayList<Moder> findDefiniteModers(String searchField){
        connect();
        ResultSet s;
        ArrayList<Moder> result = new ArrayList<>();
        String searchField1[];

        searchField = searchField.toLowerCase();
        searchField1 = searchField.split(" ");
//        System.out.println(searchField1.length);
        if(searchField1.length > 2) throw new RuntimeException();
            // модуль преобразования строки
        else if (searchField1.length == 2) {
            try {

                if(isDight(searchField)) {
                    s = stmt.executeQuery("SELECT *\n" + "FROM public.\"Users\"\n" + "WHERE role = 'moder' AND ((login LIKE '%"+ searchField +"%') OR (login LIKE '%" + searchField + "') OR (login LIKE '" + searchField + "%'))"); // поиск по логину
                } else {
                    s = stmt.executeQuery("SELECT * \n" +
                            "FROM public.\"Users\"\n" +
                            "WHERE role = 'moder' AND (\n" +
                            "\t(\n" +
                            "\t\t((name COLLATE \"ru_RU\") ILIKE '%" + searchField1[0] + "%') AND\n" +
                            "\t\t((lastname COLLATE \"ru_RU\") ILIKE '%" + searchField1[1] + "%') \n" +
                            "\t) or (\n" +
                            "\t\t((name COLLATE \"ru_RU\") ILIKE '%" + searchField1[1] + "%') AND\n" +
                            "\t\t((lastname COLLATE \"ru_RU\") ILIKE '%" + searchField1[0] + "%') \n" +
                            "\t) or (\n" +
                            "\t\t((name COLLATE \"ru_RU\") ILIKE '%" + searchField1[0] + "%')\n" +
                            "\t) or (\n" +
                            "\t\t((name COLLATE \"ru_RU\") ILIKE '%" + searchField1[1] + "%')\n" +
                            "\t) or (\n" +
                            "\t\t((lastname COLLATE \"ru_RU\") ILIKE '%" + searchField1[0] + "%') \n" +
                            "\t) or (\n" +
                            "\t\t((lastname COLLATE \"ru_RU\") ILIKE '%" + searchField1[1] + "%') \n" +
                            "\t)\n" +
                            ")\n ORDER BY CASE \n" +
                            "WHEN (\n" +
                            "\t((name COLLATE \"ru_RU\") ILIKE '%" + searchField1[0] + "%') AND\n" +
                            "\t((lastname COLLATE \"ru_RU\") ILIKE '%" + searchField1[1] + "%')\n" +
                            ") THEN 1\n" +
                            "WHEN (((name COLLATE \"ru_RU\") ILIKE '%" + searchField1[1] + "%') AND\n" +
                            "\t((lastname COLLATE \"ru_RU\") ILIKE '%" + searchField1[0] + "%')\n" +
                            ") THEN 1 \n" +
                            "ELSE 2\n" +
                            "END"); // поиск по параметрам
                }
                connection.close();
                while (s.next()) {
                    result.add(new Moder(s.getString("login"), s.getString("password"), s.getString("role"), s.getString("name"), s.getString("lastname")));
                }
            } catch (SQLException e) {
            }
        }
        else {
            try {

                if(isDight(searchField)) {
                    s = stmt.executeQuery("SELECT *\n" + "FROM public.\"Users\"\n" + "WHERE role = 'moder' AND ((login LIKE '%"+ searchField +"%') OR (login LIKE '%" + searchField + "') OR (login LIKE '" + searchField + "%'))"); // поиск по логину
                } else {
                    s = stmt.executeQuery("SELECT * \n" +
                            "FROM public.\"Users\"\n" +
                            "WHERE role = 'moder' AND (\n" +
                            "\t(\n" +
                            "\t\t((name COLLATE \"ru_RU\") ILIKE '%" + searchField1[0] + "%')\n" +
                            "\t) OR \n" +
                            "\t(\n" +
                            "\t\t((lastname COLLATE \"ru_RU\") ILIKE '%" + searchField1[0] + "%') \n" +
                            "\t)\n" +
                            ")\n"); // поиск по параметрам
                }
                connection.close();
                while (s.next()) {
                    result.add(new Moder(s.getString("login"), s.getString("password"), s.getString("role"), s.getString("name"), s.getString("lastname")));
                }
            } catch (SQLException e) {
            }
        }
        return result;
    } // ищем определенного модера для выгрузки
    private static boolean isDight(String s){
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    } // проверка на число
    protected static boolean saveFlight(Flight flight){
        connect();
        int v = 0;
        try {
            v = stmt.executeUpdate("INSERT INTO public.\"Flights\" (id_user, creation_date, departure_date, arrival_date, departure_city, arrival_city, id_plane)\n" +
                    "VALUES ('" + flight.getId_user() + "', '" + flight.getCreation_date() + "', '" + flight.getDeparture_date() + "','" + flight.getArrival_date() + "', '" + flight.getDeparture_city()+ "', '" + flight.getArrival_city() + "', '" + flight.getId_plane() + "')");
            disconnect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return v == 1;
    }
    protected static ArrayList<City> getCities(){
        ResultSet resultQuery;
        ArrayList<City> result = new ArrayList<>();
        try {
            connect();
            resultQuery = stmt.executeQuery("SELECT * FROM \"glob\".\"Cities\"\n");
            disconnect();
            while (resultQuery.next()) {
                result.add(new City(Integer.parseInt(resultQuery.getString("id_city")), resultQuery.getString("name")));
            }

        } catch (SQLException ignored) {
        }
        return result;
    }
}
