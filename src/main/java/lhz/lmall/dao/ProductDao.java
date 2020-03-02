package lhz.lmall.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import lhz.lmall.entity.Category;
import lhz.lmall.entity.Product;

public interface ProductDao extends JpaRepository<Product, Integer> {
	Page<Product> findByCategory(Category category, Pageable pageable);

	List<Product> findByNameLike(String keyword, Pageable pageable);

	List<Product> findByCategoryOrderById(Category category);

	List<Product> findByCategoryInOrderById(List<Category> categorys);
}
