package com.example.weather;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.net.MalformedURLException;

public class PrimaryController {

    @FXML
    private TextField cityInput;

    @FXML
    private Text weatherText;

    private final String cityAPI = "https://api.weatherapi.com/v1/current.json?key=b8d428b67c744cee806224742233007&q=";
    private final String weatherAPI = "https://api.weatherapi.com/v1/forecast.json?key=b8d428b67c744cee806224742233007&aqi=no&alerts=no&days=1&q=";


    @FXML
    void getWeatherData(ActionEvent event) throws MalformedURLException {
        JSONObject todaysWeather = getWoeid();


        if (todaysWeather != null) {
            JSONObject todaysW = (JSONObject) todaysWeather.get("current");
            JSONObject jLocation = (JSONObject) todaysWeather.get("location");
            JSONObject weatherInf = GetTodaysWeatherInformation(jLocation.get("name").toString());
            System.out.println(todaysWeather);
            double currentTempC = (double) todaysW.get("temp_c");
            double minTempC = (double) weatherInf.get("mintemp_c");
            double maxTempC = (double) weatherInf.get("maxtemp_c");

            weatherText.setText(
                    "Current temperature: " + currentTempC +
                            
                            "\nMin temperature: " + minTempC +
                            "\nMax temperature: " + maxTempC
            );
        } else {
            weatherText.setText("Weather data not available for the selected city.");
        }
    }

    public JSONObject getWoeid() throws MalformedURLException {
        APIConnector apiConnectorCity = new APIConnector(cityAPI + cityInput.getText() + "&aqi=no");
        JSONObject jsonData = apiConnectorCity.getJSONObject(cityAPI + cityInput.getText() + "&aqi=no");

        if (jsonData != null) {
            return jsonData;
        }
        return null;
    }

    public JSONObject GetTodaysWeatherInformation(String woeid) throws MalformedURLException {
        APIConnector apiConnectorWeather = new APIConnector(weatherAPI + woeid + "&days=1&aqi=no&alerts=no");

        JSONObject jsonData = apiConnectorWeather.getJSONObject(weatherAPI + woeid + "&days=1&aqi=no&alerts=no");

        if (jsonData != null && jsonData.containsKey("forecast")) {
            JSONObject forecast = (JSONObject) jsonData.get("forecast");
            JSONArray forecastday = (JSONArray) forecast.get("forecastday");
            if (forecastday.size() > 0) {
                JSONObject todayForecast = (JSONObject) forecastday.get(0);
                JSONObject day = (JSONObject) todayForecast.get("day");
                return day;
            }
        }
        return null;
    }

}