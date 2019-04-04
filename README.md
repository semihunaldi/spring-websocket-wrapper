
# Standalone spring stomp websocket wrapper.

###Processing compatible.

#### add jar to your processing sketch 


Usage

```java

public static void main(String[] args) {
    WebSocketSettings webSocketSettings = new WebSocketSettings();
    webSocketSettings.setHost("localhost");
    webSocketSettings.setPort(8080);
    webSocketSettings.setSocketName("SOCKET_BASE_PATH");
    webSocketSettings.addListenerToTopic("/topic/sample", obj -> System.out.println("event return : " + obj));
    WebSocketWrapper webSocketWrapper = new WebSocketWrapper(null,webSocketSettings);
    webSocketWrapper.sendMessage("/socket/hello","TEST MESSAGE");
    try{
        Thread.sleep(60000);
    } catch(InterruptedException e){
        e.printStackTrace();
    }
}
	
```