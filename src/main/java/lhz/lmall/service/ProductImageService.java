package lhz.lmall.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import lhz.lmall.dao.ProductImageDao;
import lhz.lmall.entity.OrderItem;
import lhz.lmall.entity.Product;
import lhz.lmall.entity.ProductImage;
import lhz.lmall.enums.ImageType;
import lhz.lmall.util.SpringContextUtil;

@Service
@CacheConfig(cacheNames = "productImages")
public class ProductImageService {

	@Autowired
	private ProductImageDao productImageDao;

	@Transactional
	@CacheEvict(allEntries = true)
	public void add(ProductImage productImage) {
		productImageDao.save(productImage);

	}

	@Transactional
	@CacheEvict(allEntries = true)
	public void delete(Integer id) {
		productImageDao.deleteById(id);
	}

	@Cacheable(key = "'productImages-one-'+ #p0")
	public ProductImage get(Integer id) {
		return productImageDao.getOne(id);
	}

	@Cacheable(key = "'productImages-single-pid-'+ #p0.id")
	public List<ProductImage> listSingleProductImages(Product product) {
		return productImageDao.findByProductAndTypeOrderByIdDesc(product, ImageType.SINGLE_IMAGE.getType());
	}

	@Cacheable(key = "'productImages-single-pids-'+ #root.target.getKey1(#p0)")
	public List<ProductImage> listSingleProductImages(List<Product> ps) {

		List<ProductImage> findByProductInAndTypeOrderByIdDesc = productImageDao.findByProductInAndTypeOrderByIdDesc(ps,
				ImageType.SINGLE_IMAGE.getType());
		findByProductInAndTypeOrderByIdDesc.forEach(p -> p.setPpid(p.getProduct().getId()));
		return findByProductInAndTypeOrderByIdDesc;

	}

	/**
	 * 缓存key
	 * 
	 */
	public String getKey1(List<Product> ps) {
		List<Integer> list = new ArrayList<>();
		ps.forEach(c -> list.add(c.getId()));
		return list.stream().distinct().sorted().map(Integer::toUnsignedString).collect(Collectors.joining());
	}

	@Cacheable(key = "'productImages-detail-pid-'+ #p0.id")
	public List<ProductImage> listDetailProductImages(Product product) {
		return productImageDao.findByProductAndTypeOrderByIdDesc(product, ImageType.DETAIL_IMAGE.getType());
	}

	@Cacheable(key = "'productImages-single-pids-'+ #root.target.getKey2(#p0)")
	public List<ProductImage> listDetailProductImages(List<Product> ps) {
		List<ProductImage> findByProductInAndTypeOrderByIdDesc = productImageDao.findByProductInAndTypeOrderByIdDesc(ps,
				ImageType.DETAIL_IMAGE.getType());
		findByProductInAndTypeOrderByIdDesc.forEach(p -> p.setPpid(p.getProduct().getId()));
		return findByProductInAndTypeOrderByIdDesc;
	}

	public String getKey2(List<Product> ps) {
		List<Integer> list = new ArrayList<>();
		ps.forEach(c -> list.add(c.getId()));
		return list.stream().distinct().sorted().map(Integer::toUnsignedString).collect(Collectors.joining());
	}

	/**
	 * 一张图片用于展示
	 */
	public void setFirstProductImage(Product product) {
		ProductImageService productImageService = SpringContextUtil.getBean(ProductImageService.class);
		List<ProductImage> singleImages = productImageService.listSingleProductImages(product);
		if (!singleImages.isEmpty()) {
			product.setFirstProductImage(singleImages.get(0));
		} else {
			product.setFirstProductImage(new ProductImage());
		}
	}

	public void setFirstProdutImages(List<Product> products) {
		ProductImageService productImageService = SpringContextUtil.getBean(ProductImageService.class);
		List<ProductImage> singleImages = productImageService.listSingleProductImages(products);
		for (Product product : products) {
			product.setFirstProductImage(new ProductImage());
			for (ProductImage productImage : singleImages) {
				if (productImage.getPpid().equals(product.getId())) {
					product.setFirstProductImage(productImage);
					break;
				}

			}
		}

		// products.forEach(p -> setFirstProductImage(p));
	}

	public void setFirstProdutImagesOnOrderItems(List<OrderItem> ois) {
		ProductImageService productImageService = SpringContextUtil.getBean(ProductImageService.class);
		List<Product> products = new ArrayList<>();
		ois.forEach(o -> products.add(o.getProduct()));
		productImageService.setFirstProdutImages(products);

		// ois.forEach(o -> setFirstProductImage(o.getProduct()));
	}

}
