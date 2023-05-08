
import objects.Autorit;
import objects.Plane;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;


public class APITest {
    @Test
    public void findUser() throws IOException, InterruptedException {
        Autorit a = new Autorit("12345678", "12345678");
        HttpClient client = HttpClient.newHttpClient();

        JSONObject object = new JSONObject();
        object.put("login", a.getLogin());
        object.put("pass", a.getPass());
        JSONValue.parse(String.valueOf(a));
//        System.out.println(object.toJSONString());



        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8000/findUser")).POST(HttpRequest.BodyPublishers.ofString(object.toJSONString())).build();
        HttpResponse<String> response = null;
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

//        System.out.println(response.body()); // пишем текст в терминал

    }
    @Test
    public void findLoginTest() throws IOException, InterruptedException, ParseException {
        String a = "12345678";
        HttpClient client = HttpClient.newHttpClient();

        JSONObject object = new JSONObject();
        object.put("query", a);
//        System.out.println(object.toJSONString());



        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8000/findLogin")).POST(HttpRequest.BodyPublishers.ofString(object.toJSONString())).build();
        HttpResponse<String> response = null;
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

//        System.out.println(response.body()); // пишем текст в терминал

    }
    @Test
    public void findPlanesTest() throws IOException, InterruptedException, ParseException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8000/findPlanes")).build();
        HttpResponse<String> response = null;
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
//        System.out.println(response.body()); // пишем текст в терминал

        JSONParser parser = new JSONParser();
        JSONObject result = (JSONObject) parser.parse(response.body());
        JSONArray result1 = (JSONArray) result.get("Plane");
        ArrayList<Plane> a = new ArrayList<>();

        for (int i = 0; i < result1.size(); i++){
            JSONObject result3 = (JSONObject) result1.get(i);
            a.add(new Plane(
                    Integer.parseInt(result3.get("id_plane").toString()),
                    String.valueOf(result3.get("model")),
                    String.valueOf(result3.get("fullTitle")),
                    Integer.parseInt(result3.get("numberOfSeats").toString())));
        }
//        System.out.println(a);
    }
}
