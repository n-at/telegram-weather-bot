package ru.doublebyte.telegramWeatherBot.enums;

/**
 * File types supported by telegram
 */
public enum FileType {
    photo,
    audio,
    document,
    sticker,
    video,
    voice,
    ;

    /**
     * Get linked request type
     * @return Request type
     */
    public RequestType getRequestType() {
        switch(this) {
            case photo:
                return RequestType.sendPhoto;
            case audio:
                return RequestType.sendAudio;
            case document:
                return RequestType.sendDocument;
            case sticker:
                return RequestType.sendSticker;
            case video:
                return RequestType.sendVideo;
            case voice:
                return RequestType.sendVoice;
            default:
                return RequestType.sendDocument;
        }
    }
}
