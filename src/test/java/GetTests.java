import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class GetTests {
    HttpClient client = HttpClient.newHttpClient();
    JSONParser parser = new JSONParser();

    String socket = "http://localhost:8000/";
    @Test
    public void getCitiesTest() throws IOException, InterruptedException, ParseException {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(socket + "getCities")).GET().build();
        HttpResponse<String> response = null;
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body()); // пишем текст в терминал
//        JSONObject result = (JSONObject) parser.parse(response.body());
    }
}
