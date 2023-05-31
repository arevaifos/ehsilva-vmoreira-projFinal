package pt.uc.dei.implement;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

import org.apache.log4j.Logger;

import pt.uc.dei.dao.FunctionDao;
import pt.uc.dei.dao.RoleDao;
import pt.uc.dei.dao.UserDao;
import pt.uc.dei.exceptions.UserDataInvalidException;
import pt.uc.dei.language.Internationalization;
import pt.uc.dei.model.Function;
import pt.uc.dei.model.Role;
import pt.uc.dei.model.User;

@SessionScoped
public class AdminImplm implements Serializable {

	private static final long serialVersionUID = 1L;
	
	final static Logger logger = Logger.getLogger(AdminImplm.class);

	@Inject
	private Internationalization internationalization;
	@Inject
	private RoleDao roleDao;
	@Inject
	private FunctionDao functionDao;
	@Inject
	private UserDao userDao;
	@Inject
	private UserImpl userImpl;

	AdminImplm() {

	}

	/**
	 * Metodo para salvar na BD a edição de um utilizador.
	 * 
	 * @param user
	 * @throws UserDataInvalidException
	 */
	public void saveEditUser(User user) throws UserDataInvalidException {

		if (user != null) {
			User mergedUserData = userDao.findByIdUser(user.getIduser());

			if (mergedUserData != null) {
				// ative
				mergedUserData.setIsactive(user.getIsactive());
				// Name
				if (!mergedUserData.getName().equals(user.getName()) && user.getName() != null) {
					mergedUserData.setName(user.getName());
				}

				// Função
				if (!mergedUserData.getFunction().equals(user.getFunction()) && user.getFunction() != null) {
					mergedUserData.setFunction(user.getFunction());
				}
				// Senha
				if (!mergedUserData.getPassword().equals(user.getPassword()) && user.getPassword() != null) {
					String pass = userImpl.encriptaPsw(mergedUserData.getEmail(), user.getPassword());
					mergedUserData.setPassword(pass);
				}
				// Foto

				if (user.getRoles() != null && user.getRoles().size() > 0) {
					mergedUserData.setRoles(user.getRoles());
				} else {
					// Utilizador deve ter pelo menos uma role, devolver este
					// código
					// unico de erro para ser traduzido na web por intermédio do
					// bundle
					throw new UserDataInvalidException("USERERROR001", getBundle("USERERROR001"));
				}

				userDao.merge(mergedUserData);
				logger.info(getBundle("INFO_05")+" "+userImpl.getLoggedUser().getEmail()+" "+(new Date ())+" "+mergedUserData);
			} else {
				logger.debug(getBundle("USERERROR002")+" "+userImpl.getLoggedUser().getEmail()+" "+(new Date ())+" "+mergedUserData);
				throw new UserDataInvalidException("USERERROR002", getBundle("USERERROR002"));
			}
		} else {
			throw new UserDataInvalidException("USERERROR003", getBundle("USERERROR003"));
		}
		// System.out.println("Terminei o guardar...");
	}

	/**
	 * Método para obter da BD todas as roles
	 * 
	 * @return ArrayList<Role>
	 */
	public List<Role> findAllRoles() {
		List<Role> allRoles = roleDao.findAll();
		return allRoles;
	}

	/**
	 * Método para obter da BD todas as funções
	 * 
	 * @return ArrayList<Function>
	 */
	public List<Function> findAllFunction() {
		List<Function> allFunction = functionDao.findAll();
		return allFunction;
	}

	/**
	 * Método para obter da BD todos os Users
	 * 
	 * @return ArrayList<User>
	 */
	public List<User> findAllUsers() {
		List<User> allUsers = userDao.findAll();
		for (User u : allUsers) {
			u.setRoles(userDao.findListRoles(u.getIduser()));
		}
		return allUsers;
	}
	
	public User findUser(int id){
		return userDao.find(id);
	}
	
	
	public List<Role>findRolesByUser(int idUser){
		return userDao.findListRoles(idUser);
	}

	/**
	 * get message fron internationalization
	 * 
	 * @param key String
	 * @return
	 */
	private String getBundle(String key) {
		return internationalization.getResourceBundle().getString(key);
	}

}
