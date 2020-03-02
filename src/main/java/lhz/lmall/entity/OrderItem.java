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
@Table(name = "orderitem")
@JsonIgnoreProperties({ "handler", "hibernateLazyInitializer" })
public class OrderItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "pid")
	private Product product;

	@ManyToOne
	@JoinColumn(name = "oid")
	// @JsonBackReference
	private Order order;

	@ManyToOne
	@JoinColumn(name = "uid")
	private User user;

	private Integer number;

}
