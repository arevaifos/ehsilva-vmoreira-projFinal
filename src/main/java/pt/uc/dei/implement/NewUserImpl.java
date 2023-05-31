package pt.uc.dei.implement;

import java.io.Serializable;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.websocket.Session;

import org.apache.log4j.Logger;

import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.crypto.MacProvider;
import pt.uc.dei.backingbeans.Admin;
import pt.uc.dei.dao.FunctionDao;
import pt.uc.dei.dao.RoleDao;
import pt.uc.dei.dao.UserDao;
import pt.uc.dei.language.Internationalization;
import pt.uc.dei.model.Function;
import pt.uc.dei.model.Role;
import pt.uc.dei.model.User;

@Named
public class NewUserImpl implements Serializable {

	private static final long serialVersionUID = 1L;
	final static Logger logger = Logger.getLogger(NewUserImpl.class);

	private static ConcurrentHashMap<Integer, Key> keys = new ConcurrentHashMap<Integer, Key>();
	private int randomNum;

	@Inject
	private Internationalization internationalization;
	@Inject
	private UserDao userDao;
	@Inject
	private RoleDao roleDao;
	@Inject
	private FunctionDao functionDao;
	@Inject
	private SendEmail sendEmail;
	@Inject
	private UserImpl userImpl;

	public NewUserImpl() {

	}

	/////////// Registo de uma nova conta /////////////
	public String createNewUser(String userName, String userEmail, String userPws1, String userPws2, byte[] byteImage) {
		String messa = validateVar(userName, userEmail, userPws1, userPws2, 0);
		if (messa != null) {
			return messa;
		}

		User user = userDao.findByEmail(userEmail);
		if (user != null) {
			return getBundle("USERERROR004");
		}

		Function function = functionDao.findByDesignation("NA");
		if (function == null) {
			logger.error(getBundle("USERERROR005"));
			logger.debug("On get function NA");
			return getBundle("USERERROR005");
		}

		user = new User();

		user.setEmail(userEmail);
		user.setName(userName);
		user.setPhoto(byteImage);
		userImpl.setFlagLogin(false);
		user.setSalt(createSalt());
		user.setPassword(userImpl.encriptaPsw(user.getSalt(), userPws1));
		userImpl.setFlagLogin(true);
		user.setIsactive(false);
		user.setFunction(function);

		Role role = roleDao.findByRole("Visitante");

		if (role != null) {
			List<Role> roles = new ArrayList<Role>();
			roles.add(role);
			user.setRoles(roles);
		}

		try {
			userDao.persist(user);
			userDao.flush();
		} catch (Exception e) {
			logger.error(getBundle("USERERROR005"));
			logger.debug(e);

			return getBundle("USERERROR005");
		}

		sendEmailNewUser(userName, userEmail);

		return null;
	}

	////////////
	public void sendEmailNewUser(String userName, String userEmail) {
		Role role = roleDao.findByRole("Administrador");
		if (role == null) {
			return;
		}

		List<User> users = userDao.findListRole(1);
		if (users == null) {
			logger.warn("No administrators to send e-mail.");
			return;
		}

		String subject = getBundle("Subject_01");
		String message = getBundle("Body_01_1") + " " + userName + ", " + userEmail + ".\n\n" + getBundle("Body_01_2");

		for (int i = 0; i < users.size(); i++) {
			if (users.get(i).getIsactive()) {
				sendEmail.send(users.get(i).getEmail(), subject,
						getBundle("Body_02_1") + " " + users.get(i).getName() + "\n\n" + message);
			}
		}

	}

	///////// Reset da password ////////////
	public String pedeNovaPassword(String userEmail) {

		String textoTmp = validateVar(" ", userEmail, " ", " ", 1);
		if (textoTmp != null) {
			logger.debug(textoTmp);
			return textoTmp;
		}

		User user = userDao.findByEmail(userEmail);
		if (user == null) {
			logger.debug("Request new password " + getBundle("USERERROR006"));
			return getBundle("USERERROR006");
		}

		String texto = null;
		try {

			texto = createToken(userEmail);
			user.setToken(texto);
			userDao.merge(user);
			userDao.flush();
		} catch (Exception e) {
			logger.debug("Request new password");
			logger.error(e);
			return "Erro no tk.";
		}

		String subject = getBundle("Subject_02");

		String message = getBundle("Body_02_1") + user.getName() + "\n\n " + getBundle("Body_02_2") + " \n\n"
				+ "http://localhost:8080/ehsilva-vmoreira-projFinal/newPsw.xhtml?tk=" + texto + "&nr=" + randomNum
				+ "\n\n" + getBundle("Body_02_3") + " \n\n" + getBundle("Body_02_4");

		try {
			logger.debug("Enviar email para recuperar password " + " " + userEmail + " ");
			sendEmail.send(user.getEmail(), subject, message);
		} catch (Exception e) {
			logger.debug("Erro a enviar email");
			e.printStackTrace();
		}

		return null;

	}

