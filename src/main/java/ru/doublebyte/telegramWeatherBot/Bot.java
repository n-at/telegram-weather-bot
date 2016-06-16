package ru.doublebyte.telegramWeatherBot;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.doublebyte.telegramWeatherBot.enums.ChatAction;
import ru.doublebyte.telegramWeatherBot.enums.FileType;
import ru.doublebyte.telegramWeatherBot.enums.ParseMode;
import ru.doublebyte.telegramWeatherBot.enums.RequestType;
import ru.doublebyte.telegramWeatherBot.types.*;
import ru.doublebyte.telegramWeatherBot.utils.JsonUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Base class for telegram bots
 */
public abstract class Bot {

    private static final Logger logger = LoggerFactory.getLogger(Bot.class);

    private String apiUrl = "https://api.telegram.org/bot{token}/{request}";
    private String token = "";

    /**
     * Maximum update id. Used for getUpdates
     */
    private long maxUpdateId = 0;

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

    ///////////////////////////////////////////////////////////////////////////
    //Send Photo

    /**
     * Send photo file
     * @param chatId Message recipient
     * @param photo Photo file
     * @param caption Caption text. Not sent when null
     * @return Message sent to server
     * @throws Exception
     */
    protected Message sendPhoto(int chatId, File photo, String caption) throws Exception {
        Map<String, Object> query = new HashMap<>();
        query.put("chat_id", chatId);
        if(caption != null) {
            query.put("caption", caption);
        }
        return sendFile(FileType.photo, query, photo);
    }

    /**
     * Send photo file as reply to message
     * @param chatId Message recipient
     * @param photo Photo file
     * @param replyToMessageId Message id to reply
     * @param caption Caption text. Not sent when null
     * @return Message sent to server
     * @throws Exception
     */
    protected Message sendPhoto(int chatId, File photo, int replyToMessageId, String caption) throws Exception {
        Map<String, Object> query = new HashMap<>();
        query.put("chat_id", chatId);
        query.put("reply_to_message_id", replyToMessageId);
        if(caption != null) {
            query.put("caption", caption);
        }
        return sendFile(FileType.photo, query, photo);
    }

    /**
     * Send photo as file id
     * @param chatId Photo recipient
     * @param photoFileId File id on telegram server
     * @param caption Caption text. Not sent when null
     * @return Message sent to server
     * @throws Exception
     */
    protected Message sendPhoto(int chatId, int photoFileId, String caption) throws Exception {
        Map<String, Object> query = new HashMap<>();
        query.put("chat_id", chatId);
        query.put("photo", photoFileId);
        if(caption != null) {
            query.put("caption", caption);
        }
        return sendFile(FileType.photo, query);
    }

    /**
     * Send photo as file id in reply to message
     * @param chatId Photo recipient
     * @param photoFileId File id on telegram server
     * @param replyToMessageId Message id to reply
     * @param caption Caption text. Not sent when null
     * @return Message sent to server
     * @throws Exception
     */
    protected Message sendPhoto(int chatId, int photoFileId, int replyToMessageId, String caption) throws Exception {
        Map<String, Object> query = new HashMap<>();
        query.put("chat_id", chatId);
        query.put("photo", photoFileId);
        query.put("reply_to_message_id", replyToMessageId);
        if(caption != null) {
            query.put("caption", caption);
        }
        return sendFile(FileType.photo, query);
    }

    ///////////////////////////////////////////////////////////////////////////
    //Send Audio

    /**
     * Send audio file
     * @param chatId Recipient id
     * @param audio Audio file
     * @param title Track name. Not sent when null
     * @param performer Performer. Not sent when null
     * @return Message sent to server
     */
    protected Message sendAudio(int chatId, File audio, String title, String performer) throws Exception {
        Map<String, Object> query = new HashMap<>();
        query.put("chat_id", chatId);
        if(title != null) {
            query.put("title", title);
        }
        if(performer != null) {
            query.put("performer", performer);
        }
        return sendFile(FileType.audio, query, audio);
    }

    /**
     * Send audio file in reply to message
     * @param chatId Recipient id
     * @param audio Audio file
     * @param replyToMessageId Message id to reply
     * @param title Track name. Not sent when null
     * @param performer Performer. Not sent when null
     * @return Message sent to server
     */
    protected Message sendAudio(int chatId, File audio, int replyToMessageId, String title, String performer) throws Exception {
        Map<String, Object> query = new HashMap<>();
        query.put("chat_id", chatId);
        query.put("reply_to_message_id", replyToMessageId);
        if(title != null) {
            query.put("title", title);
        }
        if(performer != null) {
            query.put("performer", performer);
        }
        return sendFile(FileType.audio, query, audio);
    }

