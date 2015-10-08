package ru.doublebyte.telegramWeatherBot;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.doublebyte.telegramWeatherBot.enums.ChatAction;
import ru.doublebyte.telegramWeatherBot.enums.ParseMode;
import ru.doublebyte.telegramWeatherBot.enums.RequestType;
import ru.doublebyte.telegramWeatherBot.types.Message;
import ru.doublebyte.telegramWeatherBot.types.Update;
import ru.doublebyte.telegramWeatherBot.types.User;
import ru.doublebyte.telegramWeatherBot.types.UserProfilePhotos;
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
     * @return Bot user info
     */
    protected User getMe() throws Exception {
        JSONObject me = makeRequest(RequestType.getMe);
        User user = JsonUtil.toObject(me, User.class);

        if(user == null) {
            throw new Exception("Cannot parse bot user info");
        }

        return user;
    }

    /**
     * Get all available updates
     * @return Array of updates
     */
    protected List<Message> getUpdates() {
        return getUpdates(maxUpdateId + 1, updateLimit, updateTimeout);
    }

    /**
     * Send message to user or group
     * @param chatId Identifier of user or group
     * @param text Message text
     * @return Message object sent to server
     * @throws Exception
     */
    protected Message sendMessage(int chatId, String text) throws Exception{
        Map<String, Object> query = new HashMap<>();
        query.put("chat_id", chatId);
        query.put("text", text);
        return sendMessage(query);
    }

    /**
     * Send message to user or group as reply to other message
     * @param chatId Identifier of user or group
     * @param text Message text
     * @param replyToMessageId Identifier of message to reply
     * @return Message object sent to server
     * @throws Exception
     */
    protected Message sendMessage(int chatId, String text, int replyToMessageId) throws Exception {
        Map<String, Object> query = new HashMap<>();
        query.put("chat_id", chatId);
        query.put("text", text);
        query.put("reply_to_message_id", replyToMessageId);
        return sendMessage(query);
    }

    /**
     * Send message to user or group
     * @param chatId Identifier of user or group
     * @param text Message text
     * @param parseMode Parse mode for markdown
     * @param disableWebPagePreview Disables link previews for links in this message
     * @param replyToMessageId Identifier of message to reply
     * @return Message object sent to server
     * @throws Exception
     */
    protected Message sendMessage(int chatId, String text, ParseMode parseMode,
                                  boolean disableWebPagePreview, int replyToMessageId) throws Exception {

        Map<String, Object> query = new HashMap<>();
        query.put("chat_id", chatId);
        query.put("text", text);
        query.put("parse_mode", parseMode.toString());
        query.put("disable_web_page_preview", disableWebPagePreview);
        query.put("reply_to_message_id", replyToMessageId);
        return sendMessage(query);
    }

    /**
     * Forward any message
     * @param chatId Message recipient
     * @param fromChatId Chat where the original message was sent
     * @param messageId Message identifier
     * @return Message object sent
     * @throws Exception
     */
    protected Message forwardMessage(int chatId, int fromChatId, int messageId) throws Exception {
        Map<String, Object> query = new HashMap<>();
        query.put("chat_id", chatId);
        query.put("from_chat_id", fromChatId);
        query.put("message_id", messageId);

        JSONObject messageObject = makeRequest(RequestType.forwardMessage, query);
        Message message = JsonUtil.toObject(messageObject, Message.class);

        if(message == null) {
            throw new Exception("Cannot parse sent message");
        }

        return message;
    }

    /**
     * Send location
     * @param chatId Chat
     * @param latitude Latitude
     * @param longitude Longitude
     * @return Message sent to server
     */
    protected Message sendLocation(int chatId, double latitude, double longitude) throws Exception {
        Map<String, Object> query = new HashMap<>();
        query.put("chat_id", chatId);
        query.put("latitude", latitude);
        query.put("longitude", longitude);
        return sendLocation(query);
    }

    /**
     * Send location as reply to a message
     * @param chatId Chat
     * @param latitude Latitude
     * @param longitude Longitude
     * @param replyToMessageId Message id to reply
     * @return Message sent to server
     * @throws Exception
     */
    protected Message sendLocation(int chatId, double latitude, double longitude, int replyToMessageId) throws Exception {
        Map<String, Object> query = new HashMap<>();
        query.put("chat_it", chatId);
        query.put("latitude", latitude);
        query.put("longitude", longitude);
        query.put("reply_to_message_id", replyToMessageId);
        return sendLocation(query);
    }

    /**
     * Send chat action
     * @param chatId Recipient
     * @param action Action name
     * @throws Exception
     */
    protected void sendChatAction(int chatId, ChatAction action) throws Exception {
        Map<String, Object> query = new HashMap<>();
        query.put("chat_id", chatId);
        query.put("action", action.toString());
        makeRequest(RequestType.sendChatAction, query);
    }

    /**
     * Get user profile photos
     * @param userId User
     * @return User profile photos description
     * @throws Exception
     */
    protected UserProfilePhotos getUserProfilePhotos(int userId) throws Exception {
        Map<String, Object> query =new HashMap<>();
        query.put("user_id", userId);
        return getUserProfilePhotos(query);
    }

    /**
     * Get user profile photos
     * @param userId User
     * @param offset Sequential number of the first photo to be returned
     * @param limit Limits the number of photos to be retrieved
     * @return User profile photos description
     * @throws Exception
     */
    protected UserProfilePhotos getUserProfilePhotos(int userId, int offset, int limit) throws Exception {
        Map<String, Object> query = new HashMap<>();
        query.put("user_id", userId);
        query.put("offset", offset);
        query.put("limit", limit);
        return getUserProfilePhotos(query);
    }

    ///////////////////////////////////////////////////////////////////////////

    /**
     * Make request to telegram bot api
     * @param requestType Type of request
     * @return Request result
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

            return result.optJSONObject("result");
        } catch(Exception e) {
            logger.error("Failed to make request: " + requestType, e);
            return null;
        }
    }

    /**
     * Make api request without parameters
     * @param requestType Type of request
     * @return Request result
     */
    private JSONObject makeRequest(RequestType requestType) {
        return makeRequest(requestType, new HashMap<>());
    }

    /**
     * Get updates from Telegram
     * @return Array of updates
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

                if(update.getMessage() == null) {
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

    /**
     * Send message to user or group described by given parameters
     * @param query Message parameters
     * @return Message sent to server
     * @throws Exception
     */
    private Message sendMessage(Map<String, Object> query) throws Exception {
        JSONObject messageObject = makeRequest(RequestType.sendMessage, query);
        Message message = JsonUtil.toObject(messageObject, Message.class);

        if(message == null) {
            throw new Exception("Cannot parse sent message");
        }

        return message;
    }

    /**
     * Send location with given parameters
     * @param query Query parameters
     * @return Message sent to server
     * @throws Exception
     */
    private Message sendLocation(Map<String, Object> query) throws Exception {
        JSONObject messageObject = makeRequest(RequestType.sendLocation, query);
        Message message = JsonUtil.toObject(messageObject, Message.class);

        if(message == null) {
            throw new Exception("Cannot parse sent message");
        }

        return message;
    }

    /**
     * Get user profile photos with given parameters
     * @param query Query parameters
     * @return User profile photos description
     * @throws Exception
     */
    private UserProfilePhotos getUserProfilePhotos(Map<String, Object> query) throws Exception {
        JSONObject photosObject = makeRequest(RequestType.getUserProfilePhotos, query);
        UserProfilePhotos photos = JsonUtil.toObject(photosObject, UserProfilePhotos.class);

        if(photos == null) {
            throw new Exception("Cannot parse user profile photos");
        }

        return photos;
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
