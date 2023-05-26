package handlers;

import ClientAPI.API;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import objects.City;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class GetCitiesHandler extends Handler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
//        InputStream input = exchange.getRequestBody();
//        StringBuilder stringBuilder = new StringBuilder();
//        new BufferedReader(new InputStreamReader(input))
//                .lines()
//                .forEach( (String s) -> stringBuilder.append(s + "\n") );
//        JSONObject query = null;
//        try {
//            query = (JSONObject) parser.parse(String.valueOf(stringBuilder));
//        } catch (org.json.simple.parser.ParseException e) {
//            System.out.println("сломалось");
//        }
        ArrayList<City> listResult;
        JSONArray arrayResult = new JSONArray();
        listResult = API.getCities();
        for (int i = 0; i < listResult.size(); i++){
            JSONObject a = listResult.get(i).toJSONObject();
            arrayResult.add(a);
        }
        JSONObject result = new JSONObject();
        result.put("City", arrayResult);
        result.put("result", true);
        StringBuilder respText = new StringBuilder(result.toJSONString());
        exchange.sendResponseHeaders(200, respText.toString().getBytes().length);
        OutputStream output = exchange.getResponseBody();
        output.write(respText.toString().getBytes());
        output.flush();
        exchange.close();
//        проход по листу запихиваем в JSON

    }
}
