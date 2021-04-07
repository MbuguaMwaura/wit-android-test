# Greeting Web Socket Android Client

This is a small app that sent messages to the [Spring Boot Greeting Websocket Service](https://github.com/spring-guides/gs-messaging-stomp-websocket).
This messages are displayed to all the devices with app installed or web pages connected to service.

Check the App Demo in Youtube.

[![APP DEMO VIDEO](https://img.youtube.com/vi/6Iruu7ChwVU/0.jpg)](https://www.youtube.com/watch?v=6Iruu7ChwVU)

## Requirements
- Android Studio 3.1.6
- Minimum SDK 16

## Setup
### Greeting Websocket Service
Download, build and run the [Spring Boot Greeting Websocket Service](https://github.com/spring-guides/gs-messaging-stomp-websocket)
in the server of your preference.

### Set an custom URL and PORT
In the `[root_project_path]/local.properties` file set the following constants:
``` 
greetings.url=example.com
greetings.port=1234
```

If `greetings.url` is not set, the default URL will be `localhost`.
If `greetings.port` is not set, the default port will be `8080`.



