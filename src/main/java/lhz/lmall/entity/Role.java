package lhz.lmall.entity;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@Entity
@Table(name = "role")
@JsonIgnoreProperties({ "handler", "hibernateLazyInitializer" })

public class Role {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String name;
	private String des;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "role_permission", joinColumns = { @JoinColumn(name = "rid") }, inverseJoinColumns = {
			@JoinColumn(name = "pid") })
	private Set<Permission> permissions;

}
