import objects.Autorit;
import objects.Flight;
import objects.Moder;
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
import java.text.SimpleDateFormat;
import java.util.*;

public class APITest {
    HttpClient client = HttpClient.newHttpClient();
    JSONParser parser = new JSONParser();

    String socket = "https://6a83-94-140-137-201.ngrok-free.app/";
    @Test
    public void findUserTest() throws IOException, InterruptedException, ParseException {
        Autorit a = new Autorit("12345678", "12345678");

        JSONObject object = new JSONObject();
        object.put("login", a.getLogin());
        object.put("pass", a.getPass());
        JSONValue.parse(String.valueOf(a));
        System.out.println(object.toJSONString());
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(socket + "findUser")).POST(HttpRequest.BodyPublishers.ofString(object.toJSONString())).build();
        HttpResponse<String> response = null;
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body()); // пишем текст в терминал
        JSONObject result = (JSONObject) parser.parse(response.body());
    }
    @Test
    public void findLoginTest() throws IOException, InterruptedException, ParseException {
        String a = "12345678";

        JSONObject object = new JSONObject();
        object.put("query", a);
        System.out.println(object.toJSONString());



        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(socket + "findLogin")).POST(HttpRequest.BodyPublishers.ofString(object.toJSONString())).build();
        HttpResponse<String> response = null;
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body()); // пишем текст в терминал

    }
    @Test
    public void findPlanesTest() throws IOException, InterruptedException, ParseException {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(socket + "findPlanes")).build();
        HttpResponse<String> response = null;
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body()); // пишем текст в терминал


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
    @Test
    public void findPlaneTest() throws ParseException, IOException, InterruptedException {
        StringBuilder query = new StringBuilder("A319ne");
        JSONObject query1 = new JSONObject();
        query1.put("query", query.toString());
        System.out.println(query1);

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(socket + "findPlane")).POST(HttpRequest.BodyPublishers.ofString(query1.toJSONString())).build();
        HttpResponse<String> response = null;
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
    }
    @Test
    public void deletePlaneTest() throws ParseException, IOException, InterruptedException {
        Integer query = 10;
        JSONObject query1 = new JSONObject();
        query1.put("query", query);
        System.out.println(query1);

//        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("socket + "deletePlane")).POST(HttpRequest.BodyPublishers.ofString(query1.toJSONString())).build();
//        HttpResponse<String> response = null;
//        response = client.send(request, HttpResponse.BodyHandlers.ofString());
//        System.out.println(response.body());
    }
    @Test
    public void savePlaneTest() throws ParseException, IOException, InterruptedException {
        Plane query = new Plane(1, "A380-Y", "Airbus A380-Y",15);
        JSONObject query1 = new JSONObject();
        query1.put("id_plane", query.getId_plane().toString());
        query1.put("model", query.getModel());
        query1.put("fullTitle", query.getFullTitle());
        query1.put("numberOfSeats", query.getNumberOfSeats().toString());
        JSONObject query2 = new JSONObject();
        query2.put("Plane", query1);
        System.out.println(query2);

//        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("socket + "savePlane")).POST(HttpRequest.BodyPublishers.ofString(query2.toJSONString())).build();
//        HttpResponse<String> response = null;
//        response = client.send(request, HttpResponse.BodyHandlers.ofString());
//        System.out.println(response.body());
    }
    @Test
    public void updatePlaneTest() throws ParseException, IOException, InterruptedException {
        Plane query = new Plane(61, "A380-Y", "Airbus A380-Y",20);
        JSONObject query1 = new JSONObject();
        query1.put("id_plane", query.getId_plane().toString());
        query1.put("model", query.getModel());
        query1.put("fullTitle", query.getFullTitle());
        query1.put("numberOfSeats", query.getNumberOfSeats().toString());
        JSONObject query2 = new JSONObject();
        query2.put("Plane", query1);
        System.out.println(query2);
//        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(socket + "updatePlane")).POST(HttpRequest.BodyPublishers.ofString(query2.toJSONString())).build();
//        HttpResponse<String> response = null;
//        response = client.send(request, HttpResponse.BodyHandlers.ofString());
//        System.out.println(response.body());
    }
    @Test
    public void saveModerTest() throws ParseException, IOException, InterruptedException {
        Moder query = new Moder("12223344","12223345","moder", "Вася", "Пупкин");
        JSONObject query1 = new JSONObject();
        query1.put("login", query.getLogin());
        query1.put("password", query.getPassword());
        query1.put("role", query.getRole());
        query1.put("name", query.getName());
        query1.put("lastname", query.getLastname());
        JSONObject query2 = new JSONObject();
        query2.put("Moder", query1);
        System.out.println(query2);

//        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(socket + "saveModer")).POST(HttpRequest.BodyPublishers.ofString(query2.toJSONString())).build();
//        HttpResponse<String> response = null;
//        response = client.send(request, HttpResponse.BodyHandlers.ofString());
//        System.out.println(response.body());
    }
    @Test
    public void findModersTest() throws ParseException, IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(socket + "findModers")).build();
        HttpResponse<String> response = null;
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());

        JSONObject result = (JSONObject) parser.parse(response.body());
        JSONArray result1 = (JSONArray) result.get("Moder");
        ArrayList<Moder> a = new ArrayList<>();

        for (int i = 0; i < result1.size(); i++){
            JSONObject result3 = (JSONObject) result1.get(i);
            a.add(new Moder(
                    String.valueOf(result3.get("login")),
                    String.valueOf(result3.get("password")),
                    String.valueOf(result3.get("role")),
                    String.valueOf(result3.get("name")),
                    String.valueOf(result3.get("lastname"))));
        }
        System.out.println(a);
    }
    @Test
    public void testUUID(){
        UUID gen = UUID.randomUUID();
        System.out.println(gen);
        Date curTime = new Date();
        System.out.println(curTime.getTime());
    }
    @Test
