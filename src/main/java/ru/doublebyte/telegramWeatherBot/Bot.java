package ru.doublebyte.telegramWeatherBot;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.doublebyte.telegramWeatherBot.enums.RequestType;
import ru.doublebyte.telegramWeatherBot.types.Message;
import ru.doublebyte.telegramWeatherBot.types.Update;
import ru.doublebyte.telegramWeatherBot.types.User;
import ru.doublebyte.telegramWeatherBot.utils.JsonUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Bot {

    private static final Logger logger = LoggerFactory.getLogger(Bot.class);

    private String apiUrl = "https://api.telegram.org/bot{token}/{request}";
    private String token = "";

    /**
     * Maximum update id. Used for getUpdates
     */
    private int maxUpdateId = 0;

    /**
     * Maximum number of messages loaded with getUpdates
     */
    private int updateLimit = 100;

    /**
     * Timeout for long polling in getUpdates (seconds)
     */
    private int updateTimeout = 0;

    ///////////////////////////////////////////////////////////////////////////

    public Bot(String token) {
        this.token = token;
    }

    ///////////////////////////////////////////////////////////////////////////

    /**
     * Get bot user info
     */
    public User getMe() throws Exception {
        JSONObject me = makeRequest(RequestType.getMe);
        User user = JsonUtil.toObject(me, User.class);

        if(user == null) {
            throw new Exception("Cannot get bot user info");
        }

        return user;
    }

    /**
     * Get all available updates
     * @return List
     */
    protected List<Message> getUpdates() {
        return getUpdates(maxUpdateId + 1, updateLimit, updateTimeout);
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

    /**
     * Get updates from Telegram
     * @return List
     */
    private List<Message> getUpdates(int offset, int limit, int timeout) {
        try {
            HttpResponse<JsonNode> response = Unirest.get(apiUrl)
                    .routeParam("token", token)
                    .routeParam("request", RequestType.getUpdates.toString())
                    .queryString("offset", offset)
                    .queryString("limit", limit)
                    .queryString("timeout", timeout)
                    .asJson();

            JSONObject result = response.getBody().getObject();

            boolean ok = result.getBoolean("ok");
            if(!ok) {
                String description = result.getString("description");
                throw new Exception("Telegram API error: " + description);
            }

            JSONArray array = result.getJSONArray("result");
            List<Message> updates = new ArrayList<>();

            for(int idx = 0; idx < array.length(); idx++) {
                Update update = JsonUtil.toObject(array.getJSONObject(idx), Update.class);

                if(update == null) {
                    logger.warn("Cannot parse an update");
                    continue;
                }

                maxUpdateId = Math.max(maxUpdateId, update.getUpdateId());

                updates.add(update.getMessage());
            }

            return updates;
        } catch(Exception e) {
            logger.error("Failed to get updates", e);
            return new ArrayList<>();
        }
    }

    ///////////////////////////////////////////////////////////////////////////

    public String getApiUrl() {
        return apiUrl;
    }

    public String getToken() {
        return token;
    }

    public int getUpdateLimit() {
        return updateLimit;
    }

    public int getUpdateTimeout() {
        return updateTimeout;
    }

    public void setUpdateLimit(int updateLimit) {
        if(updateLimit < 1 || updateLimit > 100) {
            throw new IllegalArgumentException("Update limit must be in bounds [1, 100]");
        }
        this.updateLimit = updateLimit;
    }

    public void setUpdateTimeout(int updateTimeout) {
        if(updateTimeout < 0) {
            throw new IllegalArgumentException("Update timeout cannot be negative");
        }
        this.updateTimeout = updateTimeout;
    }
}
