package com.semihunaldi.springwebsocketwrapper;

import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import com.semihunaldi.springwebsocketwrapper.spring.WebSocketStompClient;
import com.semihunaldi.springwebsocketwrapper.spring.SockJsClient;
import com.semihunaldi.springwebsocketwrapper.spring.WebSocketTransport;
import com.semihunaldi.springwebsocketwrapper.spring.sockjs.Transport;
import org.springframework.web.socket.sockjs.frame.Jackson2SockJsMessageCodec;
import processing.core.PApplet;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by semihunaldi on 4.04.2019
 */

public class WebSocketWrapper {

	private final static WebSocketHttpHeaders headers = new WebSocketHttpHeaders();

	private StompSession stompSession;

	private WebSocketSettings webSocketSettings;

	private WebSocketStompClient stompClient;

	public WebSocketWrapper(PApplet pApplet, WebSocketSettings webSocketSettings) {
		this.webSocketSettings = webSocketSettings;
		if(pApplet != null){
			pApplet.registerMethod("dispose", this);
		}
		connectTo(webSocketSettings);
	}

	private void connectTo(WebSocketSettings webSocketSettings) {
		ApplicationContext.getInstance().setCustomSessionId(webSocketSettings.getCustomSessionId());
		try{
			ListenableFuture<StompSession> connect = connect(webSocketSettings);
			stompSession = connect.get();
			subscribeToTopics(stompSession, webSocketSettings);
		} catch(Exception e){
			if(webSocketSettings.isDebug()) {
				e.printStackTrace();
			} else {
				System.out.println(e.getMessage());
			}
		}
	}

	private ListenableFuture<StompSession> connect(WebSocketSettings webSocketSettings) {
		Transport webSocketTransport = new WebSocketTransport(new StandardWebSocketClient());
		List<Transport> transports = Collections.singletonList(webSocketTransport);
		SockJsClient sockJsClient = new SockJsClient(transports);
		sockJsClient.setMessageCodec(new Jackson2SockJsMessageCodec());
		stompClient = new WebSocketStompClient(sockJsClient);
		String url = "//{host}:{port}" + (webSocketSettings.getSocketName().startsWith("/") ? webSocketSettings.getSocketName() : "/" + webSocketSettings.getSocketName());
		if(webSocketSettings.isUseSSL()) {
			url = "wss:" + url;
		} else {
			url = "ws:" + url;
		}
		return stompClient.connect(url, headers, new MyHandler(), webSocketSettings.getHost(), webSocketSettings.getPort());
	}

	private void subscribeToTopics(StompSession stompSession, WebSocketSettings webSocketSettings) {
		for(Map.Entry<String, WebSocketEvent> listenerMethodBySubscribeTopic : webSocketSettings.getListenerMethodBySubscribeTopicMap().entrySet()){
			String topic = listenerMethodBySubscribeTopic.getKey();
			WebSocketEvent listener = listenerMethodBySubscribeTopic.getValue();
			stompSession.subscribe(topic, new StompFrameHandler() {

				public Type getPayloadType(StompHeaders stompHeaders) {
					return byte[].class;
				}

				public void handleFrame(StompHeaders stompHeaders, Object o) {
					listener.eventTriggered(new String((byte[]) o));
				}
			});
		}
	}

	public void sendMessage(String topic, String message) {
		stompSession.send(topic, message.getBytes());
	}

	public void sendMessageToSessionId(String topic, String message, String destinationSessionId) {
		StompHeaders stompHeaders = new StompHeaders();
		stompHeaders.setDestination(topic);
		stompHeaders.add("destinationSessionId",destinationSessionId);
		stompSession.send(stompHeaders, message.getBytes());
	}

	private class MyHandler extends StompSessionHandlerAdapter {

		@Override
		public void afterConnected(StompSession stompSession, StompHeaders stompHeaders) {
			System.out.println("Now connected with sessionId : " + stompSession.getSessionId());
		}

		@Override
		public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
			super.handleException(session, command, headers, payload, exception);
		}

		@Override
		public void handleTransportError(StompSession session, Throwable exception) {
			super.handleTransportError(session, exception);
			if(webSocketSettings.isRetry()) {
				new Thread(() -> {
					try {
						Thread.sleep(webSocketSettings.getRetrySeconds() * 1000);
					} catch (InterruptedException e) {
						if(webSocketSettings.isDebug()) {
							e.printStackTrace();
						} else {
							System.out.println(e.getMessage());
						}
					}
					stompClient.stop();
					connectTo(webSocketSettings);
				}).start();
			}
		}
	}

	public void dispose() {
		System.out.println("disposed");
	}

	public static void main(String[] args) {
		WebSocketSettings webSocketSettings = new WebSocketSettings();
		webSocketSettings.setHost("localhost");
		webSocketSettings.setPort(8080);
		webSocketSettings.setSocketName("SOCKET_BASE_PATH");
		webSocketSettings.setRetry(true);
		webSocketSettings.setDebug(false);
		webSocketSettings.addListenerToTopic("/topic/sample", obj -> System.out.println("event return : " + obj));
		WebSocketWrapper webSocketWrapper = new WebSocketWrapper(null, webSocketSettings);
		webSocketWrapper.sendMessage("/socket/hello", "TEST MESSAGE");
		webSocketWrapper.sendMessageToSessionId("/socket/hello", "TEST MESSAGE","12345-12345");
	}
}