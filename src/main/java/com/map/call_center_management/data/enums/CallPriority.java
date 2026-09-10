package com.map.call_center_management.data.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum CallPriority {
	LOW("LOW"), MEDIUM("MEDIUM"), HIGH("HIGH"), CRITICAL("CRITICAL");
	
	private final String value;
	
	CallPriority(String value) {
		this.value = value;
	}
	
	@JsonValue
	public String getValue() {
		return this.value;
	}
}
