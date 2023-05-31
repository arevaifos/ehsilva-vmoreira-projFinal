package pt.uc.dei.dao;

import java.io.Serializable;

import javax.ejb.Stateless;

import pt.uc.dei.model.Type_activity;

@Stateless
public class Type_activityDao extends AbstractDao<Type_activity> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Type_activityDao() {
		super(Type_activity.class);

	}
	
	
}
