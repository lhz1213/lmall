package lhz.lmall.service;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lhz.lmall.dao.OrderItemDao;
import lhz.lmall.entity.Order;
import lhz.lmall.entity.OrderItem;
import lhz.lmall.entity.Product;
import lhz.lmall.entity.User;

@Service
public class OrderItemService {
	@Autowired
	private OrderItemDao orderItemDao;
	@Autowired
	private ProductImageService productImageService;

	/**
	 * 填充订单 用于显示
	 */
	public void fill(List<Order> orders) {
		orders.forEach(o -> fill(o));
	}

	public void fill(Order order) {

		List<OrderItem> orderItems = listByOrder(order);
		BigDecimal total = new BigDecimal(0);
		int totalNumber = 0;
		for (OrderItem oi : orderItems) {
			total = total.add(oi.getProduct().getPromotePrice().multiply(new BigDecimal(oi.getNumber())));
			totalNumber += oi.getNumber();
			productImageService.setFirstProductImage(oi.getProduct());
		}
		order.setTotal(total);
		order.setOrderItems(orderItems);
		order.setTotalNumber(totalNumber);
	}

	@Transactional
	public void update(OrderItem orderItem) {
		orderItemDao.save(orderItem);
	}

	@Transactional
	public void update(List<OrderItem> ois) {
		orderItemDao.saveAll(ois);
	}

	@Transactional
	public void add(OrderItem orderItem) {
		orderItemDao.save(orderItem);
	}

	public OrderItem get(Integer id) {
		return orderItemDao.getOne(id);
	}

	public List<OrderItem> findByIds(List<Integer> ids) {
		return orderItemDao.findAllById(ids);
	}

	@Transactional
	public void delete(Integer id) {
		orderItemDao.deleteById(id);
	}

	public List<OrderItem> listByProduct(Product product) {
		return orderItemDao.findByProduct(product);
	}

	public List<OrderItem> listByOrder(Order order) {
		return orderItemDao.findByOrderOrderByIdDesc(order);
	}

	public List<OrderItem> listByUser(User user) {
		return orderItemDao.findByUserAndOrderIsNull(user);
	}

	/**
	 * 产品销量
	 * 
	 */
	public Integer getSaleCount(Product product) {

		List<OrderItem> ois = listByProduct(product);
		Integer result = 0;
		for (OrderItem oi : ois) {
			if (null != oi.getOrder() && null != oi.getOrder().getPayDate())
				result += oi.getNumber();
		}
		return result;
	}

}
