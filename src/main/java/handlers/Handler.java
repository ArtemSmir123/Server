package handlers;

import objects.Regist;
import org.jetbrains.annotations.NotNull;
import org.json.simple.parser.JSONParser;
import serv.*;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

public abstract class Handler {
    JSONParser parser = new JSONParser();
     protected static Regist registration(String login){
        Set<String> nums = RegController.arrayList.keySet();
        Iterator<String> iterator = nums.iterator();
        Regist asd = null;
        for (int i = 0; i < nums.size(); i++) {
            if (iterator.next().toString().equals(login)) {
                System.out.println("поймали неуникальный");
                return null;
            }
        }
        UUID reg = UUID.randomUUID();
        asd = new Regist(reg, login);
        RegController.arrayList.put(login, asd);
        return asd;
    }
    protected static boolean updateRegistration(@NotNull UUID uuid){
        Set<String> nums = RegController.arrayList.keySet();
        Iterator<String> iterator = nums.iterator();
        Regist asd = null;
        for (int i = 0; i < nums.size(); i++) {
            Regist regis = RegController.arrayList.get(iterator.next());
            if(regis.getUuid().equals(uuid)){
                UUID reg = uuid;
                asd = new Regist(reg, regis.getLogin());
                RegController.arrayList.put(regis.getLogin(), asd);
                return true;
            }
        }
        return false;
    }
}
