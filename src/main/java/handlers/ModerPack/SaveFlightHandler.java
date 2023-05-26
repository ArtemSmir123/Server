package handlers.ModerPack;

import ClientAPI.API;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import handlers.Handler;
import objects.Flight;
import org.json.simple.JSONObject;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SaveFlightHandler extends Handler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        InputStream input = exchange.getRequestBody();
        StringBuilder stringBuilder = new StringBuilder();
        new BufferedReader(new InputStreamReader(input))
                .lines()
                .forEach( (String s) -> stringBuilder.append(s + "\n") );
        JSONObject query = null;
        try {
            query = (JSONObject) parser.parse(String.valueOf(stringBuilder));
        } catch (org.json.simple.parser.ParseException e) {
            System.out.println("сломалось");
        }
        Flight flight;
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
        Calendar cal = Calendar.getInstance();
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        int id_flight = 0;
        int id_plane = 0;
        try {
            cal.setTime(sdf.parse((String) query.get("creation_date")));
            cal1.setTime(sdf.parse((String) query.get("departure_date")));
            cal2.setTime(sdf.parse((String) query.get("arrival_date")));
            id_plane = Integer.parseInt((String) query.get("id_plane"));
        } catch (java.text.ParseException e) {
            System.out.println("!!!");
        }
//        System.out.println("+");
        flight = new Flight(null,
                String.valueOf(query.get("id_user")),
                cal.getTime(),
                cal1.getTime(),
                cal2.getTime(),
                String.valueOf(query.get("departure_city")),
                String.valueOf(query.get("arrival_city")),
                id_plane
        );
//        System.out.println("+");
        boolean b = API.saveFlight(flight);


        JSONObject result = new JSONObject();
        result.put("result", true);
        StringBuilder respText = new StringBuilder(result.toJSONString());
        exchange.sendResponseHeaders(200, respText.toString().getBytes().length);
        OutputStream output = exchange.getResponseBody();
        output.write(respText.toString().getBytes());
        output.flush();
        exchange.close();
    }
}