	////////////

	public String criaNovaPassword(int random, String token, String userPws1, String userPws2) {
		String msg = getBundle("USERERROR007");
		if (token == null || random < 10000) {
			logger.debug("token null or nr invalid" + new Date());
			return msg;
		}

		String textoTmp = validateVar(" ", " ", userPws1, userPws2, 2);
		if (textoTmp != null) {
			return textoTmp;
		}

		Key key = keys.get(random);
		if (key == null) {
			logger.error(msg + " Ilegal " + new Date());
			return (msg);
		}

		try {
			textoTmp = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody().getSubject();
		} catch (ExpiredJwtException e1) {
			logger.error(msg + " - Expired ");
			logger.debug(e1);
			return (msg);
		} catch (UnsupportedJwtException e1) {
			logger.error(msg + " - Unsupported ");
			logger.debug(e1);
			return (msg);
		} catch (MalformedJwtException e1) {
			logger.error(msg + " - Malformed ");
			logger.debug(e1);
			return (msg);
		} catch (SignatureException e1) {
			logger.error(msg + " - Signature ");
			logger.debug(e1);
			return (msg);
		} catch (IllegalArgumentException e1) {
			logger.error(msg + " - Illegal ");
			logger.debug(e1);
			return (msg);
		}

		User user = userDao.findByEmail(textoTmp);
		if (user == null) {
			logger.debug(getBundle("USERERROR006") + " " + textoTmp);
			return getBundle("USERERROR006");
		}
		if (user.getToken() == null) {
			return msg;
		}
		if (!user.getToken().equals(token)) {
			return msg;
		}

		Date validade;
		try {
			validade = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody().getExpiration();
		} catch (ExpiredJwtException e1) {
			logger.error(msg + " - Expired ");
			logger.debug(e1);
			return (msg);
		} catch (UnsupportedJwtException e1) {
			logger.error(msg + " - Unsupported ");
			logger.debug(e1);
			return (msg);
		} catch (MalformedJwtException e1) {
			logger.error(msg + " - Malformed ");
			logger.debug(e1);
			return (msg);
		} catch (SignatureException e1) {
			logger.error(msg + " - Signature ");
			logger.debug(e1);
			return (msg);
		} catch (IllegalArgumentException e1) {
			logger.error(msg + " - Illegal ");
			logger.debug(e1);
			return (msg);
		}
		if (validade.before(new Date())) {
			logger.debug(msg + " - E.Expired ");
			return msg;
		}

		user.setToken(null);
		userImpl.setFlagLogin(false);
		user.setSalt(createSalt());
		user.setPassword(userImpl.encriptaPsw(user.getSalt(), userPws1));
		userImpl.setFlagLogin(true);

		try {
			userDao.merge(user);
			userDao.flush();
			key = keys.remove(random);
			logger.info("User created " + user.getName() + " " + user.getEmail() + " " + new Date());
		} catch (Exception e) {
			logger.error(msg);
			logger.debug(e);
			return msg;
		}

		return null;
	}

	private String createToken(String email) {
		// a JWT token
		// The issued token must be associated to a user
		// Return the issued token
		Date dateNow = new Date();
		Date dateExpired = new Date(dateNow.getTime() + 30 * 60 * 10000);

		String token;
		Key key = MacProvider.generateKey();
		randomNum = 0;
		do {
			randomNum = (int) (12345 + (Math.random() * 99999));
		} while (keys.containsKey(randomNum));
		keys.put(randomNum, key);

		try {
			token = Jwts.builder().setSubject(email).signWith(io.jsonwebtoken.SignatureAlgorithm.HS256, key)
					.setExpiration(dateExpired).compact();
		} catch (Exception e) {
			logger.debug(e);
			return "Error ";
		}
		return token;

	}

	/////////// Alteração de uma conta /////////////

