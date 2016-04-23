package ru.doublebyte.telegramWeatherBot.types;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This object represents a group chat
 * https://core.telegram.org/bots/api#groupchat
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GroupChat {

    @JsonProperty("id")
    private int id;

    @JsonProperty("title")
    private String title;

    public GroupChat() {

    }

    ///////////////////////////////////////////////////////////////////////////

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
