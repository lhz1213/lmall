package lhz.lmall.controller.fore;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lhz.lmall.entity.Category;
import lhz.lmall.service.CategoryService;
import lhz.lmall.service.ProductService;

@RestController
public class ForeController {

	@Autowired
	private CategoryService categoryService;
	@Autowired
	private ProductService productService;

	@GetMapping("/forehome")
	public List<Category> home() {
		List<Category> cs = categoryService.list();
		productService.fill(cs);
		productService.fillByRow(cs);
		categoryService.removeCategoryFromProduct(cs);

		return cs;
	}

}
