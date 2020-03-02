package lhz.lmall.es;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import lhz.lmall.entity.Product;

public interface ProductEsDao extends ElasticsearchRepository<Product, Integer> {

}
