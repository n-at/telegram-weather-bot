Telegram weather bot
====================

Simple [Telegram](https://telegram.org/) weather bot.
 
Weather data provided by [OpenWeatherMap](http://openweathermap.org/).

Building
--------

JDK >= 1.8 required.

    $ ./mvnw clean compile package
    
Testing
-------

    $ ./mvnw test
    
Running
-------

    java -Djava.awt.headless=true -Xmx32m -jar telegram-weather-bot.jar

License
-------

BSD
