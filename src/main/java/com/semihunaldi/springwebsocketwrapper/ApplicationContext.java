package com.semihunaldi.springwebsocketwrapper;

/**
 * Created by semihunaldi on 8.04.2019
 */
public class ApplicationContext {

	private String customSessionId;

	private static ApplicationContext ourInstance = new ApplicationContext();

	public static ApplicationContext getInstance() {
		return ourInstance;
	}

	private ApplicationContext() {
	}

	public String getCustomSessionId() {
		return customSessionId;
	}

	public void setCustomSessionId(String customSessionId) {
		this.customSessionId = customSessionId;
	}
}
