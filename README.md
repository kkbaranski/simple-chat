# Simple Java Chat

The chat is based on simple java socket and unencrypted text protocol. Topology: one server and many clients, each handled in separate thread. The client has simple GUI written in JavaFX.

### Requirements
  - JDK version 1.8
  - Bash - in case of compiling with scripts

### Installing & Compiling

For both server and client:

```sh
$ scripts/compile
```

### Launch
```sh
$ java -jar chat-server.jar PORT
$ java -jar chat-client.jar SERVER_ADDRESS SERVER_PORT [USERNAME]
```

### Todo's

 - Write Tests
 - Encrypt connections
 - Secure protocol

### Author
[krzysztof baranski]

[krzysztof baranski]:pl.linkedin.com/pub/krzysztof-baranski/72/246/728/en

