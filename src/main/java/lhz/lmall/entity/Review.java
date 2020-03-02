package lhz.lmall.entity;

import java.util.Date;

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
@Table(name = "review")
@JsonIgnoreProperties({ "handler", "hibernateLazyInitializer" })
public class Review {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "uid")
	private User user;

	@ManyToOne
	@JoinColumn(name = "pid")
	private Product product;

	private String content;
	private Date createDate;

}
