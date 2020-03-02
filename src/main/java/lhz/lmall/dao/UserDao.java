package lhz.lmall.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import lhz.lmall.entity.User;

public interface UserDao extends JpaRepository<User, Integer> {
	User findByName(String name);

	User findByNameAndPassword(String name, String password);
}
