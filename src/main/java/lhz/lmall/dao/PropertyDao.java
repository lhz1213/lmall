package lhz.lmall.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import lhz.lmall.entity.Category;
import lhz.lmall.entity.Property;

public interface PropertyDao extends JpaRepository<Property, Integer> {

	Page<Property> findByCategory(Category category, Pageable pageable);

	List<Property> findByCategory(Category category);

}
