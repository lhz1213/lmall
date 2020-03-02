package lhz.lmall.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import lhz.lmall.entity.Category;

public interface CategoryDao extends JpaRepository<Category, Integer> {

}
