package ru.doublebyte.telegramWeatherBot.types;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * This object represent a user's profile pictures
 * https://core.telegram.org/bots/api#userprofilephotos
 */
public class UserProfilePhotos {

    @JsonProperty("total_count")
    private int totalCount;

    @JsonProperty("photos")
    private List<List<PhotoSize>> photos;

    public UserProfilePhotos() {

    }

    ///////////////////////////////////////////////////////////////////////////

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<List<PhotoSize>> getPhotos() {
        return photos;
    }

    public void setPhotos(List<List<PhotoSize>> photos) {
        this.photos = photos;
    }
}
