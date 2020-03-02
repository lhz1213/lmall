package lhz.lmall.controller.admin;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lhz.lmall.entity.Product;
import lhz.lmall.entity.ProductImage;
import lhz.lmall.enums.ImageSize;
import lhz.lmall.enums.ImageType;
import lhz.lmall.service.ProductImageService;
import lhz.lmall.service.ProductService;
import lhz.lmall.util.ImageUtil;

@RestController
public class ProductImageController {
	@Autowired
	private ProductService productService;
	@Autowired
	private ProductImageService productImageService;

	@GetMapping("/products/{pid}/productImages")
	public List<ProductImage> list(@RequestParam("type") String type, @PathVariable("pid") Integer pid) {
		Product product = productService.get(pid);

		if (ImageType.SINGLE_IMAGE.getType().equals(type)) {
			List<ProductImage> singles = productImageService.listSingleProductImages(product);
			return singles;
		} else {
			List<ProductImage> details = productImageService.listDetailProductImages(product);
			return details;
		}
	}

	@Transactional(rollbackFor = Exception.class)
	@PostMapping("/productImages")
	public ProductImage add(@RequestParam("pid") int pid, @RequestParam("type") String type, MultipartFile image)
			throws IOException {
		Product product = productService.get(pid);
		ProductImage productImage = new ProductImage();
		productImage.setProduct(product);
		productImage.setType(type);
		productImageService.add(productImage);
		if (ImageType.SINGLE_IMAGE.getType().equals(type)) {
			ImageUtil.save(productImage, image, ImageSize.PRODUCT_BIG.getSize(),
					ImageType.SINGLE_PRODUCT_IMAGE.getType());
			ImageUtil.save(productImage, image, ImageSize.PRODUCT_MIDDLE.getSize(),
					ImageType.MIDDLE_SINGLE_PRODUCT_IMAGE.getType());
			ImageUtil.save(productImage, image, ImageSize.PRODUCT_SMALL.getSize(),
					ImageType.SMALL_SINGLE_PRODUCT_IMAGE.getType());
		} else {
			ImageUtil.save(productImage, image, ImageSize.PRODUCT_BIG.getSize(),
					ImageType.DETAIL_PRODUCT_IMAGE.getType());
		}
		return productImage;
	}

	@DeleteMapping("/productImages/{id}")
	public String delete(@PathVariable("id") Integer id) {
		ProductImage productImage = productImageService.get(id);
		if (ImageType.SINGLE_IMAGE.getType().equals(productImage.getType())) {
			ImageUtil.delete(id, ImageType.SINGLE_PRODUCT_IMAGE.getType());
			ImageUtil.delete(id, ImageType.SMALL_SINGLE_PRODUCT_IMAGE.getType());
			ImageUtil.delete(id, ImageType.MIDDLE_SINGLE_PRODUCT_IMAGE.getType());
		} else {
			ImageUtil.delete(id, ImageType.DETAIL_PRODUCT_IMAGE.getType());
		}
		productImageService.delete(id);
		return null;

	}

}
