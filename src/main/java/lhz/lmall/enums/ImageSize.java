package lhz.lmall.enums;

import lombok.Getter;

@Getter
public enum ImageSize {
	CATEGORY_SMALL(200), CATEGORY_MIDDLE(400), CATEGORY_BIG(600), PRODUCT_SMALL(60), PRODUCT_MIDDLE(200),
	PRODUCT_BIG(400),;

	private Integer size;

	private ImageSize(Integer size) {
		this.size = size;
	}

}
