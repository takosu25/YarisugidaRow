package yrsg;

public class YrsgSyusi {
	private int plus = 0;
	private String cause = "";

	public YrsgSyusi(int plus,String cause) {
		SetPlus(plus);
		SetCause(cause);
	}

	void SetPlus(int newplus){
		plus = newplus;
	}

	void SetCause(String cause) {
		this.cause = cause;
	}

	int GetPlus() {
		return plus;
	}

	String GetCause() {
		return cause;
	}
}
