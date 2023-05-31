package pt.uc.dei.dao;

import java.io.Serializable;

import javax.ejb.Stateless;

import pt.uc.dei.model.Holiday;


@Stateless
public class HolidayDao extends AbstractDao<Holiday> implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public HolidayDao() {
		super(Holiday.class);
	}
}
