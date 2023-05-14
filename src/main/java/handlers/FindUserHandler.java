package handlers;

import ClientAPI.API;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
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
                .forEach( (String s) -> stringBuilder.append(s + "\n") );

        JSONObject query;
        try {
            query = (JSONObject) parser.parse(String.valueOf(stringBuilder));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        Autorit autorit = new Autorit((String) query.get("login"),(String) query.get("pass"));
        Users user = API.findUser(autorit);
        Regist reg = registration(user.getLogin());

        OutputStream output = exchange.getResponseBody();
        JSONObject object = new JSONObject();
        if (reg != null) {
            System.out.println("Получили запрос на поиск по логину и паролю\t" + query.get("login") + "\t" +
                    "" + exchange.getRemoteAddress());
            object.put("result", "true");
            object.put("login", user.getLogin());
            object.put("pass", user.getPassword());
            object.put("role", user.getRole());
            object.put("name", user.getName());
            object.put("lastname", user.getLastname());
            object.put("regTime", reg.getRegTime().toString());
            object.put("uuid", reg.getUuid().toString());
        } else {
            object.put("result", "false");
        }
        StringBuilder respText = new StringBuilder(object.toJSONString());
        exchange.sendResponseHeaders(200, respText.toString().getBytes().length);
        output.write(respText.toString().getBytes());
        output.flush();
        exchange.close();
    }
}
