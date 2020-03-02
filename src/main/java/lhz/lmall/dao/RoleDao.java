package lhz.lmall.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import lhz.lmall.entity.Role;

public interface RoleDao extends JpaRepository<Role, Integer> {

}
