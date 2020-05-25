package be;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "example", uniqueConstraints = @UniqueConstraint(columnNames = { "val" }))
public class Example {

	@Id
	@GeneratedValue
	private Long id;

	private String val;

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public String getVal() {
		return val;
	}

	public void setVal(final String val) {
		this.val = val;
	}
}
