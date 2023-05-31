package pt.uc.dei.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the Attachment database table.
 * 
 */
@Entity
@NamedQueries({
@NamedQuery(name="Attachment.findAll", query="SELECT a FROM Attachment a"),
@NamedQuery(name="Attachment.findByIdActivity", query="SELECT a FROM Attachment a WHERE a.activityHistoric.activity.idActivity=:idActivity")
})
public class Attachment implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int idattachment;

	@Lob
	private byte[] attachmentFile;

	//bi-directional many-to-one association to Activity_historic
	@ManyToOne
	@JoinColumn(name="activity_historic_idhistoricactivity")
	private Activity_historic activityHistoric;

	private String mimefile;

	private String namefile;


	public Attachment() {
	}

	public int getIdattachment() {
		return this.idattachment;
	}

	public void setIdattachment(int idattachment) {
		this.idattachment = idattachment;
	}

	public byte[] getAttachmentFile() {
		return this.attachmentFile;
	}

	public void setAttachmentFile(byte[] attachmentFile) {
		this.attachmentFile = attachmentFile;
	}

	public Activity_historic getActivityHistoric() {
		return this.activityHistoric;
	}

	public void setActivityHistoric(Activity_historic activityHistoric) {
		this.activityHistoric = activityHistoric;
	}

	/**
	 * @return the mimefile
	 */
	public String getMimefile() {
		return mimefile;
	}

	/**
	 * @param mimefile the mimefile to set
	 */
	public void setMimefile(String mimefile) {
		this.mimefile = mimefile;
	}

	/**
	 * @return the namefile
	 */
	public String getNamefile() {
		return namefile;
	}

	/**
	 * @param namefile the namefile to set
	 */
	public void setNamefile(String namefile) {
		this.namefile = namefile;
	}

}