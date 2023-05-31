package pt.uc.dei.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the Holidays database table.
 * 
 */
@Entity
@Table(name="Holidays")
@NamedQuery(name="Holiday.findAll", query="SELECT h FROM Holiday h")
public class Holiday implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Temporal(TemporalType.DATE)
	private Date dateHoliday;

	private String description;

	private boolean isAnual;

	public Holiday() {
	}

	public Date getDateHoliday() {
		return this.dateHoliday;
	}

	public void setDateHoliday(Date dateHoliday) {
		this.dateHoliday = dateHoliday;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean getIsAnual() {
		return this.isAnual;
	}

	public void setIsAnual(boolean isAnual) {
		this.isAnual = isAnual;
	}

}