package pt.uc.dei.implement;

import java.io.Serializable;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.jboss.resteasy.util.Base64;

import pt.uc.dei.dao.UserDao;
import pt.uc.dei.language.Internationalization;
import pt.uc.dei.model.Project;
import pt.uc.dei.model.Role;
import pt.uc.dei.model.User;

/**
 * @author cnest
 *
 */
@Named("userImpl")
@SessionScoped
public class UserImpl implements Serializable {

	private static final long serialVersionUID = 1L;

	final static Logger logger = Logger.getLogger(UserImpl.class);
	private String pepper = "wdqerztyuiocp0o9i87ujyh6gt5rfedsx";// verificar se
	// nao deve ser
	// estatica por
	// ser unica
	@Inject
	private UserDao userDao;
	@Inject
	private ProjectImpl projectImpl;

	@Inject
	private Internationalization internationalization;

	private User loggedUser = null;
	private List<Role> rolesLoggedUser = new ArrayList<>();
	private boolean adminUser = false;
	private boolean directorUser = false;
	private boolean userUser = false;
	private boolean visitorUser = false;
	private boolean gestorUser = false;
	private boolean onlyAdmin=false;
	private boolean begin=true;
	private boolean flagLogin;

	public UserImpl() {
		flagLogin = true;
	}

	public String onload() {
		String newLocale = internationalization.getLocale();
		for (Map.Entry<String, Object> entries : internationalization.getCountries().entrySet()) {
			if (entries.getValue().toString().equals(newLocale)) {
				FacesContext.getCurrentInstance().getViewRoot().setLocale((Locale) entries.getValue());
			}
		}
		return "";
	}

	/**
	 * METODO PARA FAZER LOGIN
	 * 
	 * @param emailLogin
	 * @param passwordLogin
	 * @return
	 */
	public User isRegistered(String emailLogin, String passwordLogin, boolean isGoogleLogin) {
		try {

			User user = null;
			if (!isGoogleLogin) {
				String passwordLoginEncript = encriptPass(emailLogin, passwordLogin);
				user = userDao.findByLogin(emailLogin, passwordLoginEncript);
			} else {
				user = userDao.findByEmail(emailLogin);
			}

			if (user != null) {
				user.setRoles(userDao.findListRoles(user.getIduser()));	

				for(int i=0; i<user.getRoles().size();i++){

					switch (user.getRoles().get(i).getRole()) {
					case "Administrador":
						adminUser = true;
						break;
					case "Diretor":
						directorUser = true;
						break;
					case "Utilizador":
						userUser = true;
						break;
					case "Visitante":
						visitorUser = true;
						break;
					default:
						break;
					}

				}
				if(adminUser){
					if(!userUser && !visitorUser && !directorUser){
						onlyAdmin=true;
						begin=false;
					}
				}
				if(visitorUser){
					if(!userUser && !adminUser && !directorUser){
						begin=false;
					}
				}
				
				user.setPassword(null);
				user.setSalt(null);
				loggedUser = user;

				logger.info("LOGIN User: " + loggedUser.getEmail() + " " + new Date());
			}
			return user;
		} catch (Exception e) {
			logger.error(getBundle("USERERROR015"));
			logger.error(e.getMessage());
			return null;
		}
	}

	/**
	 * METODO PARA FAZER LOGOUT
	 * 
	 * @return
	 */
	public boolean logout() {
		try {
			FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
			logger.info("LOGOUT User: " + loggedUser.getEmail() + " " + new Date());
			loggedUser = null;
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("LOGOUT: " + loggedUser.getName() + new Date());
			logger.error(e);
			return false;
		}
	}

	public String encriptaPsw(String email, String password) {
		return encriptPass(email, password);
	}

	private String encriptPass(String email, String password) {

		String passwordSaltPepper = addSaltPepper(email, password);
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			logger.error(e);
		}
		byte[] passwordBytes = passwordSaltPepper.getBytes();
		byte[] hash = md.digest(passwordBytes);
		String hashedPassword = Base64.encodeBytes(hash);

		return hashedPassword;
	}

	/**
	 * Método que junta o salt, com a password e com a pepper;
	 * 
	 * Recebe email no Login e salt no newUser *
	 * 
	 * @return String
	 */

	private String addSaltPepper(String tmp, String password) {
		String salt = null;
		if (flagLogin) {
			salt = userDao.findSalt(tmp);
		} else {
			salt = tmp;
		}
		String saltPasswordPepper = new String();
		if (salt != null) {
			saltPasswordPepper = salt + password + pepper;
		}
		return saltPasswordPepper;
	}

	public String getPepper() {
		return pepper;
	}

	public void setPepper(String pepper) {
		this.pepper = pepper;
	}

	public User getLoggedUser() {
		return loggedUser;
	}

	public void setLoggedUser(User loggedUser) {
		this.loggedUser = loggedUser;
	}

	/**
	 * @param flagLogin
	 *            the flagLogin to set
	 */
	public void setFlagLogin(boolean flagLogin) {
		this.flagLogin = flagLogin;
	}

	/**
	 * METODO PARA SABER SE ESTÁ LOGADO QUE É UTILIZADO NO FILTRO.
	 * 
	 * @return
	 */
	public boolean isLogged() {
		return loggedUser != null;
	}

	public boolean isAdminUser() {
		return adminUser;
	}

	public void setAdminUser(boolean adminUser) {
		this.adminUser = adminUser;
	}

	public boolean isDirectorUser() {
		return directorUser;
	}

	public void setDirectorUser(boolean directorUser) {
		this.directorUser = directorUser;
	}

	public boolean isUserUser() {
		return userUser;
	}

	public void setUserUser(boolean userUser) {
		this.userUser = userUser;
	}

	public boolean isVisitorUser() {
		return visitorUser;
	}

	public void setVisitorUser(boolean visitorUser) {
		this.visitorUser = visitorUser;
	}

	public List<Role> getRolesLoggedUser() {
		return rolesLoggedUser;
	}

	public void setRolesLoggedUser(List<Role> rolesLoggedUser) {
		this.rolesLoggedUser = rolesLoggedUser;
	}

	public boolean isGestorUser() {
		List<Project> projectWhereIsManager = projectImpl.getAllProjectByManage(loggedUser.getIduser());
		if (projectWhereIsManager.size() > 0 && projectWhereIsManager != null && !projectWhereIsManager.isEmpty()) {
			gestorUser = true;
		} else {
			gestorUser = false;
		}
		return gestorUser;
	}

	public void setGestorUser(boolean gestorUser) {
		this.gestorUser = gestorUser;
	}

	private String getBundle(String key) {
		return internationalization.getResourceBundle().getString(key);
	}

	public boolean isOnlyAdmin() {
		return onlyAdmin;
	}

	public void setOnlyAdmin(boolean onlyAdmin) {
		this.onlyAdmin = onlyAdmin;
	}

	public boolean isBegin() {
		return begin;
	}

	public void setBegin(boolean begin) {
		this.begin = begin;
	}

	
	
}
