package ru.doublebyte.telegramWeatherBot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableScheduling
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Autowired
    private WeatherBot weatherBot;

    @Scheduled(fixedDelayString = "${bot.poll_interval}")
    public void update() {
        weatherBot.handleRequests();
    }

}
