package pt.uc.dei.backingbeans;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;

import pt.uc.dei.helpers.CSVGenerator;
import pt.uc.dei.implement.UserImpl;
import pt.uc.dei.language.Internationalization;
import pt.uc.dei.model.User;

@Named("login")
@RequestScoped
public class Login {

	final static Logger logger = Logger.getLogger(Login.class);
	@Inject
	private UserImpl userImpl;
	@Inject
	private Internationalization internationalization;

	private String inputEmail;
	private String inputPassword;


	public Login() {
		super();
	}

	public String doLogin(){
		
		String ePattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
		java.util.regex.Matcher m = p.matcher(inputEmail);
		
		FacesMessage message;
		if (inputEmail.trim().equals("") || inputEmail.equals(null) || m.matches() == false) {
			message = new FacesMessage(FacesMessage.SEVERITY_INFO, getBundle("USERERROR011"), ""); 
			FacesContext.getCurrentInstance().addMessage("", message);
			return "";		
		}	
		ePattern = "^[a-zA-Z0-9]+$";
		p = java.util.regex.Pattern.compile(ePattern);
		m = p.matcher(inputPassword);

		if (inputEmail.trim().equals("") || inputEmail.equals(null) || m.matches() == false) {
			message = new FacesMessage(FacesMessage.SEVERITY_INFO, getBundle("USERERROR012"), ""); 
			FacesContext.getCurrentInstance().addMessage("", message);
			return "";		
		}
		
		User userLogged=userImpl.isRegistered(inputEmail,inputPassword,false);
		if(userLogged!=null){
			if(userLogged.getIsactive()&& !userImpl.isVisitorUser() && !userImpl.isOnlyAdmin()){
				logger.info("LOGIN USER: "+ userLogged.getEmail());
				return "pages/beginArea.xhtml?faces-redirect=true";
			}else if(userLogged.getIsactive() && userImpl.isOnlyAdmin()){
					logger.info("LOGIN USER: "+ userLogged.getEmail());
					return "pages/adminArea.xhtml?faces-redirect=true";
			}else{
				message = new FacesMessage(FacesMessage.SEVERITY_INFO, getBundle("USERERROR013")+ " " + inputEmail, "");
				FacesContext.getCurrentInstance().addMessage("", message);
				
				return "pages/visitorArea.xhtml?faces-redirect=true";
			}
			
		}else{
			message = new FacesMessage(FacesMessage.SEVERITY_INFO, getBundle("USERERROR013"), "");
			FacesContext.getCurrentInstance().addMessage("", message);
			return "";
		}			
	}
	
	private String getBundle(String key) {
		return internationalization.getResourceBundle().getString(key);
	}


	public String doLogout() {
		userImpl.logout();
		return "/index.xhtml?faces-redirect=true";
	}
	public String getInputEmail() {
		return inputEmail;
	}

	public void setInputEmail(String inputEmail) {
		this.inputEmail = inputEmail;
	}

	public String getInputPassword() {
		return inputPassword;
	}

	public void setInputPassword(String inputPassword) {
		this.inputPassword = inputPassword;
	}
}
