package serv;

import com.sun.net.httpserver.HttpServer;
import handlers.*;
import objects.Regist;
import org.jetbrains.annotations.NotNull;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class App {
    public static RegController regController = new RegController();
    public static void main( String[] args ) throws IOException {
        System.out.println( "Hello World!" );
        int serverPort = 8000;

        regController.start();
        JSONParser parser = new JSONParser();
        HttpServer server = HttpServer.create(new InetSocketAddress("localhost", serverPort), 0);


        server.createContext("/findUser", new FindUserHandler(regController)); // поиск пользователя
        server.createContext("/findLogin", new FindLoginHandler(regController)); // проверка на уникальность логина
        server.createContext("/findPlanes", new FindPlanesHandler(regController)); // получение самолетов
        server.createContext("/findPlane", new FindPlaneHandler(regController)); // проверка на уникальность самолета
        server.createContext("/deletePlane", new DeletePlaneHandler(regController)); // удаление самолета
        server.createContext("/savePlane", new SavePlaneHandler(regController)); // добавление самолета
        server.createContext("/updatePlane", new UpdatePlaneHandler(regController)); // обновление самолета
        server.createContext("/saveModer", new SaveModerHandler(regController)); // создание модератора
        server.createContext("/findModers", new FindModersHandler(regController)); // получение самолетов
        server.createContext("/editModer", new EditModerHandler(regController)); // изменение модератора
        server.createContext("/updateSession", new UpdateSessionHandler(regController)); // изменение модератора
        server.createContext("/deleteModer", new DeleteModerHandler(regController)); // удаление самолета

        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(1000);
        server.setExecutor(threadPoolExecutor); // создали пул потоков
        server.start(); // запустили сервер
//        logger.info(" Server started on port 8000"); // логируем информацию

    }
    private static boolean updateRegistration(@NotNull UUID uuid){
        Set<String> nums = regController.arrayList.keySet();
        Iterator<String> iterator = nums.iterator();
        Regist asd = null;
        for (int i = 0; i < nums.size(); i++) {
            Regist regis = regController.arrayList.get(iterator.next());
            if(regis.getUuid().equals(uuid)){
                UUID reg = uuid;
                asd = new Regist(reg, regis.getLogin());
                regController.arrayList.put(regis.getLogin(), asd);
                return true;
            }
        }
        return false;
    }
}


