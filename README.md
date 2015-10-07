Telegram weather bot
====================

Simple [Telegram](https://telegram.org/) weather bot.
 
Weather data provided by [OpenWeatherMap](http://openweathermap.org/).

Building
--------

Java >= 8 and maven >= 3 required.

    mvn clean compile assembly:single
    
Testing
-------

    mvn test
    
Running
-------

    java -Djava.awt.headless=true -jar telegram-weather-bot-1.0-jar-with-dependencies.jar
