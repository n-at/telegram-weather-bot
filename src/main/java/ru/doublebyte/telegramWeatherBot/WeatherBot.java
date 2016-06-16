package ru.doublebyte.telegramWeatherBot;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.doublebyte.telegramWeatherBot.enums.WeatherUnits;
import ru.doublebyte.telegramWeatherBot.types.Chat;
import ru.doublebyte.telegramWeatherBot.types.Message;
import ru.doublebyte.telegramWeatherBot.utils.Command;
import ru.doublebyte.telegramWeatherBot.utils.JsonUtil;
import ru.doublebyte.telegramWeatherBot.weatherTypes.CurrentWeather;
import ru.doublebyte.telegramWeatherBot.weatherTypes.Forecast;
import ru.doublebyte.telegramWeatherBot.weatherTypes.WeatherCondition;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Weather bot class
 */
public class WeatherBot extends Bot {

    private static final Logger logger = LoggerFactory.getLogger(WeatherBot.class);

    private String weatherApiKey = "";
    private String language = "en";
    private WeatherUnits units = WeatherUnits.metric;

    private final String weatherApiUrl = "http://api.openweathermap.org/data/2.5/{endpoint}";
    private final String messagesBundleName = "weatherBot.messages";

    private ResourceBundle messages = ResourceBundle.getBundle(messagesBundleName, Locale.ENGLISH);

    ///////////////////////////////////////////////////////////////////////////

    public WeatherBot(String token, String weatherApiKey) {
        super(token);
        this.weatherApiKey = weatherApiKey;
    }

    ///////////////////////////////////////////////////////////////////////////

