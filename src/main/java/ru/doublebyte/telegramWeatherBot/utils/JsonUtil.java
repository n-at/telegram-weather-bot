package ru.doublebyte.telegramWeatherBot.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonUtil {

    private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Convert Json object to class instance
     * @param object Json object to convert
     * @param cls Class
     * @param <T> Some class
     * @return T
     */
    public static <T> T toObject(JSONObject object, Class<T> cls) {
        try {
            return (T)objectMapper.readValue(object.toString(), cls);
        } catch(ClassCastException e) {
            logger.error("Cannot cast to " + cls.getName(), e);
        } catch(Exception e) {
            logger.error("Unable to convert json to object", e);
        }
        return null;
    }

}
