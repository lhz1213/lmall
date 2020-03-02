package lhz.lmall.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lhz.lmall.dao.UserDao;
import lhz.lmall.entity.User;
import lhz.lmall.util.PageUtil;
import lhz.lmall.vo.MyPage;

@Service

public class UserService {
	@Autowired
	private UserDao userDao;

	public MyPage<User> list(Integer page, Integer size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
		Page<User> Jp = userDao.findAll(pageable);
		MyPage<User> mp = PageUtil.converter(Jp);
		return mp;
	}

	public boolean isExist(String name) {
		User user = getByName(name);
		return null != user;
	}

	public User getByName(String name) {
		return userDao.findByName(name);
	}

	@Transactional
	public void add(User user) {
		userDao.save(user);
	}

	public User get(String name, String password) {
		return userDao.findByNameAndPassword(name, password);
	}

}
