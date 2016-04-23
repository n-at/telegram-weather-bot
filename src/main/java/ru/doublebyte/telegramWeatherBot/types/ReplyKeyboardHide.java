package ru.doublebyte.telegramWeatherBot.types;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * https://core.telegram.org/bots/api#replykeyboardhide
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReplyKeyboardHide {

    @JsonProperty("hide_kayboard")
    private boolean hideKeyboard;

    @JsonProperty("selective")
    private boolean selective;

    public ReplyKeyboardHide() {

    }

    ///////////////////////////////////////////////////////////////////////////

    public boolean isHideKeyboard() {
        return hideKeyboard;
    }

    public void setHideKeyboard(boolean hideKeyboard) {
        this.hideKeyboard = hideKeyboard;
    }

    public boolean isSelective() {
        return selective;
    }

    public void setSelective(boolean selective) {
        this.selective = selective;
    }
}
