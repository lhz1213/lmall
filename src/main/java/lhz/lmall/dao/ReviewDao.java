package lhz.lmall.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import lhz.lmall.entity.Product;
import lhz.lmall.entity.Review;

public interface ReviewDao extends JpaRepository<Review, Integer> {

	List<Review> findByProductOrderByIdDesc(Product product);

	Integer countByProduct(Product product);

}
