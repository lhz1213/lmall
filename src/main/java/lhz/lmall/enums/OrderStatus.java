package lhz.lmall.enums;

import lombok.Getter;

@Getter
public enum OrderStatus {
	WAIT_PAY(0, "待付款"), WAIT_DELIVERY(1, "待发货"), WAIT_CONFIRM(2, "待收货"), WAIT_REVIEW(3, "待评价"), FINISH(4, "完成"),
	DELETE(99, "删除");
	private Integer statusCode;
	private String statusDesc;

	private OrderStatus(Integer statusCode, String statusDesc) {
		this.statusCode = statusCode;
		this.statusDesc = statusDesc;
	}

}
