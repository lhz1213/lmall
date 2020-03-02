package lhz.lmall.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lhz.lmall.entity.Product;
import lhz.lmall.entity.PropertyValue;
import lhz.lmall.service.ProductService;
import lhz.lmall.service.PropertyValueService;

@RestController
public class PropertyValueController {

	@Autowired
	private PropertyValueService propertyValueService;
	@Autowired
	private ProductService productService;

	@GetMapping("/products/{pid}/propertyValues")
	public List<PropertyValue> list(@PathVariable("pid") Integer pid) {
		Product product = productService.get(pid);
		propertyValueService.init(product);
		List<PropertyValue> propertyValues = propertyValueService.list(product);
		return propertyValues;
	}

	@PutMapping("/propertyValues")
	public Object update(@RequestBody PropertyValue propertyValue) {
		propertyValueService.update(propertyValue);
		return propertyValue;
	}

}
