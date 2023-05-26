package handlers.AdminPack;

import ClientAPI.API;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import handlers.Handler;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.*;

public class DeletePlaneHandler extends Handler implements HttpHandler {

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
    }
}
