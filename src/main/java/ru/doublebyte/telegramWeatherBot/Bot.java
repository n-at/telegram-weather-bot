package ru.doublebyte.telegramWeatherBot;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.doublebyte.telegramWeatherBot.enums.RequestType;
import ru.doublebyte.telegramWeatherBot.types.User;
import ru.doublebyte.telegramWeatherBot.utils.JsonUtil;

import java.util.HashMap;
import java.util.Map;

public class Bot {

    private static final Logger logger = LoggerFactory.getLogger(Bot.class);

    private String apiUrl = "https://api.telegram.org/bot{token}/{request}";
    private String token = "";

    public Bot(String token) {
        this.token = token;
    }

    ///////////////////////////////////////////////////////////////////////////

    /**
     * Get bot user info
     */
    public User getMe() {
        JSONObject me = makeRequest(RequestType.getMe);
        User user = JsonUtil.toObject(me, User.class);

        if(user == null) {
            logger.warn("Cannot get bot user info!");
            return new User();
        }

        return user;
    }

    ///////////////////////////////////////////////////////////////////////////

    /**
     * Make request to telegram bot api
     * @param requestType RequestType
     * @return JSONObject
     */
    private JSONObject makeRequest(RequestType requestType, Map<String, Object> parameters) {
        try {
            HttpResponse<JsonNode> response = Unirest.get(apiUrl)
                    .routeParam("token", token)
                    .routeParam("request", requestType.toString())
                    .queryString(parameters)
                    .asJson();

            JSONObject result = response.getBody().getObject();

            boolean ok = result.getBoolean("ok");
            if(!ok) {
                String description = result.getString("description");
                throw new Exception("Telegram API error: " + description);
            }

            return result.getJSONObject("result");
        } catch(Exception e) {
            logger.error("Failed to make request: " + requestType, e);
            return null;
        }
    }

    /**
     * Make api request without parameters
     * @param requestType RequestType
     * @return JSONObject
     */
    private JSONObject makeRequest(RequestType requestType) {
        return makeRequest(requestType, new HashMap<>());
    }

    ///////////////////////////////////////////////////////////////////////////

    public String getApiUrl() {
        return apiUrl;
    }

    public String getToken() {
        return token;
    }
}
