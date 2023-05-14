package handlers;

import ClientAPI.API;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import objects.Plane;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.*;

public class UpdatePlaneHandler extends Handler implements HttpHandler {

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
    }
}
