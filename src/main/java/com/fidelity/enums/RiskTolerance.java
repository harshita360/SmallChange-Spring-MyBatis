package com.fidelity.enums;

public enum RiskTolerance {
	CONSERVATIVE("CONSERVATIVE"), 
	BELOW_AVERAGE("BELOW_AVERAGE"), 
	AVERAGE("AVERAGE"), 
	ABOVE_AVERAGE("ABOVE_AVERAGE"), 
	AGGRESSIVE("AGGRESSIVE");
	
	private String code;

	private RiskTolerance(String code) {
		this.code = code;
	}

	public static RiskTolerance of(String code) {
		for (RiskTolerance ct: values()) {
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
