package lhz.lmall.controller.fore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import lhz.lmall.entity.Category;
import lhz.lmall.entity.Product;
import lhz.lmall.entity.ProductImage;
import lhz.lmall.entity.PropertyValue;
import lhz.lmall.entity.Review;
import lhz.lmall.service.CategoryService;
import lhz.lmall.service.ProductImageService;
import lhz.lmall.service.ProductService;
import lhz.lmall.service.PropertyValueService;
import lhz.lmall.service.ReviewService;
import lhz.lmall.util.ProductComparator;

@RestController
public class GoodsController {

	@Autowired
	private ProductService productService;
	@Autowired
	private ProductImageService productImageService;
	@Autowired
	private PropertyValueService propertyValueService;
	@Autowired
	private ReviewService reviewService;
	@Autowired
	private CategoryService categoryService;

	@GetMapping("/foreproduct/{pid}")
	public Map<String, Object> product(@PathVariable("pid") int pid) {
		Product product = productService.get(pid);

		List<ProductImage> productSingleImages = productImageService.listSingleProductImages(product);
		List<ProductImage> productDetailImages = productImageService.listDetailProductImages(product);
		product.setProductSingleImages(productSingleImages);
		product.setProductDetailImages(productDetailImages);

		List<PropertyValue> pvs = propertyValueService.list(product);
		List<Review> reviews = reviewService.list(product);
		productService.setSaleAndReviewNumber(product);
		productImageService.setFirstProductImage(product);

		Map<String, Object> map = new HashMap<>();
		map.put("product", product);
		map.put("pvs", pvs);
		map.put("reviews", reviews);
		/*
		 * ResultVo vo = new ResultVo(); vo.setObj(map);
		 */

		return map;
	}

	@GetMapping("/forecategory/{cid}")
	public Category category(@PathVariable("cid") Integer cid, String sort) {
		Category c = categoryService.get(cid);
		productService.fill(c);
		productService.setSaleAndReviewNumber(c.getProducts());
		categoryService.removeCategoryFromProduct(c);
		ProductComparator.sort(c.getProducts(), sort);
		return c;
	}

	@PostMapping("/foresearch")
	public List<Product> search(String keyword) {
		if (null == keyword) {
			keyword = "";
		}
		List<Product> ps = productService.search(keyword, 0, 12);
		productImageService.setFirstProdutImages(ps);
		productService.setSaleAndReviewNumber(ps);
		return ps;
	}

}
