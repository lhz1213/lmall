package lhz.lmall.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lhz.lmall.entity.User;
import lhz.lmall.service.UserService;
import lhz.lmall.vo.MyPage;

@RestController
public class UserController {
	@Autowired
	private UserService userService;

	@GetMapping("/users")
	public MyPage<User> list(@RequestParam(value = "start", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "5") Integer size) {

		return userService.list(page, size);
	}

}
