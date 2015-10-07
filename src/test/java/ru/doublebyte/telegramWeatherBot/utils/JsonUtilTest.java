package ru.doublebyte.telegramWeatherBot.utils;

import org.json.JSONObject;
import org.junit.Test;
import ru.doublebyte.telegramWeatherBot.types.User;

import static org.junit.Assert.*;

public class JsonUtilTest {

    @Test
    public void testToObject() throws Exception {
        JSONObject json = new JSONObject();
        json.put("id", 10);
        json.put("first_name", "John");
        json.put("last_name", "Doe");
        json.put("username", "user");

        User user = JsonUtil.toObject(json, User.class);
        assertNotNull(user);
        assertEquals(10, user.getId());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("user", user.getUserName());
    }
}