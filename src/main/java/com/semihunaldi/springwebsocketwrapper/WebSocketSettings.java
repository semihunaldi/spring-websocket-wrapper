package com.semihunaldi.springwebsocketwrapper;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by semihunaldi on 4.04.2019
 */
public class WebSocketSettings {

	private String host;
	private Integer port;

	private String socketName;

	private Map<String, WebSocketEvent> listenerMethodBySubscribeTopicMap = new HashMap<>();

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

	public String getSocketName() {
		return socketName;
	}

	public void setSocketName(String socketName) {
		this.socketName = socketName;
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
