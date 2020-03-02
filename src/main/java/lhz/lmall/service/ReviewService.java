package lhz.lmall.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lhz.lmall.dao.ReviewDao;
import lhz.lmall.entity.Product;
import lhz.lmall.entity.Review;

@Service

public class ReviewService {
	@Autowired
	private ReviewDao reviewDao;

	@Transactional

	public void add(Review review) {
		reviewDao.save(review);
	}

	public List<Review> list(Product product) {
		List<Review> result = reviewDao.findByProductOrderByIdDesc(product);
		return result;
	}

	public int getCount(Product product) {
		return reviewDao.countByProduct(product);
	}

}
