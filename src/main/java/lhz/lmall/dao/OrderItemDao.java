package lhz.lmall.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import lhz.lmall.entity.Order;
import lhz.lmall.entity.OrderItem;
import lhz.lmall.entity.Product;
import lhz.lmall.entity.User;

public interface OrderItemDao extends JpaRepository<OrderItem, Integer> {
	List<OrderItem> findByOrderOrderByIdDesc(Order order);

	List<OrderItem> findByUserAndOrderIsNull(User user);

	List<OrderItem> findByProduct(Product product);
}
