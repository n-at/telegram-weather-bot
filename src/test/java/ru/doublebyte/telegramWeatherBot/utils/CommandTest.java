package ru.doublebyte.telegramWeatherBot.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class CommandTest {

    @Test
    public void testGetCommand() throws Exception {
        Command command = new Command("/command arg0 arg1");
        assertEquals("command", command.getCommand());
        assertArrayEquals(new String[]{"arg0", "arg1"}, command.getArgs());
    }
}