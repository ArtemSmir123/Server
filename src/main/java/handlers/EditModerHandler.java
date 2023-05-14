package handlers;

import ClientAPI.API;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import objects.Moder;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.*;

public class EditModerHandler extends Handler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        InputStream input = exchange.getRequestBody();
        StringBuilder stringBuilder = new StringBuilder();
        new BufferedReader(new InputStreamReader(input))
                .lines()
                .forEach( (String s) -> stringBuilder.append(s + "\n") );
        JSONObject a;
        JSONObject moder;
        try {
            a = (JSONObject) parser.parse(stringBuilder.toString());
            moder = (JSONObject) parser.parse(a.get("Moder").toString());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        System.out.println(a);
        System.out.println(moder);
        Moder moder1 = new Moder(moder.get("login").toString(), moder.get("password").toString(), moder.get("role").toString(), moder.get("name").toString(), moder.get("lastname").toString());
        boolean b = API.editModer(moder1, a.get("name").toString(), a.get("lastname").toString());

        System.out.println("Получили запрос на обновление модерора\t \t" +
                "" + exchange.getRemoteAddress());
        JSONObject object = new JSONObject();
        if(b) {
            object.put("result", true);
        } else {
            object.put("result", false);
        }
        StringBuilder respText = new StringBuilder(object.toJSONString());
        exchange.sendResponseHeaders(200, respText.toString().getBytes().length);
        OutputStream output = exchange.getResponseBody();
        output.write(respText.toString().getBytes());
        output.flush();
        exchange.close();
    }
}
