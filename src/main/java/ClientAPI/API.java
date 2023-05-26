package ClientAPI;

import objects.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class API {
    public static Users findUser(Autorit query){
        return SQL.findUser(query);
    } // ищет пользователя по логину и паролю
    public static boolean findLogin(StringBuilder query){
        return SQL.findLogin(query);
    } // ищет наличие пользователя в системе
    public static ArrayList<Plane> findPlanes(){
        return SQL.findPlanes();
    } // запрашивает список самолетов
    public static boolean findPlane(StringBuilder model){
        return SQL.findPlane(model);
    } // проверяет уникальность самолета, если нашли то true
    public static boolean deletePlane(Integer id){
        return SQL.deletePlane(id);
    } // удалить самолет
    public static boolean savePlane(String plane1, String plane2, int plane3){
        return SQL.savePlane(plane1, plane2, plane3);
    } // сохранить самолет в БД
    public static boolean updatePlane(@NotNull Plane plane){
        return SQL.updatePlane(plane);
    } // редактирование самолета в БД
    public static boolean saveModer(Moder moder){
        return SQL.saveModer(moder);
    } // сохранение модератора
    public static ArrayList<Moder> findModers(){
        return SQL.findModers();
    } // ищем модеров для выгрузки
    public static boolean editModer(Moder user, String name, String lastname) {
        return SQL.editModerQuery(user, name, lastname);
    } // редактирование модератора
    public static boolean deleteModer(Moder user){
        return SQL.deleteModer(user);
    }
    public static ArrayList<Moder> findDefiniteModers(String searchField){
        return SQL.findDefiniteModers(searchField);
    }
    public static boolean saveFlight(Flight flight){
        return SQL.saveFlight(flight);
    }

    public static ArrayList<City> getCities(){
        return SQL.getCities();
    } // Получить города
}
