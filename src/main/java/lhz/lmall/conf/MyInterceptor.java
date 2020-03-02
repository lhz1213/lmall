package lhz.lmall.conf;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import lhz.lmall.entity.Category;
import lhz.lmall.entity.OrderItem;
import lhz.lmall.entity.User;
import lhz.lmall.service.CategoryService;
import lhz.lmall.service.OrderItemService;

public class MyInterceptor implements HandlerInterceptor {

	@Autowired
	private CategoryService categoryService;
	@Autowired
	private OrderItemService orderItemService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		return true;
		/*
		 * String[] requiredAuthPages = new String[] { "buy", "alipay", "payed", "cart",
		 * "bought", "confirmPay", "orderConfirmed", "review", "admin",
		 * 
		 * "forebuyone", "forebuy", "foreaddCart", "forecart", "forechangeOrderItem",
		 * "foredeleteOrderItem", "forecreateOrder", "forepayed", "forebought",
		 * "foreconfirmPay", "foreorderConfirmed", "foredeleteOrder", "forereview",
		 * "foredoreview"
		 * 
		 * }; HttpSession session = request.getSession(); String contextPath =
		 * request.getServletContext().getContextPath(); String uri =
		 * request.getRequestURI(); uri = StringUtils.remove(uri, contextPath + "/");
		 * 
		 * boolean result = false; for (String requiredAuthPage : requiredAuthPages) {
		 * if (StringUtils.startsWith(uri, requiredAuthPage)) { result = true; break; }
		 * }
		 * 
		 * if (result) { User user = (User) session.getAttribute("user"); if (user ==
		 * null) { response.sendRedirect("/lmall/login"); return false; } else if
		 * (user.getName() != "admin" && StringUtils.startsWith(uri, "admin")) {
		 * response.sendRedirect("/lmall/home"); return false; } } return true;
		 */
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

		HttpSession session = request.getSession();
		if (null == request.getServletContext().getAttribute("categories_below_search")) {
			List<Category> cs = categoryService.list();
			request.getServletContext().setAttribute("categories_below_search", cs);
		}

		if (null == request.getServletContext().getAttribute("contextPath")) {
			String contextPath = request.getServletContext().getContextPath();
			request.getServletContext().setAttribute("contextPath", contextPath);
		}

		User user = (User) session.getAttribute("user");
		if (null != user) {
			int cartTotalItemNumber = 0;
			List<OrderItem> ois = orderItemService.listByUser(user);
			cartTotalItemNumber = ois.size();
			session.setAttribute("cartTotalItemNumber", cartTotalItemNumber);
		}

	}

}
