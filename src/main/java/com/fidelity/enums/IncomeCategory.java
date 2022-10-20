package com.fidelity.enums;


public enum IncomeCategory {
	BASIC("T0K_T20K"),
	LOW("T20K_T40K"),
	MEDIUM("T40K_T60K"),
	HIGH("T60K_T80K"),
	EXTREME("T80K_TPLUS");

	
	private String code;

	private IncomeCategory(String code) {
		this.code = code;
	}

	public static IncomeCategory of(String code) {
		for (IncomeCategory ct: values()) {
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
