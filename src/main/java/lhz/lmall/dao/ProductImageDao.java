package lhz.lmall.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import lhz.lmall.entity.Product;
import lhz.lmall.entity.ProductImage;

public interface ProductImageDao extends JpaRepository<ProductImage, Integer> {
	public List<ProductImage> findByProductAndTypeOrderByIdDesc(Product product, String type);

	public List<ProductImage> findByProductInAndTypeOrderByIdDesc(List<Product> ps, String type);
}