    /**
     * Send audio file as file id
     * @param chatId Recipient id
     * @param audioFileId Audio file id on telegram server
     * @param title Track name. Not sent when null
     * @param performer Performer. Not sent when null
     * @return Message sent to server
     */
    protected Message sendAudio(int chatId, int audioFileId, String title, String performer) throws Exception {
        Map<String, Object> query = new HashMap<>();
        query.put("chat_id", chatId);
        query.put("audio", audioFileId);
        if(title != null) {
            query.put("title", title);
        }
        if(performer != null) {
            query.put("performer", performer);
        }
        return sendFile(FileType.audio, query);
    }

    /**
     * Send audio file as file id in reply to message
     * @param chatId Recipient id
     * @param audioFileId Audio file id on telegram server
     * @param replyToMessageId Message id to reply
     * @param title Track name. Not sent when null
     * @param performer Performer. Not sent when null
     * @return Message sent to server
     */
    protected Message sendAudio(int chatId, int audioFileId, int replyToMessageId, String title, String performer) throws Exception {
        Map<String, Object> query = new HashMap<>();
        query.put("chat_id", chatId);
        query.put("audio", audioFileId);
        query.put("reply_to_message_id", replyToMessageId);
        if(title != null) {
            query.put("title", title);
        }
        if(performer != null) {
            query.put("performer", performer);
        }
        return sendFile(FileType.audio, query);
    }

    ///////////////////////////////////////////////////////////////////////////
    //Send Document

    /**
     * Send document
     * @param chatId Recipient id
     * @param document File to send
     * @return Message send to server
     * @throws Exception
     */
    protected Message sendDocument(int chatId, File document) throws Exception {
        Map<String, Object> query = new HashMap<>();
        query.put("chat_id", chatId);
        return sendFile(FileType.document, query, document);
    }

    /**
     * Send document in reply to message
     * @param chatId Recipient id
     * @param document File to send
     * @param replyToMessageId Message id to reply
     * @return Message send to server
     * @throws Exception
     */
    protected Message sendDocument(int chatId, File document, int replyToMessageId) throws Exception {
        Map<String, Object> query = new HashMap<>();
        query.put("chat_id", chatId);
        query.put("reply_to_message_id", replyToMessageId);
        return sendFile(FileType.document, query, document);
    }

    /**
     * Send document as file id
     * @param chatId Recipient id
     * @param documentFileId File id on telegram server
     * @return Message send to server
     * @throws Exception
     */
    protected Message sendDocument(int chatId, int documentFileId) throws Exception {
        Map<String, Object> query = new HashMap<>();
        query.put("chat_id", chatId);
        query.put("document", documentFileId);
        return sendFile(FileType.document, query);
    }

    /**
     * Send document as file id in reply to message
     * @param chatId Recipient id
     * @param documentFileId File id on telegram server
     * @param replyToMessageId Message id to reply
     * @return Message send to server
     * @throws Exception
     */
    protected Message sendDocument(int chatId, int documentFileId, int replyToMessageId) throws Exception {
        Map<String, Object> query = new HashMap<>();
        query.put("chat_id", chatId);
        query.put("document", documentFileId);
        query.put("reply_to_message_id", replyToMessageId);
        return sendFile(FileType.document, query);
    }

    ///////////////////////////////////////////////////////////////////////////
    //Send Video

    /**
     * Send video
     * @param chatId Recipient id
     * @param video Video file
     * @param caption Video caption. Not sent when null
     * @return Message sent to server
     * @throws Exception
     */
    protected Message sendVideo(int chatId, File video, String caption) throws Exception {
        Map<String, Object> query = new HashMap<>();
        query.put("chat_id", chatId);
        if(caption != null) {
            query.put("caption", caption);
        }
        return sendFile(FileType.video, query, video);
    }

