package ru.doublebyte.telegramWeatherBot.types;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This object represents an incoming update
 * https://core.telegram.org/bots/api#update
 */
public class Update {

    @JsonProperty("update_id")
    private int updateId;

    @JsonProperty("message")
    private Message message;

    public Update() {

    }

    ///////////////////////////////////////////////////////////////////////////

    public int getUpdateId() {
        return updateId;
    }

    public void setUpdateId(int updateId) {
        this.updateId = updateId;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