//    public void testUU(){
//        Set<String> nums = regController.arrayList.keySet();
//        Iterator<String> iterator = nums.iterator();
//        Regist asd = null;
//        for (int i = 0; i < nums.size(); i++) {
//            if (iterator.next().toString().equals("12345678")) {
//                System.out.println("поймали неуникальный");
//            }
//        }
//
//        UUID reg = UUID.randomUUID();
//        asd = new Regist(reg, "12345678");
//        RegController.arrayList.put("12345678", asd);
//
//        nums = RegController.arrayList.keySet();
//        iterator = nums.iterator();
//        asd = null;
//        for (int i = 0; i < nums.size(); i++) {
//            if (iterator.next().toString().equals("12345678")) {
//                System.out.println("поймали неуникальный");
//            }
//        }
//
//        reg = UUID.randomUUID();
//        asd = new Regist(reg, "12345678");
//        RegController.arrayList.put("12345678", asd);
//    }
//    @Test
    public void editModerTest() throws IOException, InterruptedException {
        JSONObject query = new JSONObject();
        Moder user = new Moder("98415037", "98415037", "moder", "dsal", "ads");
        String name = "dfs";
        String lastname = "dsa";
        query.put("login", user.getLogin());
        query.put("password", user.getPassword());
        query.put("role", user.getRole());
        query.put("name", user.getName());
        query.put("lastname", user.getLastname());
        JSONObject query1 = new JSONObject();
        query1.put("Moder", query);
        query1.put("name", name);
        query1.put("lastname", lastname);

        System.out.println(query1.toJSONString());

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(socket + "editModer")).POST(HttpRequest.BodyPublishers.ofString(query1.toJSONString())).build();
        HttpResponse<String> response = null;
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
    }
    @Test
    public void deleteModerTest() throws IOException, InterruptedException {
        Moder moder = new Moder("12334455","12334455", "moder", "aaa", "aaa");
        JSONObject query = new JSONObject();
        query.put("login", moder.getLogin());
        query.put("password", moder.getPassword());
        query.put("role", moder.getRole());
        query.put("name", moder.getName());
        query.put("lastname", moder.getLastname());
        System.out.println(query);
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(socket + "deleteModer")).POST(HttpRequest.BodyPublishers.ofString(query.toJSONString())).build();
        HttpResponse<String> response = null;
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
    }
    @Test
    public void findDefiniteModersTest() throws IOException, InterruptedException, ParseException {
        JSONObject query = new JSONObject();
        query.put("queryString", "Кло Дан");
        System.out.println(query);
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(socket + "findDefiniteModers")).POST(HttpRequest.BodyPublishers.ofString(query.toJSONString())).build();
        HttpResponse<String> response = null;
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        JSONObject array = (JSONObject) parser.parse(response.body());
    }

    @Test
    public void saveFlightTest() throws IOException, InterruptedException, ParseException, java.text.ParseException {
        Calendar ad = new GregorianCalendar();
        ad.set(2023, Calendar.JANUARY, 29, 19, 15, 14);
        Calendar ad1 = new GregorianCalendar();
        ad1.set(2023, Calendar.MARCH, 3, 12, 12, 0);
        Calendar ad2 = new GregorianCalendar();
        ad2.set(2023, Calendar.MARCH, 4, 0, 24, 0);
//        System.out.println(ad.getTime().toString());
//        System.out.println(ad1.getTime().toString());
//        System.out.println(ad2.getTime().toString());
        Flight flight = new Flight(null,
                "87654321",
                ad.getTime(),
                ad1.getTime(),
                ad2.getTime(),
                "Moscow",
                "Yekaterinburg",
                15);

        JSONObject query = new JSONObject();
        query.put("id_flight", "null");
        query.put("id_user", flight.getId_user());
        query.put("creation_date", flight.getCreation_date().toString());
        query.put("departure_date", flight.getDeparture_date().toString());
        query.put("arrival_date", flight.getArrival_date().toString());
        query.put("departure_city", flight.getDeparture_city());
        query.put("arrival_city", flight.getArrival_city());
        query.put("id_plane", String.valueOf(flight.getId_plane()));
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
        cal.setTime(sdf.parse("Sun Jan 29 19:15:14 YEKT 2023"));// all done
        System.out.println(cal.getTime());
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(socket + "saveFlight")).POST(HttpRequest.BodyPublishers.ofString(query.toJSONString())).build();
        HttpResponse<String> response = null;
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        JSONObject array = (JSONObject) parser.parse(response.body());
    }
}
