package lhz.lmall.enums;

import lombok.Getter;

@Getter
public enum ImageType {

	CATEGORY_IMAGE("category"), SINGLE_IMAGE("single"), DETAIL_IMAGE("detail"), SINGLE_PRODUCT_IMAGE("productSingle"),
	DETAIL_PRODUCT_IMAGE("productDetail"), SMALL_SINGLE_PRODUCT_IMAGE("productSingle_small"),
	MIDDLE_SINGLE_PRODUCT_IMAGE("productSingle_middle")

	;
	private String type;

	private ImageType(String type) {
		this.type = type;
	}

}
