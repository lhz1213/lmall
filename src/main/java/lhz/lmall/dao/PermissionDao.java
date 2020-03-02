package lhz.lmall.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import lhz.lmall.entity.Permission;

public interface PermissionDao extends JpaRepository<Permission, Integer> {

}
