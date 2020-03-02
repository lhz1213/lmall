package lhz.lmall.enums;

import lombok.Getter;

@Getter
public enum Results {
	SUCCESS_CODE(0), FAIL_CODE(1);
	private Integer code;

	private Results(Integer code) {
		this.code = code;
	}

}
