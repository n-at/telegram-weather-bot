package ru.doublebyte.telegramWeatherBot.types;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This object represents a video file
 * https://core.telegram.org/bots/api#video
 */
public class Video {

    @JsonProperty("file_id")
    private String fileId;

    @JsonProperty("width")
    private int width;

    @JsonProperty("height")
    private int height;

    @JsonProperty("duration")
    private int duration;

    @JsonProperty("thumb")
    private PhotoSize thumb;

    @JsonProperty("mime_type")
    private String mimeType;

    @JsonProperty("file_size")
    private int fileSize;

    public Video() {

    }

    ///////////////////////////////////////////////////////////////////////////

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public PhotoSize getThumb() {
        return thumb;
    }

    public void setThumb(PhotoSize thumb) {
        this.thumb = thumb;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }
}
