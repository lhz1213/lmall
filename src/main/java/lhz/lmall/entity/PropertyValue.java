package lhz.lmall.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@Entity
@Table(name = "propertyvalue")
@JsonIgnoreProperties({ "handler", "hibernateLazyInitializer" })
public class PropertyValue {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "pid")

	private Product product;
	@ManyToOne
	@JoinColumn(name = "ptid")
	private Property property;

	private String value;
}
