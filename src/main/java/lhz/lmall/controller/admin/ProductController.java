package lhz.lmall.controller.admin;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lhz.lmall.entity.Product;
import lhz.lmall.service.ProductImageService;
import lhz.lmall.service.ProductService;
import lhz.lmall.vo.MyPage;

@RestController
public class ProductController {

	@Autowired
	private ProductService productService;
	@Autowired
	private ProductImageService productImageService;

	@GetMapping("/categories/{cid}/products")
	public MyPage<Product> list(@PathVariable("cid") Integer cid,
			@RequestParam(value = "start", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "10") Integer size) {
		MyPage<Product> myPage = productService.list(cid, page, size);
		productImageService.setFirstProdutImages(myPage.getList());
		return myPage;

	}

	@GetMapping("/products/{id}")
	public Product get(@PathVariable("id") Integer id) {
		Product product = productService.get(id);
		return product;
	}

	@PostMapping("/products")
	public Product add(@RequestBody Product product) {
		product.setCreateDate(new Date());
		productService.add(product);
		return product;
	}

	@DeleteMapping("/products/{id}")
	public String delete(@PathVariable("id") Integer id) {
		productService.delete(id);
		return null;
	}

	@PutMapping("/products")
	public Product update(@RequestBody Product product) {
		productService.update(product);
		return product;
	}

}
