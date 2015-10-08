package ru.doublebyte.telegramWeatherBot.utils;

import java.util.Arrays;

/**
 * Bot command
 * Trivial implementation
 */
public class Command {

    private String command;
    private String[] args;

    public Command(String command) throws Exception {
        if(command == null || command.length() < 2 || !command.startsWith("/")) {
            throw new IllegalArgumentException("Not a command");
        }

        String[] parts = command.split(" ");

        this.command = parts[0].substring(1);
        this.args = Arrays.copyOfRange(parts, 1, parts.length);
    }

    ///////////////////////////////////////////////////////////////////////////

    public String getCommand() {
        return command;
    }

    public String[] getArgs() {
        return args;
    }

}
