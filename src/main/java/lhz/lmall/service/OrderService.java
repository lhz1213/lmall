package lhz.lmall.service;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lhz.lmall.dao.OrderDao;
import lhz.lmall.entity.Order;
import lhz.lmall.entity.OrderItem;
import lhz.lmall.entity.User;
import lhz.lmall.enums.OrderStatus;
import lhz.lmall.util.PageUtil;
import lhz.lmall.vo.MyPage;

@Service

public class OrderService {

	@Autowired
	private OrderDao orderDao;
	@Autowired
	private OrderItemService orderItemService;

	public MyPage<Order> list(Integer page, Integer size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
		Page<Order> Jp = orderDao.findAll(pageable);
		Jp.forEach(o -> o.setStatusDesc());
		MyPage<Order> mp = PageUtil.converter(Jp);
		return mp;
	}

	public List<Order> listByUserAndNotDeleted(User user) {
		return orderDao.findByUserAndStatusNotOrderByIdDesc(user, OrderStatus.DELETE.getStatusCode());
	}

	public List<Order> listByUserWithoutDelete(User user) {
		List<Order> orders = listByUserAndNotDeleted(user);
		orderItemService.fill(orders);
		return orders;
	}

	public void removeOrderFromOrderItem(List<Order> orders) {
		orders.forEach(o -> removeOrderFromOrderItem(o));
	}

	public void removeOrderFromOrderItem(Order order) {
		List<OrderItem> orderItems = order.getOrderItems();
		orderItems.forEach(o -> o.setOrder(null));
	}

	public Order get(int oid) {
		return orderDao.getOne(oid);
	}

	@Transactional

	public void update(Order bean) {
		orderDao.save(bean);
	}

	@Transactional

	public void add(Order order) {
		orderDao.save(order);
	}

	/**
	 * 创建订单
	 * 
	 * 
	 */
	@Transactional

	public BigDecimal add(Order order, List<OrderItem> ois) {
		BigDecimal total = new BigDecimal(0);
		add(order);
		for (OrderItem oi : ois) {
			oi.setOrder(order);
			total = oi.getProduct().getPromotePrice().multiply(new BigDecimal(oi.getNumber())).add(total);
		}
		orderItemService.update(ois);
		return total;
	}

}
