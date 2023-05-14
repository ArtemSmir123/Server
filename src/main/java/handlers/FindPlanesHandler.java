package handlers;

import ClientAPI.API;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import objects.Plane;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import serv.*;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class FindPlanesHandler extends Handler implements HttpHandler {
    public FindPlanesHandler(RegController regController) {
        super(regController);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        ArrayList<Plane> res = API.findPlanes();
        JSONArray aer = new JSONArray();
        for(int i = 0; i < res.size(); i++){
            JSONObject ss = new JSONObject();
            ss.put("id_plane", res.get(i).getId_plane().toString());
            ss.put("model",res.get(i).getModel());
            ss.put("fullTitle",res.get(i).getFullTitle());
            ss.put("numberOfSeats",res.get(i).getNumberOfSeats().toString());
            aer.add(ss);
        }
        JSONObject mainn= new JSONObject();
        mainn.put("Plane", aer);
        System.out.println("Получили запрос на список самолетов\t \t" +
                "" + exchange.getRemoteAddress());
        JSONObject object = new JSONObject();
        object.put("Plane", aer);
        StringBuilder respText = new StringBuilder(object.toJSONString());
        exchange.sendResponseHeaders(200, respText.toString().getBytes().length);
        OutputStream output = exchange.getResponseBody();
        output.write(respText.toString().getBytes());
        output.flush();
        exchange.close();
    }
}
