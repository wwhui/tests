package com.sdhz.domain;

import java.io.Serializable;

/** …Ë÷√–≈œ¢ */
public class Config implements Serializable {
	private int configId;
	private String configName;
	private int configType;
	private int configResult;

	public int getConfigId() {
		return configId;
	}
	public void setConfigId(int configId) {
		this.configId = configId;
	}
	public String getConfigName() {
		return configName;
	}
	public void setConfigName(String configName) {
		this.configName = configName;
	}
	public int getConfigType() {
		return configType;
	}
	public void setConfigType(int configType) {
		this.configType = configType;
	}
	public int getConfigResult() {
		return configResult;
	}
	public void setConfigResult(int configResult) {
		this.configResult = configResult;
	}
}
