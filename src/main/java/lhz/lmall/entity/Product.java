package lhz.lmall.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "product")
@Document(indexName = "lmall", type = "product")
@JsonIgnoreProperties({ "handler", "hibernateLazyInitializer" })
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "cid")
	private Category category;

	@Field(type = FieldType.Text, analyzer = "ik_max_word")
	private String name;
	private String subTitle;
	private BigDecimal originalPrice;
	private BigDecimal promotePrice;
	private Integer stock;
	private Date createDate;
	@Transient
	private ProductImage firstProductImage;
	@Transient
	private List<ProductImage> productSingleImages;
	@Transient
	private List<ProductImage> productDetailImages;
	@Transient
	private Integer reviewCount;
	@Transient
	private Integer saleCount;
}
