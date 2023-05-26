package handlers.AdminPack;

import ClientAPI.API;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import handlers.Handler;
import objects.Moder;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class SaveModerHandler extends Handler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        InputStream input = exchange.getRequestBody();
        StringBuilder stringBuilder = new StringBuilder();
        new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8))
                .lines()
                .forEach( (String s) -> stringBuilder.append(s + "\n") );
        JSONObject a;
        System.out.println(String.valueOf(stringBuilder));
        try {
            a = (JSONObject) parser.parse(String.valueOf(stringBuilder));
        } catch (ParseException e) {
            System.out.println("При парсинге возникла проблема 1");
            throw new RuntimeException(e);
        }
        JSONObject b = null;
        try {
            b = (JSONObject) parser.parse(a.get("Moder").toString());
        } catch (ParseException e) {
            System.out.println("При парсинге возникла проблема 2");
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
    }
}
