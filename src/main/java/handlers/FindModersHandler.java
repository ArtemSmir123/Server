package handlers;

import ClientAPI.API;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import objects.Moder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import serv.*;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class FindModersHandler extends Handler implements HttpHandler {
    public FindModersHandler(RegController regController) {
        super(regController);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        ArrayList<Moder> res = API.findModers();
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

        JSONObject mainn= new JSONObject();
        mainn.put("Moder", aer);
        System.out.println("Получили запрос на список модеров\t \t" +
                "" + exchange.getRemoteAddress());
        JSONObject object = new JSONObject();
        object.put("Moder", aer);
        StringBuilder respText = new StringBuilder(object.toJSONString());
        exchange.sendResponseHeaders(200, respText.toString().getBytes().length);
        OutputStream output = exchange.getResponseBody();
        output.write(respText.toString().getBytes());
        output.flush();
        exchange.close();
    }
}
