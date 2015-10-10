package ru.doublebyte.telegramWeatherBot.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class CommandTest {

    @Test
    public void testGetCommand() throws Exception {
        Command command = new Command("/command arg0 arg1");
        assertEquals("command", command.getCommand());
        assertArrayEquals(new String[]{"arg0", "arg1"}, command.getArgs());

        command = new Command("/command \"arg0 arg1\" arg\\n2 arg\\t3 a\"rg4 arg5\" a\\\"rg6 arg7");
        assertEquals("command", command.getCommand());
        assertArrayEquals(new String[]{"arg0 arg1", "arg\n2", "arg\t3", "arg4 arg5", "a\"rg6", "arg7"},
                command.getArgs());
    }
}