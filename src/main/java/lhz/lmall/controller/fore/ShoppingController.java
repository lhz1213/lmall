package lhz.lmall.controller.fore;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import lhz.lmall.entity.Order;
import lhz.lmall.entity.OrderItem;
import lhz.lmall.entity.Product;
import lhz.lmall.entity.Review;
import lhz.lmall.entity.User;
import lhz.lmall.enums.OrderStatus;
import lhz.lmall.enums.Results;
import lhz.lmall.service.OrderItemService;
import lhz.lmall.service.OrderService;
import lhz.lmall.service.ProductImageService;
import lhz.lmall.service.ProductService;
import lhz.lmall.service.ReviewService;
import lhz.lmall.vo.ResultVo;

@RestController
public class ShoppingController {

	@Autowired
	private OrderItemService orderItemService;
	@Autowired
	private ProductService productService;
	@Autowired
	private ProductImageService productImageService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private ReviewService reviewService;

	/**
	 * 立即购买，插入订单项数据
	 * 
	 * 
	 * 
	 */
	@GetMapping("/forebuyone")
	public Integer buyone(Integer pid, Integer num, HttpSession session) {
		Product product = productService.get(pid);
		Integer oiid = 0;
		User user = (User) session.getAttribute("user");
		boolean found = false;
		List<OrderItem> ois = orderItemService.listByUser(user);

		for (OrderItem oi : ois) {
			if (oi.getProduct().getId().equals(pid)) {
				oi.setNumber(oi.getNumber() + num);
				orderItemService.update(oi);
				found = true;
				oiid = oi.getId();
				break;
			}
		}

		if (!found) {
			OrderItem oi = new OrderItem();
			oi.setUser(user);
			oi.setProduct(product);
			oi.setNumber(num);
			orderItemService.add(oi);
			oiid = oi.getId();
		}
		return oiid;
	}

	/**
	 * 获取订单项，准备生成订单
	 * 
	 * 
	 */
	@GetMapping("/forebuy")
	public Map<String, Object> buy(String[] oiid, HttpSession session) {
		List<OrderItem> orderItems = new ArrayList<>();
		BigDecimal total = new BigDecimal(0);
		User user = (User) session.getAttribute("user");
		Integer uid = user.getId();
		List<Integer> intoiids = new ArrayList<>();
		List<OrderItem> ois = new ArrayList<>();

		for (String strid : oiid) {
			intoiids.add(Integer.parseInt(strid));
		}
		ois = orderItemService.findByIds(intoiids);
		for (OrderItem oi : ois) {
			if (oi.getUser().getId().equals(uid)) {
				total = oi.getProduct().getPromotePrice().multiply(new BigDecimal(oi.getNumber())).add(total);
				orderItems.add(oi);
			}
		}
		productImageService.setFirstProdutImagesOnOrderItems(orderItems);
		session.setAttribute("ois", orderItems);
		Map<String, Object> map = new HashMap<>();
		map.put("orderItems", orderItems);
		map.put("total", total);
		return map;
	}

	/**
	 * 加入购物车
	 * 
	 * 
	 * 
	 */
	@GetMapping("/foreaddCart")
	public ResultVo addCart(Integer pid, Integer num, HttpSession session) {
		buyone(pid, num, session);
		ResultVo vo = new ResultVo();
		vo.setCode(Results.SUCCESS_CODE.getCode());
		return vo;
	}

	/**
	 * 查看购物车
	 * 
	 */
	@GetMapping("/forecart")
	public List<OrderItem> cart(HttpSession session) {
		User user = (User) session.getAttribute("user");
		List<OrderItem> ois = orderItemService.listByUser(user);
		productImageService.setFirstProdutImagesOnOrderItems(ois);
		return ois;
	}

	/**
	 * 调整购买个数
	 * 
	 * 
	 * 
	 */
	@GetMapping("/forechangeOrderItem")
	public ResultVo changeOrderItem(HttpSession session, Integer pid, Integer num) {
		User user = (User) session.getAttribute("user");
		ResultVo vo = new ResultVo();
		List<OrderItem> ois = orderItemService.listByUser(user);
		for (OrderItem oi : ois) {
			if (oi.getUser().getId().equals(user.getId())
					&& (null == oi.getOrder() || (oi.getOrder().getStatus() == OrderStatus.WAIT_PAY.getStatusCode()))) {
				if (oi.getProduct().getId().equals(pid)) {
					oi.setNumber(num);
					orderItemService.update(oi);
					break;
				}
			}
		}
		vo.setCode(Results.SUCCESS_CODE.getCode());
		return vo;
	}

	@GetMapping("/foredeleteOrderItem")
	public ResultVo deleteOrderItem(Integer oiid, HttpSession session) {
		ResultVo vo = new ResultVo();
		User user = (User) session.getAttribute("user");
		OrderItem oi = orderItemService.get(oiid);
		if (oi.getUser().getId().equals(user.getId())
				&& (null == oi.getOrder() || (oi.getOrder().getStatus() == OrderStatus.WAIT_PAY.getStatusCode()))) {
			orderItemService.delete(oiid);
		}
		vo.setCode(Results.SUCCESS_CODE.getCode());
		return vo;
	}

