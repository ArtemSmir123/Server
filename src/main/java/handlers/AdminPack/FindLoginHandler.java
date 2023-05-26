package handlers.AdminPack;

import ClientAPI.API;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import handlers.Handler;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.UUID;

public class FindLoginHandler extends Handler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
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
    }
}
