package ru.doublebyte.telegramWeatherBot.types;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This object represents a file ready to be downloaded
 * https://core.telegram.org/bots/api#file
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TelegramFile {

    @JsonProperty("file_id")
    private String fileId;

    @JsonProperty("file_size")
    private int fileSize;

    @JsonProperty("file_path")
    private String filePath;

    public TelegramFile() {

    }

    ///////////////////////////////////////////////////////////////////////////

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
