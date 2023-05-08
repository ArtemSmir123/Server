import ClientAPI.API;
import com.sun.net.httpserver.HttpServer;
import objects.Autorit;
import objects.Plane;
import objects.Users;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.*;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

class App {
    public static void main( String[] args ) throws IOException {
        System.out.println( "Hello World!" );
        int serverPort = 8000;
        HttpServer server = HttpServer.create(new InetSocketAddress("localhost", serverPort), 0);
        server.createContext("/finddUser", (exchange -> {
            InputStream input = exchange.getRequestBody();
            StringBuilder stringBuilder = new StringBuilder();

            new BufferedReader(new InputStreamReader(input))
                    .lines()
                    .forEach( (String s) -> stringBuilder.append(s + "\n") );

            JSONParser parser = new JSONParser();
            JSONObject query;
            try {
                query = (JSONObject) parser.parse(String.valueOf(stringBuilder));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            Autorit autorit = new Autorit((String) query.get("login"),(String) query.get("pass"));
            Users user = API.findUser(autorit);

            JSONObject object = new JSONObject();
            object.put("login", user.getLogin());
            object.put("pass", user.getPassword());
            object.put("role", user.getRole());
            object.put("name", user.getName());
            object.put("lastname", user.getLastname());

            OutputStream output = exchange.getResponseBody();
            StringBuilder htmlBuilder = new StringBuilder();
            htmlBuilder.append(object.toJSONString());
            String res = htmlBuilder.toString();
//            stringBuilder.delete(0, stringBuilder.length() - 1);
            stringBuilder.append(object.toJSONString());
//            OutputStreamWriter writer = new OutputStreamWriter(output);
//            writer.write(object.toString());
            exchange.sendResponseHeaders(200, stringBuilder.toString().length());
            output.write(stringBuilder.toString().getBytes());
//            exchange.sendResponseHeaders(200, writer.toString().length());
//            output.write(writer.toString().getBytes());
//            exchange.sendResponseHeaders(200, res.length());
//            output.write(res.getBytes());


            output.flush();
            exchange.close();
        })); // создаем контекст с помощью лямбды
        server.createContext("/findUser", (exchange -> {
            InputStream input = exchange.getRequestBody();
            StringBuilder stringBuilder = new StringBuilder();

            new BufferedReader(new InputStreamReader(input))
                    .lines()
                    .forEach( (String s) -> stringBuilder.append(s + "\n") );

            JSONParser parser = new JSONParser();
            JSONObject query;
            try {
                query = (JSONObject) parser.parse(String.valueOf(stringBuilder));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            Autorit autorit = new Autorit((String) query.get("login"),(String) query.get("pass"));
            Users user = API.findUser(autorit);
            System.out.println("Получили запрос на поиск по логину и паролю\t" + query.get("login") + "\t" +
                    "" + exchange.getRemoteAddress());
            JSONObject object = new JSONObject();
            object.put("login", user.getLogin());
            object.put("pass", user.getPassword());
            object.put("role", user.getRole());
            object.put("name", user.getName());
            object.put("lastname", user.getLastname());

            String respText = new String(object.toJSONString());
            exchange.sendResponseHeaders(200, respText.toString().getBytes().length);
            OutputStream output = exchange.getResponseBody();
            output.write(respText.toString().getBytes());
            output.flush();
            exchange.close();
        })); // создаем контекст с помощью лямбды
        server.createContext("/findLogin", (exchange -> {
            InputStream input = exchange.getRequestBody();
            StringBuilder stringBuilder = new StringBuilder();

            new BufferedReader(new InputStreamReader(input))
                    .lines()
                    .forEach( (String s) -> stringBuilder.append(s + "\n") );
            JSONParser parser = new JSONParser();
            JSONObject query;
            try {
                query = (JSONObject) parser.parse(String.valueOf(stringBuilder));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            int k = Integer.parseInt(String.valueOf(query.get("query")));
            boolean res = API.findLogin(k);
            System.out.println("Получили запрос на наличие УЗ по логину \t" + k + "\t" +
                    "" + exchange.getRemoteAddress());
            JSONObject object = new JSONObject();
            object.put("result", res);


            String respText = new String(object.toJSONString());
            exchange.sendResponseHeaders(200, respText.getBytes().length);
            OutputStream output = exchange.getResponseBody();
            output.write(respText.toString().getBytes());
            output.flush();
            exchange.close();
        })); // создаем контекст с помощью лямбды
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
            String respText = new String(object.toJSONString());
            exchange.sendResponseHeaders(200, respText.getBytes().length);
            OutputStream output = exchange.getResponseBody();
            output.write(respText.getBytes());
            output.flush();
            exchange.close();
        })); // создаем контекст с помощью лямбды
//        server.createContext("/test", new  MyHttpHandler()); // создаем контекст через полноценный класс
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(1000);
        server.setExecutor(threadPoolExecutor); // создали пул потоков
        server.start(); // запустили сервер
//        logger.info(" Server started on port 8001"); // логируем информацию

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