	public String guardaEditUser(String userName, String userPws1, String userPws2, byte[] byteImage) {

		String messa = validateVar(userName, " ", userPws1, userPws2, 3);
		if (messa != null) {
			return messa;
		}

		User user = userDao.findByEmail(userImpl.getLoggedUser().getEmail());
		if (user == null) {
			logger.debug(
					getBundle("USERERROR007") + " Edit user " + userImpl.getLoggedUser().getEmail() + " " + new Date());
			return getBundle("USERERROR007");
		}

		user.setName(userName);
		user.setPhoto(byteImage);
		userImpl.setFlagLogin(false);
		if (userPws1 != null && !userPws1.equals("")) {
			user.setSalt(createSalt());
			user.setPassword(userImpl.encriptaPsw(user.getSalt(), userPws1));
		}
		userImpl.setFlagLogin(true);

		try {
			userDao.merge(user);
			userDao.flush();
			logger.info(getBundle("INFO_05") + " " + userImpl.getLoggedUser().getEmail() + " " + new Date() + user);
		} catch (Exception e) {
			logger.error(getBundle("USERERROR007") + " " + new Date());
			logger.debug(e);
			return getBundle("USERERROR007");
		}

		return null;
	}

	/**
	 * Método que cria um salt unico;
	 * 
	 * @return String
	 */
	private String createSalt() {
		UUID generatedSalt = UUID.randomUUID();
		String salt = generatedSalt.toString();
		// byte seed[] = random.generateSeed(10);

		return salt;
	}

	/**
	 * 
	 * @param userName
	 * @param userEmail
	 * @param userPws1
	 * @param userPws2
	 * @param int
	 *            isValida (=0 valida todos os param; =1 valida os param do
	 *            email; =2 valida os param da psw; =3 valida os param edit
	 *            proprio perfil;
	 * @return menssagem erro
	 */
	private String validateVar(String userName, String userEmail, String userPws1, String userPws2, int isValida) {

		String patternEmail = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		String patternString = "^[a-zA-Z\\s]*$";
		String patternStringP = "^[a-zA-Z0-9]*$";

		if (isValida == 0 || isValida == 1) {
			if (userEmail == null) {
				return getBundle("WARN_001" + " " + getBundle("Email1"));
			}
			if (userEmail.trim() == "") {
				return getBundle("WARN_001" + " " + getBundle("Email1"));
			}
			if (!userEmail.matches(patternEmail)) {
				return getBundle("WARN_002") + " " + getBundle("Email1");
			}
		}

		if (isValida == 0 || isValida == 3) {
			if (userName == null || userName.trim() == "") {
				return getBundle("WARN_001") + " " + getBundle("Name1");
			}
			if (!userName.matches(patternString)) {
				return getBundle("WARN_002") + " " + getBundle("Name1");
			}
		}
		
 
		if (isValida == 2 || isValida == 0) {
	 
			if (userPws1 == null || userPws2 == null) {
				return getBundle("WARN_001") + " " + getBundle("Psw1");
			}
	 
			if (userPws1.trim() == "" || userPws2.trim() == "") {
				return getBundle("WARN_001") + " " + getBundle("Psw1");
			}
		 
			if (!userPws1.matches(patternStringP) || !userPws2.matches(patternStringP)) {
				return getBundle("WARN_002") + " " + getBundle("Psw1");
			}
		 
			if (!userPws1.equals(userPws2)) {
				return getBundle("USERERROR010") + " " + getBundle("Psw1");
			}
		}

	 
		if (isValida == 3) {
			if (userPws1 == null || userPws2 == null) {
				return null;
			}
			if (userPws1.trim().equals("") || userPws2.trim().equals("")) {
				return null;
			}
			if (!userPws1.equals(userPws2)) {
				return getBundle("USERERROR010");
			}
			if (!userPws1.matches(patternStringP) || !userPws2.matches(patternStringP)) {
				return getBundle("WARN_002") + " " + getBundle("Psw1");
			}
		}
		return null;
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

	public String sendEmailAgain() {

		List<User> users = userDao.findListRole(1);
		if (users == null) {
			logger.warn("No administrators to send e-mail.");
			return null;
		}

		String subject = getBundle("Subject_01");
		String message = getBundle("Body_01_0") + " " + userImpl.getLoggedUser().getName() + ", "
				+ userImpl.getLoggedUser().getEmail() + ".\n\n" + getBundle("Body_01_2");

		for (int i = 0; i < users.size(); i++) {
			if (users.get(i).getIsactive()) {
				sendEmail.send(users.get(i).getEmail(), subject,
						getBundle("Body_02_1") + " " + users.get(i).getName() + "\n\n" + message);
			}
		}

		return null;
	}

}
