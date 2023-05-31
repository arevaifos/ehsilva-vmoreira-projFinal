package pt.uc.dei.implement;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

import org.apache.log4j.Logger;

import pt.uc.dei.dao.HolidayDao;
import pt.uc.dei.language.Internationalization;
import pt.uc.dei.model.Holiday;

@SessionScoped
public class HolidayImpl implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final static Logger logger = Logger.getLogger(HolidayImpl.class);
	
	@Inject
	private HolidayDao holidayDao;
	@Inject
	private Internationalization internationalization;
	
	private List<Holiday> holidays = new ArrayList<>();

	public List<Holiday> fillHollidays() {
		try {
			holidays = holidayDao.findAll();
			return holidays;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(getBundle("ERROR_HOLi_001"));
		}
		return holidays;
	}

	public void saveHoliday(Holiday holidayNew) {
		try {
			holidayDao.persist(holidayNew);
			logger.info(getBundle("INFO_04")+" " + holidayNew.getDateHoliday());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(getBundle("ERROR_HOLi_002")+" " + holidayNew.getDateHoliday());

		}
	}

	/**
	 * METODO PARA VERIFICAR SE É UM FERIADO
	 * 
	 * @param data
	 * @return BOOLEAN
	 */
	public boolean thatDayIsHoliday(Date data) {

		boolean isNotHoliday = true;
		fillHollidays();
		if (holidays.size() > 0) {
			for (int i = 0; i < holidays.size(); i++) {
				if (holidays.get(i).getDateHoliday().equals(data)) {
					isNotHoliday = false;
				}
			}
		}
		return isNotHoliday;
	}

	/**
	 * MÉTODO PARA VERIFICAR SE A DATA É VALIDA
	 * 
	 * @param dayToValidate
	 * @return BOOLEAN
	 */
	public boolean dayIsvalid(Date dayToValidate) {
		Calendar cal = Calendar.getInstance();
		DateFormat format = new SimpleDateFormat("yyyy/mm/dd");
		format.format(dayToValidate);
		cal = format.getCalendar();
		if (cal.get(Calendar.DAY_OF_WEEK) == 1 || cal.get(Calendar.DAY_OF_WEEK) == 7) {
			return false;
		} else {
			boolean isNotHoliday = thatDayIsHoliday(dayToValidate);
			return isNotHoliday;
		}
	}

	public List<Holiday> fillHolidaysAndWeekend() {

		return holidays;
	}

	private String getBundle(String key) {
		return internationalization.getResourceBundle().getString(key);
	}

	// public static void main(String[] args) {
	// Date hoje2 = Calendar.getInstance().getTime();
	// int i = (int) new Date().getTime();
	// Date hoje = new Date(i);
	//
	// System.out.println("Integer : " + i);
	// System.out.println("Long : "+ new Date().getTime());
	// System.out.println("Long date : " + new Date(new Date().getTime()));
	// System.out.println("Int Date : " + new Date(i));
	// Calendar cal=Calendar.getInstance();
	// DateFormat format=new SimpleDateFormat("yyyy/mm/dd");
	// format.format(hoje);
	// System.out.println("DATA1: "+format.getClass());
	// cal=format.getCalendar();
	// System.out.println("DATA1: "+cal.get(Calendar.DAY_OF_WEEK));
	//
	// System.out.println("Date2: " + hoje2);
	// Calendar cal2 = Calendar.getInstance();
	// System.out.println("Date2: cal2 "+cal2.get(Calendar.DAY_OF_WEEK));
	// cal2.add(Calendar.DAY_OF_MONTH, 1);
	// System.out.println("Date2 +1dia: " + cal2.getTime());
	// System.out.println("Date2: cal2 "+cal2.get(Calendar.DAY_OF_WEEK));
	// }
}
