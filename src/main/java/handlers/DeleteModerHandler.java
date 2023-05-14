package handlers;

import ClientAPI.API;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import objects.Moder;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import serv.*;

import java.io.*;

public class DeleteModerHandler extends Handler implements HttpHandler {
    public DeleteModerHandler(RegController regController) {
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
        boolean result = API.deleteModer(new Moder(String.valueOf(a.get("login")),String.valueOf(a.get("password")),String.valueOf(a.get("role")),String.valueOf(a.get("name")),String.valueOf(a.get("lastname"))));
        System.out.println("Получили запрос на удаление модератора\t" + a.get("login") + "\t" +
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
