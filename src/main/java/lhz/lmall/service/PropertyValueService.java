package lhz.lmall.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lhz.lmall.dao.PropertyValueDao;
import lhz.lmall.entity.Product;
import lhz.lmall.entity.Property;
import lhz.lmall.entity.PropertyValue;

@Service

public class PropertyValueService {

	@Autowired
	private PropertyValueDao propertyValueDao;
	@Autowired
	private PropertyService propertyService;

	@Transactional

	public void update(PropertyValue propertyValue) {
		propertyValueDao.save(propertyValue);
	}

	@Transactional

	public void init(Product product) {
		List<Property> propertys = propertyService.list(product.getCategory());
		for (Property property : propertys) {
			PropertyValue propertyValue = findByPropertyAndProduct(product, property);
			if (null == propertyValue) {
				propertyValue = new PropertyValue();
				propertyValue.setProduct(product);
				propertyValue.setProperty(property);
				propertyValueDao.save(propertyValue);
			}
		}
	}

	public PropertyValue findByPropertyAndProduct(Product product, Property property) {
		return propertyValueDao.findByPropertyAndProduct(property, product);
	}

	public List<PropertyValue> list(Product product) {
		return propertyValueDao.findByProductOrderByIdDesc(product);
	}

}
