package pt.uc.dei.model;

import java.math.BigDecimal;

public class UsersAllocations {
	
	private int idUser;
	
	private String name;
	
	private String email;
	
	private BigDecimal percentage;
	
	public UsersAllocations() {
		super();
	}

	public UsersAllocations(int idUser, String name, String email, BigDecimal percentage) {
		super();
		this.idUser = idUser;
		this.name = name;
		this.email = email;
		this.percentage = percentage;
	}

	public int getIdUser() {
		return idUser;
	}

	public void setIdUser(int idUser) {
		this.idUser = idUser;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public BigDecimal getPercentage() {
		return percentage;
	}

	public void setPercentage(BigDecimal percentage) {
		this.percentage = percentage;
	}

	@Override
	public String toString() {
		return "UsersAllocations [idUser=" + idUser + ", name=" + name + ", email=" + email + ", percentage="
				+ percentage + "]";
	}
	
	

}
