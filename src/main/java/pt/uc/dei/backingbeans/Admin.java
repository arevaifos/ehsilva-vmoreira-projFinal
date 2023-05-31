package pt.uc.dei.backingbeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;


import pt.uc.dei.exceptions.UserDataInvalidException;
import pt.uc.dei.implement.AdminImplm;
import pt.uc.dei.language.Internationalization;
import pt.uc.dei.model.Function;
import pt.uc.dei.model.Role;
import pt.uc.dei.model.User;

/**
 * @author 
 *
 */

@Named("admin")
@SessionScoped
public class Admin implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final static Logger logger = Logger.getLogger(Admin.class);


	@Inject
	private Internationalization internationalization;

	@Inject
	private AdminImplm adminImplm;


	private List<Role> allRoles = new ArrayList<>();
	private List<Role> newRolesAtive = new ArrayList<>();
	private List<Role> newRolesInative = new ArrayList<>();
	private List<User> allUsers = new ArrayList<>();
	private List<Role> ativeRoles = new ArrayList<>();
	private List<Role> inativeRoles = new ArrayList<>();

	private List<String> functionString=new ArrayList<>();
	private List<String> ativeRolesString = new ArrayList<>();
	private List<String> inativeRolesString = new ArrayList<>();

	private List<Function> allFunction = new ArrayList<>();


	private String newRole;
	private String name = "";
	private String email = "";
	private String inputNewPassword="***";
	private String inputNewPassword2="***";
	private String newFunctionString="";

	private boolean ative = false;
	private boolean panelAdmin = true;
	private boolean canEdit = false; // variavel que vai permitir bloquear o
	//	// botão de editar.... e afins se
	//	// estiver a editar

	private Function newFunction = new Function();

	private Role roleInative;
	private Role roleAtive;

	private String roleInativeString;
	private String roleAtiveString;

	User editUser=new User();

	private int idEditUser = 0; // id do user a ser editado


	public Admin() {
		super();

	}

	@PostConstruct
	public void init(){
		this.allRoles = adminImplm.findAllRoles();
		this.allFunction = adminImplm.findAllFunction();
	}

	/**
	 * METODO TO LOAD OS DADOS DO USER
	 * 
	 * @param idUser
	 */
	public void loadUser(int idUser){
		idEditUser = idUser;
		for(int i=0; i<allFunction.size();i++){
			functionString.add(allFunction.get(i).getDesignation());
		}
		editUser=adminImplm.findUser(idUser);		
		name = editUser.getName();
		email = editUser.getEmail();
		if(editUser.getFunction()!=null){
			newFunctionString=editUser.getFunction().getDesignation();
		}
		for(int i=0; i<allFunction.size();i++){
			functionString.add(allFunction.get(i).getDesignation());
		}
		List<Role> rolesUser=adminImplm.findRolesByUser(editUser.getIduser());


		for (int i = 0; i < rolesUser.size(); i++) {
			if(rolesUser.size()>1){
				if(!rolesUser.get(i).getRole().equals("Visitante"))
					ativeRoles.add(rolesUser.get(i));
				ativeRolesString.add(rolesUser.get(i).getRole());
			}else{
				ativeRoles.add(rolesUser.get(i));
				ativeRolesString.add(rolesUser.get(i).getRole());		
			}
		}
		for (int j = 0; j < allRoles.size(); j++) {
			boolean isEqual = false;
			for (int i = 0; i < ativeRoles.size(); i++) {
				if (allRoles.get(j).getRole().equals(ativeRoles.get(i).getRole())) {
					isEqual = true;
				}
			}
			if (!isEqual) {
					inativeRoles.add(allRoles.get(j));
					inativeRolesString.add(allRoles.get(j).getRole());
				}
		}

		if (editUser.getIsactive()) {
			ative = true;
		} else if (!editUser.getIsactive()) {
			ative = false;
		} else {
			ative = false;
		}
		canEdit=true;

	}

	public void refreshFunctionUser(){ 
		allFunction=new ArrayList<>();
		functionString= new ArrayList<>();
		allFunction = adminImplm.findAllFunction();
		for(int i=0; i<allFunction.size();i++){
			functionString.add(allFunction.get(i).getDesignation());
		}
	}

	/**
	 * EVENTO DO ADICIONAR ROLE
	 * 
	 * @param event
	 */
	public void listenerLeft(AjaxBehaviorEvent event) throws UserDataInvalidException{
		String result = "called by " + event.getComponent().getClass().getName();
		logger.debug("ListenerLeft---> "+result);
	}


	/**
	 * EVENTO DO ELIMINAR ROLE
	 * 
	 * @param event
	 */
	public void listenerRight(AjaxBehaviorEvent event) {
		String result = "called by " + event.getComponent().getClass().getName();
		logger.debug("listenerRight---> "+result);
	}


	/**
	 * MÉTODO PARA ADICIONAR ROLE
	 */
	public void addNewRole(){
		logger.debug("INICIO");
		logger.debug("Print size ativeRoles BEFORE ----> " + ativeRolesString.size());
		if(roleInativeString!=null){
			ativeRolesString.add(roleInativeString);
			inativeRolesString.remove(roleInativeString);

		}
		logger.debug("Adicionei o " + roleInativeString);
		logger.debug("Print size ativeRoles  AFTER ----> " + ativeRolesString.size());

		for (int i = 0; i < ativeRolesString.size(); i++) {
			boolean notExist = false;
			for (int k = 0; k < ativeRoles.size(); k++) {
				if (ativeRoles.get(k).getRole().equals(ativeRolesString.get(i))) {
					notExist = true;
				}
			}

			if (!notExist) {
				for (int r = 0; r < allRoles.size(); r++) {
					if (allRoles.get(r).getRole().equals(ativeRolesString.get(i))) {
						ativeRoles.add(allRoles.get(r));
						inativeRoles.remove(allRoles.get(r));
					}
				}
			}

		}
		removeVisitante();
		logger.debug("FIM ADD ROLE");
	}


	/**
	 * MÉTODO PARA ELIMINAR ROLE
	 */
	public void deletedNewRole() {		
		if(roleAtiveString!=null && canRemove()){
			inativeRolesString.add(roleAtiveString);
			ativeRolesString.remove(roleAtiveString);
		}

		for (int i = 0; i < inativeRolesString.size(); i++) {
			boolean notExist = false;
			for (int k = 0; k < inativeRoles.size(); k++) {
				if (inativeRoles.get(k).getRole().equals(inativeRolesString.get(i))) {
					notExist = true;
				}
			}

			if (!notExist && canRemove()) {
				for (int r = 0; r < allRoles.size(); r++) {
					if (allRoles.get(r).getRole().equals(inativeRolesString.get(i))) {
						inativeRoles.add(allRoles.get(r));
						ativeRoles.remove(allRoles.get(r));
					}
				}
			}

		}
	}


	private boolean canRemove(){
		if(ativeRoles.size()>1){
			return true;
		}else{
			return false;
		}

	}

	private void removeVisitante(){
		for(int j=0;j<inativeRolesString.size();j++){
			if(!inativeRolesString.contains("Visitante") && !ativeRolesString.contains("Visitante"))
				if(ativeRoles.size()>1){
					Role visitante= new Role();
					String role ="Visitante";
					for(int i=0;i<ativeRoles.size();i++){
						if(ativeRoles.get(i).getRole().equals(role)){
							visitante=ativeRoles.get(i);
						}	
					}
					ativeRoles.remove(visitante);
					ativeRolesString.remove(role);
					inativeRoles.add(visitante);
					inativeRolesString.add(role);
				}

		}
		
		
		
	}

	private void addVisitante(){
		boolean hasVisitante=false;
		if(ativeRoles.size()>1){
			Role visitante= new Role();
			String role ="Visitante";
			for(int i=0; i<allRoles.size();i++){
				if(allRoles.get(i).getRole().equals(role)){
					visitante=allRoles.get(i);
				}
			}
			for(int i=0;i<ativeRoles.size();i++){
				if(ativeRoles.get(i).getRole().equals(role)){
					hasVisitante=true;			
				}	
			}
			if(!hasVisitante){
				inativeRoles.add(visitante);
				inativeRolesString.add(role);
			}
		}
	}


	/**
	 * MÉTODO PARA GUARDAR ALTERAÇÕES DO UTILIZADOR
	 */
	public void saveEditUser() throws UserDataInvalidException {

		if(!name.equals(editUser.getName())&&name!=null){
			editUser.setName(name);
		}
		editUser.setIsactive(ative);
		if(newFunctionString!=null){
			for(int i=0; i<allFunction.size();i++){
				if(allFunction.get(i).getDesignation().equals(newFunctionString))
					editUser.setFunction(allFunction.get(i));
			}
		}
		if(inputNewPassword!=null && inputNewPassword2!=null){
			if(inputNewPassword.equals(inputNewPassword2)){
				editUser.setPassword(inputNewPassword);
			}
		}
		editUser.setRoles(getAtiveRoles());

		try {
			adminImplm.saveEditUser(editUser);
			logger.debug("Voltou ao save do Edit Admin.java");
			cleanEditUser();
		} catch (UserDataInvalidException e) {
			logger.error(getBundle(e.getErrorCode()));
			logger.error(getBundle("USERERROR009"));

			FacesContext.getCurrentInstance().addMessage(e.getErrorCode(), new FacesMessage(internationalization.getResourceBundle().getString(e.getErrorCode())));
		}
		logger.debug("Save editUser");
		cleanEditUser();
	}

	public void canceleEditUser() {
		cleanEditUser();
		logger.debug("Cancele editUser");
	}


	private void cleanEditUser(){
		canEdit = false;
		ativeRolesString.clear();
		inativeRolesString.clear();
		functionString.clear();
		ativeRoles.clear();
		inativeRoles.clear();
		allFunction.clear();
		name = "";
		email="";
		ative = false;
		inputNewPassword="***";
		inputNewPassword2="***";
	}

	// GETTERS AND SETTERS
	public String getNewRole() {
		return newRole;
	}

	public void setNewRole(String newRole) {
		this.newRole = newRole;
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

	public void setAllUsers(List<User> allUsers) {
		this.allUsers = allUsers;
	}

	public boolean isAtive() {
		return ative;
	}

	public void setAtive(boolean ative) {
		this.ative = ative;
	}

	public void setAtiveRoles(List<Role> ativeRoles) {
		this.ativeRoles = ativeRoles;
	}

	public List<Role> getNewRolesAtive() {
		return newRolesAtive;
	}

	public void setNewRolesAtive(List<Role> newRolesAtive) {
		this.newRolesAtive = newRolesAtive;
	}

	public List<Role> getNewRolesInative() {
		return newRolesInative;
	}

	public void setNewRolesInative(List<Role> newRolesInative) {
		this.newRolesInative = newRolesInative;
	}

	public void setInativeRoles(List<Role> inativeRoles) {

		this.inativeRoles = inativeRoles;
	}

	public List<Role> getInativeRoles() {

		return inativeRoles;
	}

	public List<Role> getAllRoles() {
		return allRoles;
	}

	public void setAllRoles(List<Role> allRoles) {
		this.allRoles = allRoles;
	}

	public List<Function> getAllFunction() {
		return allFunction;
	}

	public void setAllFunction(List<Function> allFunction) {
		this.allFunction = allFunction;
	}

	public Function getNewFunction() {
		return newFunction;
	}

	public void setNewFunction(Function newFunction) {
		this.newFunction = newFunction;
	}

	public List<Role> getAtiveRoles() {
		return ativeRoles;
	}

	public Role getRoleInative() {
		return roleInative;
	}

	public void setRoleInative(Role roleInative) {;
	this.roleInative = roleInative;
	}

	public Role getRoleAtive() {
		return roleAtive;
	}

	public void setRoleAtive(Role roleAtive) {
		this.roleAtive = roleAtive;
	}

	public List<User> getAllUsers() {
		allUsers = adminImplm.findAllUsers();
		return allUsers;
	}

	public boolean isCanEdit() {
		return canEdit;
	}

	public void setCanEdit(boolean canEdit) {
		this.canEdit = canEdit;
	}

	public int getIdEditUser() {
		return idEditUser;
	}

	public void setIdEditUser(int idEditUser) {
		this.idEditUser = idEditUser;
	}

	public List<String> getAtiveRolesString() {
		return ativeRolesString;
	}

	public void setAtiveRolesString(List<String> ativeRolesString) {
		this.ativeRolesString = ativeRolesString;
	}

	public List<String> getInativeRolesString() {
		return inativeRolesString;
	}

	public void setInativeRolesString(List<String> inativeRolesString) {
		this.inativeRolesString = inativeRolesString;
	}

	public String getRoleInativeString() {
		return roleInativeString;
	}

	public void setRoleInativeString(String roleInativeString) {
		System.out.println("SETRoleInative1" + roleInativeString);
		this.roleInativeString = roleInativeString;
	}

	public String getRoleAtiveString() {
		return roleAtiveString;
	}

	public void setRoleAtiveString(String roleAtiveString) {
		System.out.println("SETRoleAtive1" + roleAtiveString);
		this.roleAtiveString = roleAtiveString;
	}

	public boolean isPanelAdmin() {
		panelAdmin = !panelAdmin;
		return panelAdmin;
	}

	public void setPanelAdmin(boolean panelAdmin) {
		this.panelAdmin = panelAdmin;
	}

	public String getInputNewPassword() {
		return inputNewPassword;
	}

	public void setInputNewPassword(String inputNewPassword) {
		this.inputNewPassword = inputNewPassword;
	}

	public String getInputNewPassword2() {
		return inputNewPassword2;
	}

	public void setInputNewPassword2(String inputNewPassword2) {
		this.inputNewPassword2 = inputNewPassword2;
	}

	public List<String> getFunctionString() {
		return functionString;
	}

	public void setFunctionString(List<String> functionString) {
		this.functionString = functionString;
	}

	public String getNewFunctionString() {
		return newFunctionString;
	}

	public void setNewFunctionString(String newFunctionString) {
		this.newFunctionString = newFunctionString;
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
