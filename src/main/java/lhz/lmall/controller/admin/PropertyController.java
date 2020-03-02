package lhz.lmall.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lhz.lmall.entity.Property;
import lhz.lmall.service.PropertyService;
import lhz.lmall.vo.MyPage;

@RestController
public class PropertyController {

	@Autowired
	private PropertyService propertyService;

	@GetMapping("/categories/{cid}/properties")
	public MyPage<Property> list(@PathVariable("cid") Integer cid,
			@RequestParam(value = "start", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "5") Integer size) {
		return propertyService.list(cid, page, size);
	}

	@GetMapping("/properties/{id}")
	public Property get(@PathVariable("id") Integer id) {
		Property property = propertyService.get(id);
		return property;
	}

	@PostMapping("/properties")
	public Property add(@RequestBody Property property) {
		propertyService.add(property);
		return property;
	}

	@DeleteMapping("/properties/{id}")
	public String delete(@PathVariable("id") Integer id) {
		propertyService.delete(id);
		return null;
	}

	@PutMapping("/properties")
	public Property update(@RequestBody Property property) {
		propertyService.update(property);
		return property;
	}

}
