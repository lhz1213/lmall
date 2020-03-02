package lhz.lmall.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import lhz.lmall.entity.Product;
import lhz.lmall.entity.Property;
import lhz.lmall.entity.PropertyValue;

public interface PropertyValueDao extends JpaRepository<PropertyValue, Integer> {

	List<PropertyValue> findByProductOrderByIdDesc(Product product);

	PropertyValue findByPropertyAndProduct(Property property, Product product);
}
