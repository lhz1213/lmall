package lhz.lmall.controller.admin;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lhz.lmall.entity.Category;
import lhz.lmall.enums.ImageSize;
import lhz.lmall.enums.ImageType;
import lhz.lmall.exception.MyException;
import lhz.lmall.service.CategoryService;
import lhz.lmall.util.ImageUtil;
import lhz.lmall.vo.MyPage;

/**
 * @author lhz
 * 
 * 
 * 
 *
 */
@RestController

public class CategoryController {

	@Autowired
	private CategoryService categoryService;

	@GetMapping("/categories/{id}")
	public Category get(@PathVariable("id") Integer id) {
		return categoryService.get(id);
	}

	@GetMapping("/categories")
	public MyPage<Category> list(@RequestParam(value = "start", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "5") Integer size) {

		return categoryService.list(page, size);
	}

	@Transactional(rollbackFor = Exception.class)
	@PostMapping("/categories")
	public Category add(Category category, MultipartFile image) throws IOException {

		categoryService.add(category);
		ImageUtil.save(category, image, ImageSize.CATEGORY_MIDDLE.getSize(), ImageType.CATEGORY_IMAGE.getType());
		return category;
	}

	@DeleteMapping("/categories/{id}")
	public String delete(@PathVariable("id") Integer id) throws MyException {
		try {
			categoryService.delete(id);
		} catch (Exception e) {
			// e.printStackTrace();
			throw new MyException("请先清空当前分类下的内容");
		}
		ImageUtil.delete(id, ImageType.CATEGORY_IMAGE.getType());
		return null;

	}

	@Transactional(rollbackFor = Exception.class)
	@PutMapping("/categories/{id}")
	public Category update(Category category, MultipartFile image) throws IOException {
		categoryService.update(category);
		ImageUtil.save(category, image, ImageSize.CATEGORY_MIDDLE.getSize(), ImageType.CATEGORY_IMAGE.getType());
		return category;
	}

}
