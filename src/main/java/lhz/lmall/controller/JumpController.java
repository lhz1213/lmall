package lhz.lmall.controller;

import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author lhz
 * 
 *         控制页面跳转
 * 
 *
 */
@Controller
public class JumpController {

	/**
	 * 后台页面跳转
	 */
	@GetMapping("/admin")
	public String admin() {
		return "redirect:admin_category_list";
	}

	@GetMapping("/admin_category_list")
	public String listCategory() {
		return "admin/listCategory";
	}

	@GetMapping("/admin_category_edit")
	public String editCategory() {
		return "admin/editCategory";
	}

	@GetMapping("/admin_order_list")
	public String listOrder() {
		return "admin/listOrder";
	}

	@GetMapping("/admin_product_list")
	public String listProduct() {
		return "admin/listProduct";
	}

	@GetMapping("/admin_product_edit")
	public String editProduct() {
		return "admin/editProduct";
	}

	@GetMapping("/admin_productImage_list")
	public String listProductImage() {
		return "admin/listProductImage";
	}

	@GetMapping("/admin_property_list")
	public String listProperty() {
		return "admin/listProperty";
	}

	@GetMapping("/admin_property_edit")
	public String editProperty() {
		return "admin/editProperty";
	}

	@GetMapping("/admin_propertyValue_edit")
	public String editPropertyValue() {
		return "admin/editPropertyValue";
	}

	@GetMapping("/admin_user_list")
	public String listUser() {
		return "admin/listUser";
	}

	@GetMapping("/unauthorized")
	public String unauthorized() {
		return "admin/unauthorized";
	}

	/**
	 * 前台页面跳转
	 */
	@GetMapping("/")
	public String index() {
		return "redirect:home";
	}

	@GetMapping("/home")
	public String home() {
		return "fore/home";
	}

	@GetMapping("/register")
	public String register() {
		return "fore/register";
	}

	@GetMapping("/alipay")
	public String alipay() {
		return "fore/alipay";
	}

	@GetMapping("/bought")
	public String bought() {
		return "fore/bought";
	}

	@GetMapping("/buy")
	public String buy() {
		return "fore/buy";
	}

	@GetMapping("/cart")
	public String cart() {
		return "fore/cart";
	}

	@GetMapping("/category")
	public String category() {
		return "fore/category";
	}

	@GetMapping("/confirmPay")
	public String confirmPay() {
		return "fore/confirmPay";
	}

	@GetMapping("/login")
	public String login() {
		return "fore/login";
	}

	@GetMapping("/orderConfirmed")
	public String orderConfirmed() {
		return "fore/orderConfirmed";
	}

	@GetMapping("/payed")
	public String payed() {
		return "fore/payed";
	}

	@GetMapping("/product")
	public String product() {
		return "fore/product";
	}

	@GetMapping("/registerSuccess")
	public String registerSuccess() {
		return "fore/registerSuccess";
	}

	@GetMapping("/review")
	public String review() {
		return "fore/review";
	}

	@GetMapping("/search")
	public String searchResult() {
		return "fore/searched";
	}

	@GetMapping("/forelogout")
	public String logout(HttpSession session) {
		Subject subject = SecurityUtils.getSubject();
		if (subject.isAuthenticated()) {
			subject.logout();
		}
		return "redirect:home";
	}
}
