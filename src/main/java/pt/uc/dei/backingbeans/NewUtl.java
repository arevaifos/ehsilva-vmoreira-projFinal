package pt.uc.dei.backingbeans;

import java.io.IOException;
import java.io.Serializable;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Utils;
import javax.servlet.http.Part;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import pt.uc.dei.implement.NewUserImpl;
import pt.uc.dei.implement.UserImpl;
import pt.uc.dei.language.Internationalization;

@Named("newUtl")
@SessionScoped
public class NewUtl implements Serializable {

	private static final long serialVersionUID = 1L;

	final static Logger logger = Logger.getLogger(NewUtl.class);

	@Inject
	private Internationalization internationalization;

	private int randomNum;

	@Inject
	private NewUserImpl newUserImpl;

	@Inject
	private UserImpl userImpl;

	// @RequestScoped
	// Can be @ViewScoped, but caution should be taken with byte[] property. You
	// don't want to save it in session.
	private Part fileImage;
	private byte[] byteImage;

	private String userName;
	private String userEmail;
	private String userPws1;
	private String userPws2;
	private String token;

	NewUtl() {

	}

	public void guardaEditUser() {

		String msg = newUserImpl.guardaEditUser(userName, userPws1, userPws2, byteImage);

		if (msg == null) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, getBundle("INFO_05"), "");
			FacesContext.getCurrentInstance().addMessage("", message);
			limpaDados();
			// return "index.xhtml";
		} else {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, "");
			FacesContext.getCurrentInstance().addMessage("", message);
			// return "";
		}

	}

	@PostConstruct
	public void getLoggedUser() {
		if (userImpl.getLoggedUser() == null) {
			limpaDados();
		} else {

			userName = userImpl.getLoggedUser().getName();
			byteImage = userImpl.getLoggedUser().getPhoto();
			userPws1 = "";
			userPws2 = "";
		}
	}

	public void guardaNovaConta() {
		String msg = newUserImpl.createNewUser(userName, userEmail, userPws1, userPws2, byteImage);
		if (msg == null) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, getBundle("INFO_06"), "");
			FacesContext.getCurrentInstance().addMessage("", message);
			limpaDados();
			// return "index.xhtml";
		} else {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, "");
			FacesContext.getCurrentInstance().addMessage("", message);
			// return "";
		}

	}

	private void limpaDados() {
		fileImage = null;
		byteImage = null;
		userName = null;
		userEmail = null;
		userPws1 = null;
		userPws2 = null;
		token = null;
		randomNum = 0;
	}

	////////////////////////////////////////////
	public void readImagem() throws IOException {
		if (fileImage != null) {
			byteImage = Utils.toByteArray(fileImage.getInputStream());
		}
		Ajax.update("formFoto");

	}

	public void sendEmailAgain() {
		String msg = newUserImpl.sendEmailAgain( );
		if (msg == null) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, getBundle("INFO_13"), "");
			FacesContext.getCurrentInstance().addMessage("", message);
			limpaDados();
			// return "index.xhtml";
		} else {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, "");
			FacesContext.getCurrentInstance().addMessage("", message);
			// return "";
		}
	}

	/////////////// janela e-mail para recuperar password
	/////////////// /////////////////////////////
	public void pedeNovaPassword() {

		String msg = newUserImpl.pedeNovaPassword(userEmail);
		if (msg == null) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, getBundle("INFO_07"), "");
			FacesContext.getCurrentInstance().addMessage("", message);
			limpaDados();
		} else {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, "");
			FacesContext.getCurrentInstance().addMessage("", message);
		}
	}

	public void criaNovaPassword() {
		String msg = newUserImpl.criaNovaPassword(randomNum, token, userPws1, userPws2);
		if (msg == null) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, getBundle("INFO_08"), "");
			FacesContext.getCurrentInstance().addMessage("", message);
			limpaDados();
			// return "index.xhtml";
		} else {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, "");
			FacesContext.getCurrentInstance().addMessage("", message);
			// return "";
		}
	}

	/**
	 * get message fron internationalization
	 * 
	 * @param key
	 *            String
	 * @return
	 */
	private String getBundle(String key) {
		return internationalization.getResourceBundle().getString(key);
	}

	/////////////// get's e set's /////////////////////////////
	public Part getFileImage() {
		return fileImage;
	}

	public void setFileImage(Part file) {
		this.fileImage = file;
	}

	public byte[] getByteImage() {
		return byteImage;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the userEmail
	 */
	public String getUserEmail() {
		return userEmail;
	}

	/**
	 * @param userEmail
	 *            the userEmail to set
	 */
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	/**
	 * @return the userPws1
	 */
	public String getUserPws1() {
		return userPws1;
	}

	/**
	 * @param userPws1
	 *            the userPws1 to set
	 */
	public void setUserPws1(String userPws1) {
		this.userPws1 = userPws1;
	}

	/**
	 * @return the userPws2
	 */
	public String getUserPws2() {
		return userPws2;
	}

	/**
	 * @param userPws2
	 *            the userPws2 to set
	 */
	public void setUserPws2(String userPws2) {
		this.userPws2 = userPws2;

	}

	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * @param token
	 *            the token to set
	 */
	public void setToken(String token) {
		this.token = token;
	}

	public int getRandomNum() {
		return randomNum;
	}

	public void setRandomNum(int randomNum) {
		this.randomNum = randomNum;
	}

}
