package lhz.lmall.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.collections.IteratorUtils;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import lhz.lmall.dao.ProductDao;
import lhz.lmall.entity.Category;
import lhz.lmall.entity.Product;
import lhz.lmall.es.ProductEsDao;
import lhz.lmall.util.PageUtil;
import lhz.lmall.vo.MyPage;

@Service
@CacheConfig(cacheNames = "products")
public class ProductService {
	@Autowired
	private ProductDao productDao;
	@Autowired
	private ProductEsDao productEsDao;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private ProductImageService productImageService;
	@Autowired
	private OrderItemService orderItemService;
	@Autowired
	private ReviewService reviewService;

	public MyPage<Product> list(Integer cid, Integer page, Integer size) {
		Category category = categoryService.get(cid);
		page = page < 0 ? 0 : page;
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
		Page<Product> Jp = productDao.findByCategory(category, pageable);
		MyPage<Product> mp = PageUtil.converter(Jp);
		return mp;
	}

	public List<Product> listByCategory(Category category) {
		return productDao.findByCategoryOrderById(category);
	}

	@Cacheable(key = "'products-list-'+ #root.target.getKey(#p0)")
	public List<Product> listByCategorys(List<Category> categorys) {
		return productDao.findByCategoryInOrderById(categorys);
	}

	public String getKey(List<Category> categorys) {
		List<Integer> list = new ArrayList<>();
		categorys.forEach(c -> list.add(c.getId()));
		return list.stream().distinct().sorted().map(Integer::toUnsignedString).collect(Collectors.joining());
	}

	@Transactional
	@CacheEvict(allEntries = true)
	public void add(Product product) {
		Product p = productDao.save(product);
		productEsDao.save(p);
	}

	@Transactional
	@CacheEvict(allEntries = true)
	public void delete(Integer id) {
		productDao.deleteById(id);
		productEsDao.deleteById(id);
	}

	public Product get(Integer id) {
		Product p = productDao.getOne(id);
		return p;
	}

	@Transactional
	@CacheEvict(allEntries = true)
	public void update(Product product) {
		productDao.save(product);
		productEsDao.save(product);
	}

	@Transactional
	@CacheEvict(allEntries = true)
	public void update(List<Product> ps) {
		productDao.saveAll(ps);
	}

	/**
	 * 填充商品
	 */

	public void fill(List<Category> categorys) {

		List<Product> products = listByCategorys(categorys);
		productImageService.setFirstProdutImages(products);
		for (Category c : categorys) {
			List<Product> ps = new ArrayList<>();
			for (Product p : products) {
				if (p.getCategory().equals(c)) {
					ps.add(p);
				}
			}
			c.setProducts(ps);
		}

		// categorys.forEach(c -> fill(c));
	}

	public void fill(Category category) {

		List<Product> products = listByCategory(category);
		productImageService.setFirstProdutImages(products);
		category.setProducts(products);
	}

	/**
	 * 分批填充
	 */
	public void fillByRow(List<Category> categorys) {
		int productNumberEachRow = 8;
		for (Category category : categorys) {
			List<Product> products = category.getProducts();
			List<List<Product>> productsByRow = new ArrayList<>();
			for (int i = 0; i < products.size(); i += productNumberEachRow) {
				int size = i + productNumberEachRow;
				size = size > products.size() ? products.size() : size;
				List<Product> productsOfEachRow = products.subList(i, size);
				productsByRow.add(productsOfEachRow);
			}
			category.setProductsByRow(productsByRow);
		}
	}

	/**
	 * 设置销量 评论数
	 */
	public void setSaleAndReviewNumber(Product product) {
		Integer saleCount = orderItemService.getSaleCount(product);
		product.setSaleCount(saleCount);

		Integer reviewCount = reviewService.getCount(product);
		product.setReviewCount(reviewCount);

	}

	public void setSaleAndReviewNumber(List<Product> products) {
		products.forEach(p -> setSaleAndReviewNumber(p));

	}

	/**
	 * 搜索
	 * 
	 * 
	 * 
	 */
	public List<Product> search(String keyword, Integer page, Integer size) {
		page = page < 0 ? 0 : page;
		keyword = keyword == null ? "" : keyword;
		initDatabase2ES();
		QueryBuilder qb = QueryBuilders.matchPhraseQuery("name", keyword);
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
		SearchQuery sq = new NativeSearchQueryBuilder().withPageable(pageable).withQuery(qb).build();
		Page<Product> search = productEsDao.search(sq);
		// List<Product> products = productDao.findByNameLike("%" + keyword + "%",
		// pageable);
		return search.getContent();
	}

	private void initDatabase2ES() {

		Iterable<Product> findAll = productEsDao.findAll();
		@SuppressWarnings("unchecked")
		List<Product> ps = IteratorUtils.toList(findAll.iterator());
		if (ps.isEmpty()) {
			List<Product> products = productDao.findAll();
			productEsDao.saveAll(products);
		}
	}

}
