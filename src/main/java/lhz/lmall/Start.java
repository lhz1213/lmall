package lhz.lmall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author lhz
 * 
 * 
 *
 */
@SpringBootApplication
@EnableCaching
@EnableJpaRepositories(basePackages = "lhz.lmall.dao")
@EnableElasticsearchRepositories(basePackages = "lhz.lmall.es")
public class Start {

	public static void main(String[] args) {
		SpringApplication.run(Start.class, args);

	}

}