	/**
	 * 待付款
	 * 
	 * 
	 */
	@PostMapping("/forecreateOrder")
	public Map<String, Object> createOrder(@Valid @RequestBody Order order, HttpSession session,
			BindingResult bindingResult) {
		Map<String, Object> map = new HashMap<>();
		if (bindingResult.hasErrors()) {
			map.put("msg", bindingResult.getFieldError().getDefaultMessage());
		} else {
			User user = (User) session.getAttribute("user");

			String orderCode = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date())
					+ RandomUtils.nextInt(0, 10000);
			order.setOrderCode(orderCode);
			order.setCreateDate(new Date());
			order.setUser(user);
			order.setStatus(OrderStatus.WAIT_PAY.getStatusCode());
			@SuppressWarnings("unchecked")
			List<OrderItem> ois = (List<OrderItem>) session.getAttribute("ois");
			BigDecimal total = orderService.add(order, ois);

			map.put("oid", order.getId());
			map.put("total", total);
		}

		return map;
	}

	@GetMapping("/forepayed")
	public ResultVo payed(Integer oid, HttpSession session) {
		User user = (User) session.getAttribute("user");
		Order order = orderService.get(oid);
		ResultVo vo = new ResultVo();
		if (order.getUser().getId().equals(user.getId())
				&& order.getStatus().equals(OrderStatus.WAIT_PAY.getStatusCode())) {
			order.setStatus(OrderStatus.WAIT_DELIVERY.getStatusCode());
			order.setPayDate(new Date());
			orderService.update(order);
			vo.setObj(order);
		} else {
			vo.setMsg("操作失败");
			vo.setCode(Results.FAIL_CODE.getCode());
		}
		return vo;
	}

	/**
	 * 查看购物车
	 * 
	 */
	@GetMapping("/forebought")
	public List<Order> bought(HttpSession session) {
		User user = (User) session.getAttribute("user");
		List<Order> os = orderService.listByUserWithoutDelete(user);
		orderService.removeOrderFromOrderItem(os);
		return os;
	}

	@GetMapping("/foreconfirmPay")
	public Order confirmPay(Integer oid) {
		Order o = orderService.get(oid);
		orderItemService.fill(o);
		orderService.removeOrderFromOrderItem(o);
		return o;
	}

	@GetMapping("/foreorderConfirmed")
	public ResultVo orderConfirmed(Integer oid, HttpSession session) {
		User user = (User) session.getAttribute("user");
		Order o = orderService.get(oid);
		ResultVo vo = new ResultVo();
		if (o.getUser().getId().equals(user.getId())
				&& o.getStatus().equals(OrderStatus.WAIT_CONFIRM.getStatusCode())) {
			o.setStatus(OrderStatus.WAIT_REVIEW.getStatusCode());
			o.setConfirmDate(new Date());
			orderService.update(o);
			vo.setCode(Results.SUCCESS_CODE.getCode());
		} else {
			vo.setMsg("操作失败");
		}
		return vo;
	}

	@PutMapping("/foredeleteOrder")
	public ResultVo deleteOrder(Integer oid, HttpSession session) {
		User user = (User) session.getAttribute("user");
		Order o = orderService.get(oid);
		ResultVo vo = new ResultVo();
		if (o.getUser().getId().equals(user.getId())) {
			o.setStatus(OrderStatus.DELETE.getStatusCode());
			orderService.update(o);
			vo.setCode(Results.SUCCESS_CODE.getCode());
		} else {
			vo.setCode(Results.FAIL_CODE.getCode());
			vo.setMsg("操作失败");
		}
		return vo;
	}

	@GetMapping("/forereview")
	public ResultVo review(Integer oid, HttpSession session) {
		User user = (User) session.getAttribute("user");
		Order o = orderService.get(oid);
		ResultVo vo = new ResultVo();
		if (o.getUser().getId().equals(user.getId())) {
			orderItemService.fill(o);
			orderService.removeOrderFromOrderItem(o);
			Product p = o.getOrderItems().get(0).getProduct();
			List<Review> reviews = reviewService.list(p);
			productService.setSaleAndReviewNumber(p);
			Map<String, Object> map = new HashMap<>();
			map.put("p", p);
			map.put("o", o);
			map.put("reviews", reviews);
			vo.setObj(map);
		} else {
			vo.setCode(Results.FAIL_CODE.getCode());
			vo.setMsg("操作失败");
		}

		return vo;
	}

	@PostMapping("/foredoreview")
	public ResultVo doreview(HttpSession session, Integer oid, Integer pid, String content) {
		User user = (User) session.getAttribute("user");
		Order o = orderService.get(oid);
		ResultVo vo = new ResultVo();
		if (o.getUser().getId().equals(user.getId()) && o.getStatus().equals(OrderStatus.WAIT_REVIEW.getStatusCode())) {

			o.setStatus(OrderStatus.FINISH.getStatusCode());
			orderService.update(o);

			Product p = productService.get(pid);
			content = HtmlUtils.htmlEscape(content);

			Review review = new Review();
			review.setContent(content);
			review.setProduct(p);
			review.setCreateDate(new Date());
			review.setUser(user);
			reviewService.add(review);
		} else {
			vo.setCode(Results.FAIL_CODE.getCode());
			vo.setMsg("操作失败");
		}
		return vo;
	}

}
