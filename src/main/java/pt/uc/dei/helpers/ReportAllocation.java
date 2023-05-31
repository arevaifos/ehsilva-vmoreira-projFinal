package pt.uc.dei.helpers;

import java.util.Date;

public class ReportAllocation {
	
	private int idAllocation;
	
	private Date begindateAllocation;
	
	private Date enddateAllocation;
	
	private float percentage;

	private String codigoProject;
	
	private String emailUser;

	private int idActivity;	
	
	private String nameActivity;
	
	public ReportAllocation() {
		super();
	}

	public ReportAllocation(int idAllocation, Date begindateAllocation, Date enddateAllocation, float percentage,
			String codigoProject, String emailUser, int idActivity, String nameActivity) {
		super();
		this.idAllocation = idAllocation;
		this.begindateAllocation = begindateAllocation;
		this.enddateAllocation = enddateAllocation;
		this.percentage = percentage;
		this.codigoProject = codigoProject;
		this.emailUser = emailUser;
		this.idActivity = idActivity;
		this.nameActivity = nameActivity;
	}

	public int getIdAllocation() {
		return idAllocation;
	}

	public void setIdAllocation(int idAllocation) {
		this.idAllocation = idAllocation;
	}

	public Date getBegindateAllocation() {
		return begindateAllocation;
	}

	public void setBegindateAllocation(Date begindateAllocation) {
		this.begindateAllocation = begindateAllocation;
	}

	public Date getEnddateAllocation() {
		return enddateAllocation;
	}

	public void setEnddateAllocation(Date enddateAllocation) {
		this.enddateAllocation = enddateAllocation;
	}

	public float getPercentage() {
		return percentage;
	}

	public void setPercentage(float percentage) {
		this.percentage = percentage;
	}

	public String getCodigoProject() {
		return codigoProject;
	}

	public void setCodigoProject(String codigoProject) {
		this.codigoProject = codigoProject;
	}

	public String getEmailUser() {
		return emailUser;
	}

	public void setEmailUser(String emailUser) {
		this.emailUser = emailUser;
	}

	public int getIdActivity() {
		return idActivity;
	}

	public void setIdActivity(int idActivity) {
		this.idActivity = idActivity;
	}

	public String getNameActivity() {
		return nameActivity;
	}

	public void setNameActivity(String nameActivity) {
		this.nameActivity = nameActivity;
	}

	@Override
	public String toString() {
		return "ReportAllocation [idAllocation=" + idAllocation + ", begindateAllocation=" + begindateAllocation
				+ ", enddateAllocation=" + enddateAllocation + ", percentage=" + percentage + ", codigoProject="
				+ codigoProject + ", emailUser=" + emailUser + ", idActivity=" + idActivity + ", nameActivity="
				+ nameActivity + "]";
	}


	//String input = "[\"user1\",\"track1\",\"player1\", \"user1\",\"track2\",\"player2\", \"user1\",\"track3\",\"player3\"]";
	

public String toStringCabecalho() {
		return "[ idAllocation, begindateAllocation, enddateAllocation, percentage, codigoProject, emailUser, idActivity, nameActivity;]";
	}

public String toStringCorpo() {
	return "["+idAllocation+", "+begindateAllocation +", "+enddateAllocation+", "+percentage+", "+codigoProject+", "+emailUser+", "+idActivity+", "+nameActivity+";]";
}

	

	




}
