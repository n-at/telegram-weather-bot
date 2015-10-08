package ru.doublebyte.telegramWeatherBot.types;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Object that can be User or GroupChat
 * Used in Message class
 */
public class UserOrChat {

    @JsonProperty("id")
    private int id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("username")
    private String userName;

    public UserOrChat() {

    }

    /**
     * Get object value as user
     * @return User
     */
    public User asUser() {
        User user = new User();
        user.setId(id);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUserName(userName);
        return user;
    }

    /**
     * Get object value as GroupChat
     * @return GroupChat
     */
    public GroupChat asChat() {
        GroupChat chat = new GroupChat();
        chat.setId(id);
        chat.setTitle(title);
        return chat;
    }

    public boolean isChat() {
        return title != null;
    }

    public boolean isUser() {
        return firstName != null;
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

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
