package handlers.IdentAutentPack;

import ClientAPI.API;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import handlers.Handler;
import objects.Autorit;
import objects.Regist;
import objects.Users;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.*;

public class FindUserHandler extends Handler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        InputStream input = exchange.getRequestBody();
        StringBuilder stringBuilder = new StringBuilder();

        new BufferedReader(new InputStreamReader(input))
                .lines()
                .forEach((String s) -> stringBuilder.append(s + "\n"));

        JSONObject query;
        try {
            query = (JSONObject) parser.parse(String.valueOf(stringBuilder));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        JSONObject query1 = (JSONObject) query.get("Autorit");
        Autorit autorit = new Autorit((String) query1.get("login"), (String) query1.get("pass"));
        Users user = API.findUser(autorit);
        OutputStream output = exchange.getResponseBody();
        JSONObject object = new JSONObject();
        if (user != null) {
            Regist reg = registration(user.getLogin());
            if (reg != null) {
                System.out.println("Получили запрос на поиск по логину и паролю\t" + query1.get("login") + "\t" +
                        "" + exchange.getRemoteAddress());
                object.put("result", "true");
                object.put("Regist", reg.toJSONObject());
                if (user.getRole().equals("moder")) {
                    object.put("Moder", user.toJSONObject());
                } else if (user.getRole().equals("admin")) {
                    object.put("Admin", user.toJSONObject());
                }
            } else {
                object.put("result", "false");
            }

        } else{
            object.put("result", "false");
        }
        StringBuilder respText = new StringBuilder(object.toJSONString());
        exchange.sendResponseHeaders(200, respText.toString().getBytes().length);
        output.write(respText.toString().getBytes());
        output.flush();
        exchange.close();
    }
}
