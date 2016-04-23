Telegram weather bot
====================

Simple [Telegram](https://telegram.org/) weather bot.
 
Weather data provided by [OpenWeatherMap](http://openweathermap.org/).

Building
--------

JDK >= 1.8 and maven >= 3 required.

    mvn clean compile package
    
Testing
-------

    mvn test
    
Running
-------

    java -Djava.awt.headless=true -jar telegram-weather-bot.jar

License
-------

BSD
