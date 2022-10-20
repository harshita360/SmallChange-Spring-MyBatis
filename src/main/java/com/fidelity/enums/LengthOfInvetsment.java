package com.fidelity.enums;

public enum LengthOfInvetsment {
	BASIC("T0Y_T5Y"),
	LOW("T5Y_T7Y"),
	MEDIUM("T7Y_T10Y"),
	HIGH("T10Y_T15Y");
	
	private String code;

	private LengthOfInvetsment(String code) {
		this.code = code;
	}

	public static LengthOfInvetsment of(String code) {
		for (LengthOfInvetsment ct: values()) {
			if (ct.code.equals(code)) {
				return ct;
			}
		}
		throw new IllegalArgumentException("Unknown code");
	}
	
	public String getCode() {
		return code;
	}

}
