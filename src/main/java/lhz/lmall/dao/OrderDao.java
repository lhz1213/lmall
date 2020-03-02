package lhz.lmall.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import lhz.lmall.entity.Order;
import lhz.lmall.entity.User;

public interface OrderDao extends JpaRepository<Order, Integer> {
	public List<Order> findByUserAndStatusNotOrderByIdDesc(User user, Integer status);
}
