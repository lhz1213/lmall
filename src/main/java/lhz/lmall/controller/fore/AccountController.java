package lhz.lmall.controller.fore;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lhz.lmall.entity.User;
import lhz.lmall.enums.Results;
import lhz.lmall.service.UserService;
import lhz.lmall.vo.ResultVo;

@RestController
public class AccountController {

	@Autowired
	private UserService userService;

	@PostMapping("/foreregister")
	public ResultVo register(@Valid @RequestBody User user, BindingResult bindingResult) {
		ResultVo vo = new ResultVo();
		if (userService.isExist(user.getName())) {
			vo.setMsg("用户名已经被使用,不能使用");
			return vo;
		} else if (bindingResult.hasErrors()) {
			vo.setMsg(bindingResult.getFieldError().getDefaultMessage());
			return vo;
		} else {
			String salt = new SecureRandomNumberGenerator().nextBytes().toString();
			String encodedPassword = new SimpleHash("md5", user.getPassword(), salt, 2).toString();
			user.setSalt(salt);
			user.setPassword(encodedPassword);

			userService.add(user);
			vo.setCode(Results.SUCCESS_CODE.getCode());
			return vo;
		}
	}

	@PostMapping("/forelogin")
	public ResultVo login(@RequestBody User userParam, HttpSession session) {
		ResultVo vo = new ResultVo();
		Subject subject = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(userParam.getName(), userParam.getPassword());
		try {
			subject.login(token);
			User user = userService.getByName(userParam.getName());
			session.setAttribute("user", user);
			vo.setCode(Results.SUCCESS_CODE.getCode());
			return vo;
		} catch (AuthenticationException e) {
			vo.setMsg("账号密码错误");
			return vo;
		}
	}

	@GetMapping("/forecheckLogin")
	public ResultVo checkLogin(HttpSession session) {
		User user = (User) session.getAttribute("user");
		ResultVo vo = new ResultVo();
		if (null != user) {
			vo.setCode(Results.SUCCESS_CODE.getCode());
			return vo;
		} else {
			vo.setMsg("未登录");
			return vo;
		}
	}
}
