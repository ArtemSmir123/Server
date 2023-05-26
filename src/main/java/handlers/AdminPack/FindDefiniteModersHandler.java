package handlers.AdminPack;

import ClientAPI.API;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import handlers.Handler;
import objects.Moder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;

public class FindDefiniteModersHandler extends Handler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        InputStream input = exchange.getRequestBody();
        StringBuilder stringBuilder = new StringBuilder();
        new BufferedReader(new InputStreamReader(input))
                .lines()
                .forEach( (String s) -> stringBuilder.append(s + "\n") );

        JSONObject query;
        try {
            query = (JSONObject) parser.parse(stringBuilder.toString());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        ArrayList<Moder> res = API.findDefiniteModers((String) query.get("queryString"));
        JSONArray aer = new JSONArray();
        for(int i = 0; i < res.size(); i++){
            JSONObject ss = new JSONObject();
            ss.put("login", res.get(i).getLogin());
            ss.put("password",res.get(i).getPassword());
            ss.put("role",res.get(i).getRole());
            ss.put("name",res.get(i).getName());
            ss.put("lastname",res.get(i).getLastname());
            aer.add(ss);
        }

        JSONObject object= new JSONObject();
        object.put("Moder", aer);
        System.out.println("Получили запрос поиск модераторов\t \t" +
                "" + exchange.getRemoteAddress());

        StringBuilder respText = new StringBuilder(object.toJSONString());
        exchange.sendResponseHeaders(200, respText.toString().getBytes().length);
        OutputStream output = exchange.getResponseBody();
        output.write(respText.toString().getBytes());
        output.flush();
        exchange.close();
    }
}
