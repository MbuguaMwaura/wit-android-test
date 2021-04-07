# Greeting Web Socket Android Client

This is a small app that sent messages to the [Spring Boot Greeting Websocket Service](https://github.com/spring-guides/gs-messaging-stomp-websocket). This messages are displayed to all the devices with app installed or web pages connected to service.

Check the App Demo in Youtube.

[![APP DEMO VIDEO](https://img.youtube.com/vi/6Iruu7ChwVU/0.jpg)](https://www.youtube.com/watch?v=6Iruu7ChwVU)

## Requirements
- Android Studio 3.1.6
- Minimium SDK 16

## Setup
Download, build and run the [Spring Boot Greeting Websocket Service](https://github.com/spring-guides/gs-messaging-stomp-websocket).

If you want to connect your [Spring Boot Greeting Websocket Service](https://github.com/spring-guides/gs-messaging-stomp-websocket) with a real physical device, in the `./Greetings/app/build.gradle` file you need to __set your Greeting Service IP address__ for the `IP_DEVICE buildConfigField`.

Example:

`buildConfigField "String", "IP_DEVICE", "\"192.168.0.100\""`

Then build this source and run it in a Android Device or Emulator.


