# Chat Application

A simple chat application implemented in Java, consisting of a server and client components.
The chat is done on local, the server and all the machines are all on the same computer.
There could be additional features in the future. 

## Features

- **Server**: Handles connections from multiple clients, relays messages between clients, and supports various commands for managing the chat.
- **Client**: Connects to the server, allows users to enter their name, send messages to the server, and receive messages from other clients.
- **ASCII Art Reactions**: Includes a collection of ASCII art reactions that users can send in the chat.

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 8 or later

### Launching the application via the terminal

1. Clone the repository
2. Go to the beginning of the package :
```bash
cd src/main/java
```
3. Create the `.class` files 
```bash
javac cuni/mff/chollonm/utils/*
```
4. Start the server:
```bash
 java cuni.mff.chollonm.utils.Server 
```
5. Change terminal and launch the first client
```bash
 java cuni.mff.chollonm.utils.Client 
```
6. Repeat the operation `.5` the same number of time that you want clients.
Follow the prompts to enter your name and start chatting.
7. When you want to end the chat, don't do it the harsh way with `^C` but simply type `exit`.

### Launching the application via IntelliJ

1. Go to the server file and click on the `RUN` button to run the current file.
2. Go to the client file and click on the `RUN` button to create one client.
3. Repeat the operation `.3` the same number of time that you want clients.
      Follow the prompts to enter your name and start chatting.


## Helpful commands when chatting

**-hp**: Display help message with available commands.

**-dm** _[username] [message]_: Send a private message to a specific user.

**-cu** _[group_name] [members ...]_: Create a private group chat.

**-sg** _[group_name] [message]_: Send a message to a specific group.

**-la**: Display available ASCII art reactions.

**-pa** _[name_of_ascii_art]_: Send a reaction using ASCII art.

**-sc** _[color]_: Change the color of your chat messages.

**exit**: Exit the chat application.

## JavaDoc documentation

The `docs` folder contains the JavaDoc documentation generated from the source code. You can open the `index.html` file in a web browser to view the documentation.

## Author
This chat application was developed by [Mathilde Chollon](https://gitlab.mff.cuni.cz/chollonm).
