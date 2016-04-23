package ru.doublebyte.telegramWeatherBot.types;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * https://core.telegram.org/bots/api#forcereply
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ForceReply {

    @JsonProperty("force_reply")
    private boolean forceReply;

    @JsonProperty("selective")
    private boolean selective;

    public ForceReply() {

    }

    ///////////////////////////////////////////////////////////////////////////

    public boolean isForceReply() {
        return forceReply;
    }

    public void setForceReply(boolean forceReply) {
        this.forceReply = forceReply;
    }

    public boolean isSelective() {
        return selective;
    }

    public void setSelective(boolean selective) {
        this.selective = selective;
    }
}
