package ru.doublebyte.telegramWeatherBot.types;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * This object represents a custom keyboard with reply options
 * https://core.telegram.org/bots/api#replykeyboardmarkup
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReplyKeyboardMarkup {

    @JsonProperty("keyboard")
    private List<List<String>> keyboard;

    @JsonProperty("resize_keyboard")
    private boolean resizeKeyboard;

    @JsonProperty("one_time_keyboard")
    private boolean oneTimeKeyboard;

    @JsonProperty("selective")
    private boolean selective;

    public ReplyKeyboardMarkup() {

    }

    ///////////////////////////////////////////////////////////////////////////

    public List<List<String>> getKeyboard() {
        return keyboard;
    }

    public void setKeyboard(List<List<String>> keyboard) {
        this.keyboard = keyboard;
    }

    public boolean isResizeKeyboard() {
        return resizeKeyboard;
    }

    public void setResizeKeyboard(boolean resizeKeyboard) {
        this.resizeKeyboard = resizeKeyboard;
    }

    public boolean isOneTimeKeyboard() {
        return oneTimeKeyboard;
    }

    public void setOneTimeKeyboard(boolean oneTimeKeyboard) {
        this.oneTimeKeyboard = oneTimeKeyboard;
    }

    public boolean isSelective() {
        return selective;
    }

    public void setSelective(boolean selective) {
        this.selective = selective;
    }
}