    /**
     * Send video in reply to message
     * @param chatId Recipient id
     * @param video Video file
     * @param replyToMessageId Message id to reply
     * @param caption Video caption. Not sent when null
     * @return Message sent to server
     * @throws Exception
     */
    protected Message sendVideo(int chatId, File video, int replyToMessageId, String caption) throws Exception {
        Map<String, Object> query = new HashMap<>();
        query.put("chat_id", chatId);
        query.put("reply_to_message_id", replyToMessageId);
        if(caption != null) {
            query.put("caption", caption);
        }
        return sendFile(FileType.video, query, video);
    }

    /**
     * Send video as file id
     * @param chatId Recipient id
     * @param videoFileId Video file id on telegram server
     * @param caption Video caption. Not sent when null
     * @return Message sent to server
     * @throws Exception
     */
    protected Message sendVideo(int chatId, int videoFileId, String caption) throws Exception {
        Map<String, Object> query = new HashMap<>();
        query.put("chat_id", chatId);
        query.put("video", videoFileId);
        if(caption != null) {
            query.put("caption", caption);
        }
        return sendFile(FileType.video, query);
    }

    /**
     * Send video as file id in reply to message
     * @param chatId Recipient id
     * @param videoFileId Video file id on telegram server
     * @param replyToMessageId Message id to reply
     * @param caption Video caption. Not sent when null
     * @return Message sent to server
     * @throws Exception
     */
    protected Message sendVideo(int chatId, int videoFileId, int replyToMessageId, String caption) throws Exception {
        Map<String, Object> query = new HashMap<>();
        query.put("chat_id", chatId);
        query.put("video", videoFileId);
        query.put("reply_to_message_id", replyToMessageId);
        if(caption != null) {
            query.put("caption", caption);
        }
        return sendFile(FileType.video, query);
    }

    ///////////////////////////////////////////////////////////////////////////

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

    /**
     * Get file download info
     * @param fileId File id
     * @return File download info
     */
    protected TelegramFile getFile(int fileId) throws Exception {
        Map<String, Object> query = new HashMap<>();
        query.put("file_id", fileId);

        JSONObject fileObject = makeRequest(RequestType.getFile, query);
        TelegramFile telegramFile = JsonUtil.toObject(fileObject, TelegramFile.class);

        if(telegramFile == null) {
            throw new Exception("Cannot parse file");
        }

        return telegramFile;
    }

    ///////////////////////////////////////////////////////////////////////////

    /**
     * Make request to telegram bot api
     * @param requestType Type of request
     * @return Request result
     */
    private JSONObject makeRequest(RequestType requestType, Map<String, Object> parameters) throws Exception {
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
            logger.error("Failed to make request: " + requestType);
            throw e;
        }
    }

    /**
     * Make api request without parameters
     * @param requestType Type of request
     * @return Request result
     */
    private JSONObject makeRequest(RequestType requestType) throws Exception {
        return makeRequest(requestType, new HashMap<>());
    }

    /**
     * Get updates from Telegram
     * @return Array of updates
     */
    private List<Message> getUpdates(long offset, int limit, int timeout) {
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

    /**
     * Send file to server
     * @param fileType File type
     * @param query Query parameters
     * @param file File to send
     * @return Message sent to server
     */
    private Message sendFile(FileType fileType, Map<String, Object> query, File file) throws Exception {
        try {
            HttpResponse<JsonNode> response = Unirest.post(apiUrl)
                    .routeParam("token", token)
                    .routeParam("request", fileType.getRequestType().toString())
                    .queryString(query)
                    .field(fileType.toString(), file)
                    .asJson();
            JSONObject result = response.getBody().getObject();

            boolean ok = result.getBoolean("ok");
            if(!ok) {
                String description = result.getString("description");
                throw new Exception("Telegram API error: " + description);
            }

            JSONObject messageObject = result.getJSONObject("result");
            Message message = JsonUtil.toObject(messageObject, Message.class);
            if(message == null) {
                throw new Exception("Cannot parse message");
            }

            return message;
        } catch(Exception e) {
            logger.error("Failed to make request: " + fileType.getRequestType());
            throw e;
        }
    }

    /**
     * Send file to server. File is sent by id in query
     * @param fileType File type
     * @param query Query parameters
     * @return Message sent to server
     */
    private Message sendFile(FileType fileType, Map<String, Object> query) throws Exception {
        JSONObject messageObject = makeRequest(fileType.getRequestType(), query);
        Message message = JsonUtil.toObject(messageObject, Message.class);

        if(message == null) {
            throw new Exception("Cannot parse message while sending " + fileType.toString());
        }

        return message;
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
