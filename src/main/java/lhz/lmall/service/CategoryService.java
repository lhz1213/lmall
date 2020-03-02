package lhz.lmall.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lhz.lmall.dao.CategoryDao;
import lhz.lmall.entity.Category;
import lhz.lmall.entity.Product;
import lhz.lmall.util.PageUtil;
import lhz.lmall.vo.MyPage;

@Service
@CacheConfig(cacheNames = "categories")
public class CategoryService {
	@Autowired
	private CategoryDao categoryDao;

	/**
	 * 增删改查
	 */
	@Cacheable(key = "'categories-all'")
	public List<Category> list() {
		return categoryDao.findAll(Sort.by(Sort.Direction.DESC, "id"));
	}

	@Cacheable(key = "'categories-page-'+#p0+ '-' + #p1")
	public MyPage<Category> list(Integer page, Integer size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
		Page<Category> Jp = categoryDao.findAll(pageable);
		MyPage<Category> mp = PageUtil.converter(Jp);
		return mp;
	}

	@Transactional
	@CacheEvict(allEntries = true)
	public void add(Category category) {
		categoryDao.save(category);
	}

	@Transactional
	@CacheEvict(allEntries = true)
	public void delete(Integer id) {
		categoryDao.deleteById(id);
	}

	// @Cacheable(key = "'categories-one-'+ #p0")
	public Category get(Integer id) {
		return categoryDao.getOne(id);
	}

	@Transactional
	@CacheEvict(allEntries = true)
	public void update(Category category) {
		categoryDao.save(category);
	}

	public void removeCategoryFromProduct(List<Category> cs) {
		for (Category category : cs) {
			removeCategoryFromProduct(category);
		}
	}

	public void removeCategoryFromProduct(Category category) {
		List<Product> products = category.getProducts();
		if (null != products) {
			products.forEach(p -> p.setCategory(null));
		}
		List<List<Product>> productsByRow = category.getProductsByRow();
		if (null != productsByRow) {
			productsByRow.forEach(ps -> ps.forEach(p -> p.setCategory(null)));
		}
	}

}
