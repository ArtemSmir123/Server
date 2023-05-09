import ClientAPI.API;
import com.sun.net.httpserver.HttpServer;
import objects.*;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.*;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

class App {
    public static void main( String[] args ) throws IOException {
        System.out.println( "Hello World!" );
        int serverPort = 8000;
        regController regController = new regController();
        regController.start();
        JSONParser parser = new JSONParser();
        HttpServer server = HttpServer.create(new InetSocketAddress("localhost", serverPort), 0);
        server.createContext("/findUser", (exchange -> {
            InputStream input = exchange.getRequestBody();
            StringBuilder stringBuilder = new StringBuilder();

            new BufferedReader(new InputStreamReader(input))
                    .lines()
                    .forEach( (String s) -> stringBuilder.append(s + "\n") );

            JSONObject query;
            try {
                query = (JSONObject) parser.parse(String.valueOf(stringBuilder));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            Autorit autorit = new Autorit((String) query.get("login"),(String) query.get("pass"));
            Users user = API.findUser(autorit);
            Regist reg = registration(user.getLogin());

            OutputStream output = exchange.getResponseBody();
            JSONObject object = new JSONObject();
            if (reg != null) {
                System.out.println("Получили запрос на поиск по логину и паролю\t" + query.get("login") + "\t" +
                        "" + exchange.getRemoteAddress());
                object.put("result", "true");
                object.put("login", user.getLogin());
                object.put("pass", user.getPassword());
                object.put("role", user.getRole());
                object.put("name", user.getName());
                object.put("lastname", user.getLastname());

                object.put("regTime", reg.getRegTime().toString());
                object.put("uuid", reg.getUuid().toString());
            } else {
                object.put("result", "false");
            }
            StringBuilder respText = new StringBuilder(object.toJSONString());
            exchange.sendResponseHeaders(200, respText.toString().getBytes().length);
            output.write(respText.toString().getBytes());
            output.flush();
            exchange.close();
        })); // поиск пользователя
        server.createContext("/findLogin", (exchange -> {
            InputStream input = exchange.getRequestBody();
            StringBuilder stringBuilder = new StringBuilder();
            new BufferedReader(new InputStreamReader(input))
                    .lines()
                    .forEach( (String s) -> stringBuilder.append(s + "\n") );
            JSONObject query;
            try {
                query = (JSONObject) parser.parse(String.valueOf(stringBuilder));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            UUID que = UUID.fromString(query.get("uuid").toString());
            boolean a = updateRegistration(que);
            StringBuilder respText;
            JSONObject object = new JSONObject();
                int k = Integer.parseInt(String.valueOf(query.get("query")));
                boolean res = API.findLogin(new StringBuilder(String.valueOf(query.get("query"))));
                System.out.println("Получили запрос на наличие УЗ по логину \t" + k + "\t" +
                        "" + exchange.getRemoteAddress());
                object.put("result", res);
            respText= new StringBuilder(object.toJSONString());
            exchange.sendResponseHeaders(200, String.valueOf(respText).getBytes().length);
            OutputStream output = exchange.getResponseBody();
            output.write(String.valueOf(respText).getBytes());
            output.flush();
            exchange.close();
        })); // проверка на уникальность логина
        server.createContext("/findPlanes", (exchange -> {
            ArrayList<Plane> res = API.findPlanes();
            JSONArray aer = new JSONArray();
            for(int i = 0; i < res.size(); i++){
                JSONObject ss = new JSONObject();
                ss.put("id_plane", res.get(i).getId_plane().toString());
                ss.put("model",res.get(i).getModel());
                ss.put("fullTitle",res.get(i).getFullTitle());
                ss.put("numberOfSeats",res.get(i).getNumberOfSeats().toString());
                aer.add(ss);
            }
            JSONObject mainn= new JSONObject();
            mainn.put("Plane", aer);
            System.out.println("Получили запрос на список самолетов\t \t" +
                    "" + exchange.getRemoteAddress());
            JSONObject object = new JSONObject();
            object.put("Plane", aer);
            StringBuilder respText = new StringBuilder(object.toJSONString());
            exchange.sendResponseHeaders(200, respText.toString().getBytes().length);
            OutputStream output = exchange.getResponseBody();
            output.write(respText.toString().getBytes());
            output.flush();
            exchange.close();
        })); // получение самолетов
        server.createContext("/findPlane", (exchange -> {
            InputStream input = exchange.getRequestBody();
            StringBuilder stringBuilder = new StringBuilder();
            new BufferedReader(new InputStreamReader(input))
                    .lines()
                    .forEach( (String s) -> stringBuilder.append(s + "\n") );
            JSONObject a;
            try {
                 a = (JSONObject) parser.parse(String.valueOf(stringBuilder));
            } catch (ParseException e) {
                System.out.println("fds");
                throw new RuntimeException(e);
            }
            boolean result = API.findPlane(new StringBuilder(a.get("query").toString()));
            System.out.println("Получили запрос на проверку уникальности самолета\t" + a.get("query") + "\t" +
                    "" + exchange.getRemoteAddress());
            JSONObject object = new JSONObject();
            object.put("result", result);
            StringBuilder respText = new StringBuilder(object.toJSONString());
            exchange.sendResponseHeaders(200, respText.toString().getBytes().length);
            OutputStream output = exchange.getResponseBody();
            output.write(respText.toString().getBytes());
            output.flush();
            exchange.close();
        })); // проверка на уникальность самолета
        server.createContext("/deletePlane", (exchange -> {
            InputStream input = exchange.getRequestBody();
            StringBuilder stringBuilder = new StringBuilder();
            new BufferedReader(new InputStreamReader(input))
                    .lines()
                    .forEach( (String s) -> stringBuilder.append(s + "\n") );
            JSONObject a;
            try {
                a = (JSONObject) parser.parse(String.valueOf(stringBuilder));
            } catch (ParseException e) {
                System.out.println("При парсинге возникла проблема");
                throw new RuntimeException(e);
            }
            boolean result = API.deletePlane(Integer.parseInt(a.get("query").toString()));
            System.out.println("Получили запрос на удаление самолета\t" + a.get("query") + "\t" +
                    "" + exchange.getRemoteAddress());
            JSONObject object = new JSONObject();
            object.put("result", result);
            StringBuilder respText = new StringBuilder(object.toJSONString());
            exchange.sendResponseHeaders(200, respText.toString().getBytes().length);
            OutputStream output = exchange.getResponseBody();
            output.write(respText.toString().getBytes());
            output.flush();
            exchange.close();
        })); // удаление самолета
        server.createContext("/savePlane", (exchange -> {
            InputStream input = exchange.getRequestBody();
            StringBuilder stringBuilder = new StringBuilder();
            new BufferedReader(new InputStreamReader(input))
                    .lines()
                    .forEach( (String s) -> stringBuilder.append(s + "\n") );
            JSONObject a;
            try {
                a = (JSONObject) parser.parse(String.valueOf(stringBuilder));
            } catch (ParseException e) {
                System.out.println("При парсинге возникла проблема");
                throw new RuntimeException(e);
            }
            JSONObject b = null;
            try {
                b = (JSONObject) parser.parse(a.get("Plane").toString());
            } catch (ParseException e) {
                System.out.println("При парсинге возникла проблема");
                throw new RuntimeException(e);
            }
            boolean result = API.savePlane(b.get("model").toString(), b.get("fullTitle").toString(), Integer.parseInt(b.get("numberOfSeats").toString()));
            System.out.println("Получили запрос на добавление самолета\t" + b.get("model") + "\t" +
                    "" + exchange.getRemoteAddress());
            JSONObject object = new JSONObject();
            object.put("result", result);
            StringBuilder respText = new StringBuilder(object.toJSONString());
            exchange.sendResponseHeaders(200, respText.toString().getBytes().length);
            OutputStream output = exchange.getResponseBody();
            output.write(respText.toString().getBytes());
            output.flush();
            exchange.close();
        })); // добавление самолета
        server.createContext("/updatePlane", (exchange -> {
            InputStream input = exchange.getRequestBody();
            StringBuilder stringBuilder = new StringBuilder();
            new BufferedReader(new InputStreamReader(input))
                    .lines()
                    .forEach( (String s) -> stringBuilder.append(s + "\n") );
            JSONObject a;
            try {
                a = (JSONObject) parser.parse(String.valueOf(stringBuilder));
            } catch (ParseException e) {
                System.out.println("При парсинге возникла проблема");
                throw new RuntimeException(e);
            }
            JSONObject b = null;
            try {
                b = (JSONObject) parser.parse(a.get("Plane").toString());
            } catch (ParseException e) {
                System.out.println("При парсинге возникла проблема");
                throw new RuntimeException(e);
            }
            Plane plane = new Plane(
                    Integer.parseInt(b.get("id_plane").toString()),
                    b.get("model").toString(),
                    b.get("fullTitle").toString(),
                    Integer.parseInt(b.get("numberOfSeats").toString()));
            boolean result = API.updatePlane(plane);
            System.out.println("Получили запрос на обновление самолета\t" + b.get("model") + "\t" +
                    "" + exchange.getRemoteAddress());
            JSONObject object = new JSONObject();
            object.put("result", result);
            StringBuilder respText = new StringBuilder(object.toJSONString());
            exchange.sendResponseHeaders(200, respText.toString().getBytes().length);
            OutputStream output = exchange.getResponseBody();
            output.write(respText.toString().getBytes());
            output.flush();
            exchange.close();
        })); // обновление самолета
        server.createContext("/saveModer", (exchange -> {
            InputStream input = exchange.getRequestBody();
            StringBuilder stringBuilder = new StringBuilder();
            new BufferedReader(new InputStreamReader(input))
                    .lines()
                    .forEach( (String s) -> stringBuilder.append(s + "\n") );
            JSONObject a;
            try {
                a = (JSONObject) parser.parse(String.valueOf(stringBuilder));
            } catch (ParseException e) {
                System.out.println("При парсинге возникла проблема");
                throw new RuntimeException(e);
            }
            JSONObject b = null;
            try {
                b = (JSONObject) parser.parse(a.get("Moder").toString());
            } catch (ParseException e) {
                System.out.println("При парсинге возникла проблема");
                throw new RuntimeException(e);
            }
            Moder moder = new Moder(
                    b.get("login").toString(),
                    b.get("password").toString(),
                    b.get("role").toString(),
                    b.get("name").toString(),
                    b.get("lastname").toString());
            boolean result = API.saveModer(moder);
            System.out.println("Получили запрос на создание модера\t" + b.get("lastname") + " " + b.get("name") + "\t" +
                    "" + exchange.getRemoteAddress());
            JSONObject object = new JSONObject();
            object.put("result", result);
            StringBuilder respText = new StringBuilder(object.toJSONString());
            exchange.sendResponseHeaders(200, respText.toString().getBytes().length);
            OutputStream output = exchange.getResponseBody();
            output.write(respText.toString().getBytes());
            output.flush();
            exchange.close();
        })); // создание модератора
        server.createContext("/findModers", (exchange -> {
            ArrayList<Moder> res = API.findModers();
            JSONArray aer = new JSONArray();
            for(int i = 0; i < res.size(); i++){
                JSONObject ss = new JSONObject();
                ss.put("login", res.get(i).getLogin());
                ss.put("password",res.get(i).getPassword());
                ss.put("role",res.get(i).getRole());
                ss.put("name",res.get(i).getName());
                ss.put("lastname",res.get(i).getLastname());
                aer.add(ss);
            }
            JSONObject mainn= new JSONObject();
            mainn.put("Moder", aer);
            System.out.println("Получили запрос на список модеров\t \t" +
                    "" + exchange.getRemoteAddress());
            JSONObject object = new JSONObject();
            object.put("Moder", aer);
            StringBuilder respText = new StringBuilder(object.toJSONString());
            exchange.sendResponseHeaders(200, respText.toString().getBytes().length);
            OutputStream output = exchange.getResponseBody();
            output.write(respText.toString().getBytes());
            output.flush();
            exchange.close();
        })); // получение самолетов
        server.createContext("/editModer", (exchange -> {
            InputStream input = exchange.getRequestBody();
            StringBuilder stringBuilder = new StringBuilder();
            new BufferedReader(new InputStreamReader(input))
                    .lines()
                    .forEach( (String s) -> stringBuilder.append(s + "\n") );
            JSONObject a;
            ArrayList<Moder> res = API.findModers();
            JSONArray aer = new JSONArray();
            for(int i = 0; i < res.size(); i++){
                JSONObject ss = new JSONObject();
                ss.put("login", res.get(i).getLogin());
                ss.put("password",res.get(i).getPassword());
                ss.put("role",res.get(i).getRole());
                ss.put("name",res.get(i).getName());
                ss.put("lastname",res.get(i).getLastname());
                aer.add(ss);
            }
            JSONObject mainn= new JSONObject();
            mainn.put("Moder", aer);
            System.out.println("Получили запрос на список модеров\t \t" +
                    "" + exchange.getRemoteAddress());
            JSONObject object = new JSONObject();
            object.put("Moder", aer);
            StringBuilder respText = new StringBuilder(object.toJSONString());
            exchange.sendResponseHeaders(200, respText.toString().getBytes().length);
            OutputStream output = exchange.getResponseBody();
            output.write(respText.toString().getBytes());
            output.flush();
            exchange.close();
        })); // изменение модератора не доделан
//        server.createContext("/test", new  MyHttpHandler()); // создаем контекст через полноценный класс
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(1000);
        server.setExecutor(threadPoolExecutor); // создали пул потоков
        server.start(); // запустили сервер
//        logger.info(" Server started on port 8000"); // логируем информацию

    }
    private static Regist registration(String login){
        Set<String> nums = regController.arrayList.keySet();
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
        regController.arrayList.put(login, asd);
        return asd;
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
//class MyHttpHandler implements HttpHandler {
//
//    @Override
//    public void handle(HttpExchange httpExchange) throws IOException {
//        String requestParamValue=null;
//
//        if("GET".equals(httpExchange.getRequestMethod())) {
//            requestParamValue = handleGetRequest(httpExchange);
//        } else if("POST".equals(httpExchange)) {
//            requestParamValue = handlePostRequest(httpExchange);
//        }
//
//        handleResponse(httpExchange,requestParamValue);
//    }
//
//    private String handleGetRequest(HttpExchange httpExchange) {
//        return httpExchange.
//                getRequestURI()
//                .toString()
//                .split("\\?")[1]
//                .split("=")[1];
//    }
//
//    private void handleResponse(HttpExchange httpExchange, String requestParamValue)  throws  IOException {
//        OutputStream outputStream = httpExchange.getResponseBody();
//        StringBuilder htmlBuilder = new StringBuilder();
//
//        htmlBuilder.append("").
//                append("").
//                append("<h1>").
//                append("Hello ")
//                .append(requestParamValue)
//                .append("</h1>")
//                .append("")
//                .append("");
//
//        // encode HTML content
//        String htmlResponse = StringEscapeUtils.escapeHtml4(htmlBuilder.toString());
//
//        // this line is a must
//        httpExchange.sendResponseHeaders(200, htmlResponse.length());
//
//        outputStream.write(htmlResponse.getBytes());
//        outputStream.flush();
//        outputStream.close();
//    }
//}

