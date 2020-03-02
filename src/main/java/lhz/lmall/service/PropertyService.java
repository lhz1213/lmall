package lhz.lmall.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lhz.lmall.dao.PropertyDao;
import lhz.lmall.entity.Category;
import lhz.lmall.entity.Property;
import lhz.lmall.util.PageUtil;
import lhz.lmall.vo.MyPage;

@Service

public class PropertyService {
	@Autowired
	private PropertyDao propertyDao;
	@Autowired
	private CategoryService categoryService;

	public List<Property> list(Category category) {
		return propertyDao.findByCategory(category);
	}

	public MyPage<Property> list(Integer cid, Integer page, Integer size) {
		Category category = categoryService.get(cid);
		page = page < 0 ? 0 : page;
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
		Page<Property> Jp = propertyDao.findByCategory(category, pageable);
		MyPage<Property> mp = PageUtil.converter(Jp);
		return mp;
	}

	@Transactional

	public void add(Property property) {
		propertyDao.save(property);
	}

	@Transactional

	public void delete(Integer id) {
		propertyDao.deleteById(id);
	}

	public Property get(Integer id) {
		return propertyDao.getOne(id);
	}

	@Transactional

	public void update(Property property) {
		propertyDao.save(property);
	}

}
