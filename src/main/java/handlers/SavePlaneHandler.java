package handlers;
import ClientAPI.API;
import com.sun.net.httpserver.HttpExchange;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import serv.*;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;

public class SavePlaneHandler extends Handler implements HttpHandler {
    public SavePlaneHandler(RegController regController) {
        super(regController);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
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
    }
}
