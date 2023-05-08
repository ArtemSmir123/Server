package ClientAPI;

import objects.Autorit;
import objects.Plane;
import objects.Users;
import org.json.simple.JSONObject;

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
}
