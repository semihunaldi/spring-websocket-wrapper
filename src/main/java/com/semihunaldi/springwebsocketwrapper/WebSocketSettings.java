package com.semihunaldi.springwebsocketwrapper;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by semihunaldi on 4.04.2019
 */
public class WebSocketSettings {

	private String host;
	private Integer port;
	private boolean useSSL;

	private String socketName;

	private String customSessionId;

	private boolean retry = false;
	private int retrySeconds = 5;

	private boolean debug = false;

	private Map<String, WebSocketEvent> listenerMethodBySubscribeTopicMap = new HashMap<>();

	public WebSocketSettings(
			String host,
			Integer port,
			String socketName,
			String customSessionId,
			Map<String, WebSocketEvent> listenerMethodBySubscribeTopicMap
	) {
		this.customSessionId = customSessionId;
		this.host = host;
		this.port = port;
		this.socketName = socketName;
		this.listenerMethodBySubscribeTopicMap = listenerMethodBySubscribeTopicMap;
	}


	public WebSocketSettings(
			String host,
			Integer port,
			String socketName,
			Map<String, WebSocketEvent> listenerMethodBySubscribeTopicMap
	) {
		this.host = host;
		this.port = port;
		this.socketName = socketName;
		this.listenerMethodBySubscribeTopicMap = listenerMethodBySubscribeTopicMap;
	}

	public WebSocketSettings() {
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public boolean isUseSSL() {
		return useSSL;
	}

	public void setUseSSL(boolean useSSL) {
		this.useSSL = useSSL;
	}

	public String getSocketName() {
		return socketName;
	}

	public void setSocketName(String socketName) {
		this.socketName = socketName;
	}

	public String getCustomSessionId() {
		return customSessionId;
	}

	public void setCustomSessionId(String customSessionId) {
		this.customSessionId = customSessionId;
	}

	public boolean isRetry() {
		return retry;
	}

	public void setRetry(boolean retry) {
		this.retry = retry;
	}

	public int getRetrySeconds() {
		return retrySeconds;
	}

	public void setRetrySeconds(int retrySeconds) {
		this.retrySeconds = retrySeconds;
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public Map<String, WebSocketEvent> getListenerMethodBySubscribeTopicMap() {
		return listenerMethodBySubscribeTopicMap;
	}

	public void setListenerMethodBySubscribeTopicMap(Map<String, WebSocketEvent> listenerMethodBySubscribeTopicMap) {
		this.listenerMethodBySubscribeTopicMap = listenerMethodBySubscribeTopicMap;
	}

	public void addListenerToTopic(String topic, WebSocketEvent webSocketEvent) {
		listenerMethodBySubscribeTopicMap.put(topic, webSocketEvent);
	}
}
