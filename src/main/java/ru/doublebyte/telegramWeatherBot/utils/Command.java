package ru.doublebyte.telegramWeatherBot.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Bot command
 * Starts with /
 * Arguments are separated by space
 */
public class Command {

    private String command;
    private String[] args;

    public Command(String command) throws Exception {
        if(command == null || command.length() < 2 || !command.startsWith("/")) {
            throw new IllegalArgumentException("Not a command");
        }
        this.command = command.split(" ")[0].substring(1);

        String argumentsStr = command.substring(this.command.length() + 1).trim();

        List<String> arguments = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean insideOfQuotes = false;

        for(int charIdx = 0; charIdx < argumentsStr.length(); charIdx++) {
            char character = argumentsStr.charAt(charIdx);

            switch(character) {
                //quoted string ignores whitespace
                case '"':
                    if(insideOfQuotes) {
                        if(sb.length() != 0) {
                            arguments.add(sb.toString());
                            sb = new StringBuilder();
                        }
                        insideOfQuotes = false;
                    } else {
                        insideOfQuotes = true;
                    }
                    break;

                //whitespace is argument delimiter (unless in quoted string)
                case ' ':
                    if(!insideOfQuotes) {
                        if(sb.length() != 0) {
                            arguments.add(sb.toString());
                            sb = new StringBuilder();
                        }
                    } else {
                        sb.append(character);
                    }
                    break;

                //escape character
                case '\\':
                    if(charIdx == argumentsStr.length() - 1) {
                        break;
                    }
                    char nextCharacter = argumentsStr.charAt(charIdx+1);
                    charIdx++;

                    switch(nextCharacter) {
                        case '"':
                            sb.append('"');
                            break;
                        case 'n':
                            sb.append('\n');
                            break;
                        case 't':
                            sb.append('\t');
                            break;
                    }
                    break;

                default:
                    sb.append(character);
            }
        }

        if(sb.length() > 0) {
            arguments.add(sb.toString());
        }

        this.args = arguments.toArray(new String[arguments.size()]);
    }

    ///////////////////////////////////////////////////////////////////////////

    public String getCommand() {
        return command;
    }

    public String[] getArgs() {
        return args;
    }

}
