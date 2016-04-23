package ru.doublebyte.telegramWeatherBot.types;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * User type
 * https://core.telegram.org/bots/api#user
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    @JsonProperty("id")
    private int id;

    @JsonProperty("first_name")
    private String firstName = "";

    @JsonProperty("last_name")
    private String lastName = "";

    @JsonProperty("username")
    private String userName = "";

    public User() {

    }

    @Override
    public String toString() {
        return String.format("User[%s %s (%s), id:%d]", firstName, lastName, userName, id);
    }

    ///////////////////////////////////////////////////////////////////////////

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