    /**
     * Handle users requests
     */
    public void handleRequests() {
        List<Message> updates = getUpdates();

        for(Message message: updates) {
            Chat chat = message.getChat();
            Command command;

            try {
                command = new Command(message.getText());
            } catch(Exception e) {
                sendCurrentWeather(chat, message.getText());
                continue;
            }

            switch(command.getCommand()) {
                case "weather":
                    sendCurrentWeather(chat, command);
                    break;
                case "forecast":
                    sendForecast(chat, command);
                    break;
                case "help":
                    sendReply(chat, messages.getString("help_weather"));
                    break;
                case "start":
                    sendReply(chat, messages.getString("start_message"));
                    break;
                default:
                    sendReply(chat, messages.getString("unknown_command"));
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////

    /**
     * Send weather to chat
     * @param chat Chat to send weather
     * @param command Command from user
     */
    private void sendCurrentWeather(Chat chat, Command command) {
        try {
            String[] args = command.getArgs();
            if(args.length == 0) {
                sendReply(chat, messages.getString("need_city_name"));
                return;
            }
            sendCurrentWeather(chat, args[0]);
        } catch(Exception e) {
            logger.error("Weather error", e);
            sendReply(chat, messages.getString("weather_get_error"));
        }
    }

    /**
     * Send weather in city to chat
     * @param chat Chat
     * @param city City name
     */
    private void sendCurrentWeather(Chat chat, String city) {
        try {
            CurrentWeather currentWeather = getCurrentWeather(city);

            int temperature = (int)Math.round(currentWeather.getWeather().getTemperature());
            int humidity = currentWeather.getWeather().getHumidity();
            int pressure = (int)Math.round(currentWeather.getWeather().getPressure() * 0.75006375541921);
            String conditions = getConditions(currentWeather.getWeatherCondition());

            String weather = String.format(messages.getString("current_weather_format"),
                    currentWeather.getCityName(),
                    temperature, conditions, humidity, pressure);

            sendReply(chat, weather);
        } catch(Exception e) {
            logger.error("Weather error", e);
            sendReply(chat, messages.getString("weather_get_error"));
        }
    }

    /**
     * Send forecast to chat
     * @param chat Chat to send forecast
     * @param command Command from user
     */
    private void sendForecast(Chat chat, Command command) {
        try {
            String[] args = command.getArgs();
            if(args.length == 0) {
                sendReply(chat, messages.getString("need_city_name"));
                return;
            }
            String city = args[0];

            Forecast forecast = getForecast(city);

            List<String> forecastItems = new ArrayList<>();
            for(CurrentWeather weather: forecast.getList()) {
                int minTemp = (int)Math.round(weather.getWeather().getMinTemperature());
                int maxTemp = (int)Math.round(weather.getWeather().getMaxTemperature());
                String conditions = getConditions(weather.getWeatherCondition());
                String forecastItem = String.format(messages.getString("forecast_item_format"),
                        weather.getDateText(), minTemp, maxTemp, conditions);
                forecastItems.add(forecastItem);
            }

            String forecastStr = String.format(messages.getString("forecast_format"),
                    forecast.getCity().getName(), forecast.getCity().getCountry(), String.join("\n", forecastItems));

            sendReply(chat, forecastStr);
        } catch (Exception e) {
            logger.error("Forecast error", e);
            sendReply(chat, messages.getString("forecast_get_error"));
        }
    }

    ///////////////////////////////////////////////////////////////////////////

    /**
     * Get current weather
     * @param city City to get
     * @return Current weather
     * @throws Exception
     */
    private CurrentWeather getCurrentWeather(String city) throws Exception {
        try {
            JSONObject weatherObject = getWeatherObject("weather", city);
            CurrentWeather weather = JsonUtil.toObject(weatherObject, CurrentWeather.class);
            if(weather == null) {
                throw new Exception("Cannot parse weather");
            }
            return weather;
        } catch(Exception e) {
            logger.error("Cannot get weather data", e);
            throw e;
        }
    }

    /**
     * Get 5 day forecast
     * @param city City to get
     * @return Weather forecast
     * @throws Exception
     */
    private Forecast getForecast(String city) throws Exception {
        try {
            JSONObject forecastObject = getWeatherObject("forecast", city);
            Forecast forecast = JsonUtil.toObject(forecastObject, Forecast.class);
            if(forecast == null) {
                throw new Exception("Cannot parse forecast");
            }
            return forecast;
        } catch(Exception e) {
            logger.error("Cannot get forecast", e);
            throw e;
        }
    }

    ///////////////////////////////////////////////////////////////////////////

    /**
     * Get weather object
     * @param endpoint API endpoint
     * @param city City to get
     * @return Weather json object
     */
    private JSONObject getWeatherObject(String endpoint, String city) throws Exception {
        HttpResponse<JsonNode> response = Unirest.get(weatherApiUrl)
                .routeParam("endpoint", endpoint)
                .queryString("APPID", weatherApiKey)
                .queryString("lang", language)
                .queryString("units", units.toString())
                .queryString("q", city)
                .asJson();
        return response.getBody().getObject();
    }

    /**
     * Send reply message to user without throwing an exception
     * @param chat Chat to send message
     * @param message Message
     */
    private void sendReply(Chat chat, String message) {
        try {
            sendMessage(chat.getId(), message);
        } catch(Exception e) {
            logger.error("Cannot send reply to " + chat.toString(), e);
        }
    }

    ///////////////////////////////////////////////////////////////////////////

    /**
     * Collect conditions string
     * @param conditions Conditions list
     * @return Collected conditions
     */
    private String getConditions(List<WeatherCondition> conditions) {
        return conditions.stream()
                .map(WeatherCondition::getDescription)
                .collect(Collectors.joining(", "));
    }

    ///////////////////////////////////////////////////////////////////////////

    public String getWeatherApiKey() {
        return weatherApiKey;
    }

    public String getWeatherApiUrl() {
        return weatherApiUrl;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
        messages = ResourceBundle.getBundle(messagesBundleName, new Locale(language));
    }

    public WeatherUnits getUnits() {
        return units;
    }

    public void setUnits(WeatherUnits units) {
        this.units = units;
    }
}
