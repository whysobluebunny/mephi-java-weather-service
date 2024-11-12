package ru.mephi.abondarenko;

import static io.restassured.RestAssured.given;

public class WeatherServiceTask {
    public static void main(String[] args) {
        int limit = 7; // not less than 1
        String url = "https://api.weather.yandex.ru/v2/forecast";
        var body = given()
                .queryParam("lon", "37.139203")
                .queryParam("lat", "55.813746")
                .queryParam("limit", limit)
                .header("X-Yandex-Weather-Key", System.getenv("API_KEY"))
                .get(url).then().statusCode(200).extract().body();
        var temperatures = body.jsonPath().getList("forecasts.parts.collect {it.values()*.temp_avg}.flatten()");
        // average is calculated using temp_avg field representing average temperature on particular part of the day
        double average = temperatures.stream().mapToInt(a -> a == null ? 0 : (int) a).average().orElse(Double.NaN);
        int factTemp = body.jsonPath().getInt("fact.temp");
        System.out.printf("FACT TEMP: %d%n", factTemp);
        System.out.printf("RESPONSE: %s%n", body.asPrettyString());
        System.out.printf("AVERAGE: %,.2f%n", average);
    }
}