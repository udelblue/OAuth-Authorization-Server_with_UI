package com.udelblue.domain;

public class RequestProcessedResults {
	public String browserName;
	public String iPAddress;
	public String operatingSystem;
	public String uRI;

	public RequestProcessedResults(String operatingSystem, String browserName, String iPAddress, String uRI) {
		super();
		this.operatingSystem = operatingSystem;
		this.browserName = browserName;
		this.iPAddress = iPAddress;
		this.uRI = uRI;
	}

	public String getBrowserName() {
		return browserName;
	}

	public String getiPAddress() {
		return iPAddress;
	}

	public String getOperatingSystem() {
		return operatingSystem;
	}

	public String getuRI() {
		return uRI;
	}

	public void setBrowserName(String browserName) {
		this.browserName = browserName;
	}

	public void setiPAddress(String iPAddress) {
		this.iPAddress = iPAddress;
	}

	public void setOperatingSystem(String operatingSystem) {
		this.operatingSystem = operatingSystem;
	}

	public void setuRI(String uRI) {
		this.uRI = uRI;
	}

}