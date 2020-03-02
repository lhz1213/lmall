package lhz.lmall.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

/**
 * @author lhz
 * 
 * 
 *
 */
@Entity
@Table(name = "category")
@JsonIgnoreProperties({ "handler", "hibernateLazyInitializer" })
@Data
public class Category {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String name;

	@Transient
	List<Product> products;
	@Transient
	List<List<Product>> productsByRow;

}
