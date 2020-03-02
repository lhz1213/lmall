package lhz.lmall.util;

import java.util.Comparator;
import java.util.List;

import lhz.lmall.entity.Product;

public class ProductComparator {

	private ProductComparator() {
		super();
		// TODO Auto-generated constructor stub
	}

	public static void sort(List<Product> p, String sort) {
		if (null != sort) {
			switch (sort) {
			case "review":
				p.sort(Comparator.comparingInt(Product::getReviewCount).reversed());
				break;
			case "date":
				p.sort(Comparator.comparing(Product::getCreateDate).reversed());
				break;
			case "saleCount":
				p.sort(Comparator.comparingInt(Product::getSaleCount).reversed());
				break;
			case "price":
				p.sort(Comparator.comparing(Product::getPromotePrice));

			case "all":
				p.sort(Comparator.comparingInt(Product::getSaleCount).reversed()
						.thenComparingInt(Product::getReviewCount).reversed().thenComparing(Product::getPromotePrice)
						.thenComparing(Product::getCreateDate).reversed());
				break;
			}
		}

	}

}
