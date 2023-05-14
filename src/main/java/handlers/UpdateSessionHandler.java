package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.UUID;

public class UpdateSessionHandler extends Handler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        InputStream input = exchange.getRequestBody();
        StringBuilder stringBuilder = new StringBuilder();
        new BufferedReader(new InputStreamReader(input))
                .lines()
                .forEach( (String s) -> stringBuilder.append(s + "\n") );
        JSONObject a = null;
        try {
            a = (JSONObject) parser.parse(String.valueOf(stringBuilder));
        } catch (ParseException e) {
            System.out.println("сломалось");
        }
        UUID uuid = UUID.fromString(String.valueOf(a.get("uuid")));
        boolean b = updateRegistration(uuid);
        JSONObject object = new JSONObject();
        if (b) {
            object.put("result", true);
            System.out.println("Получен запрос на обновление сессии " + a.get("uuid"));
        } else {
            object.put("result", false);
            System.out.println("Получен запрос на обновление пустой сессии " + a.get("uuid"));
        }
        StringBuilder respText = new StringBuilder(object.toJSONString());
        exchange.sendResponseHeaders(200, respText.toString().getBytes().length);
        OutputStream output = exchange.getResponseBody();
        output.write(respText.toString().getBytes());
        output.flush();
        exchange.close();
    }
}
