package lhz.lmall.controller.admin;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lhz.lmall.entity.Order;
import lhz.lmall.entity.OrderItem;
import lhz.lmall.entity.Product;
import lhz.lmall.enums.OrderStatus;
import lhz.lmall.enums.Results;
import lhz.lmall.service.OrderItemService;
import lhz.lmall.service.OrderService;
import lhz.lmall.service.ProductService;
import lhz.lmall.vo.MyPage;
import lhz.lmall.vo.ResultVo;

@RestController
public class OrderController {
	@Autowired
	private OrderService orderService;
	@Autowired
	private OrderItemService orderItemService;
	@Autowired
	private ProductService productService;

	@GetMapping("/orders")
	public MyPage<Order> list(@RequestParam(value = "start", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "5") Integer size) {

		MyPage<Order> pages = orderService.list(page, size);
		orderItemService.fill(pages.getList());
		orderService.removeOrderFromOrderItem(pages.getList());
		return pages;
	}

	@PutMapping("deliveryOrder/{oid}")
	public ResultVo deliveryOrder(@PathVariable Integer oid) {
		Order o = orderService.get(oid);
		o.setDeliveryDate(new Date());
		o.setStatus(OrderStatus.WAIT_CONFIRM.getStatusCode());
		o.setStatusDesc();
		orderService.update(o);

		int stock = 0;
		List<OrderItem> ois = orderItemService.listByOrder(o);
		List<Product> ps = new ArrayList<>();
		Product product = new Product();
		for (OrderItem orderItem : ois) {
			product = orderItem.getProduct();
			stock = product.getStock();
			if (stock > 0) {
				product.setStock(product.getStock() - orderItem.getNumber());
			}
			ps.add(product);
		}
		productService.update(ps);
		ResultVo resultVo = new ResultVo();
		resultVo.setCode(Results.SUCCESS_CODE.getCode());
		return resultVo;
	}

}
