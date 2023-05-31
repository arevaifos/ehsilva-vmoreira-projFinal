package pt.uc.dei.backingbeans;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;

import pt.uc.dei.implement.HolidayImpl;
import pt.uc.dei.implement.UserImpl;
import pt.uc.dei.language.Internationalization;
import pt.uc.dei.model.Holiday;

@Named("holidays")
@SessionScoped
public class Holidays implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final static Logger logger = Logger.getLogger(Holidays.class);
	
	
	@Inject
	private Internationalization internationalization;
	
	@Inject
	private HolidayImpl holidayImpl;
	
	@Inject
	private UserImpl userImpl;

	private List<Holiday> allHolidays =new ArrayList<>();
	private List<Date> holidayDate=new ArrayList<>();

	private String dscHoliday="";

	private boolean annualHoliday=false;
	
	private Date holiday;

	public Holidays() {
		super();

	}

	@PostConstruct
	public void initHoliday(){
		this.allHolidays = holidayImpl.fillHollidays();
		this.holidayDate=holidaysDate();
	}
	
	public List<Holiday> refill(){
		allHolidays=holidayImpl.fillHollidays();
		return allHolidays;
	}

	public boolean canUseDate(Date dayToValidade){
		return holidayImpl.dayIsvalid(dayToValidade);
	}

	/**
	 * METODO PARA ADICIONAR FERIADO A TABELA DE FERIADOS
	 */
	public void saveHoliday(){
		if(!dscHoliday.equals("")){
			Holiday holidayNew=new Holiday();
			holidayNew.setDateHoliday(holiday);
			holidayNew.setDescription(dscHoliday);
			holidayNew.setIsAnual(annualHoliday);

			try {
				holidayImpl.saveHoliday(holidayNew);
				refill();
				cleanHoliday();
				logger.info(getBundle("INFO_04")+" "+holidayNew.getDescription()+" "+dscHoliday+" "+userImpl.getLoggedUser().getEmail()+" "+new Date ());
			 
			} catch (Exception e1) {
				logger.error(getBundle("ERROR_HOLi_001"));
			}

		}else{
			logger.debug("ERROR_HOLi_001"+" "+dscHoliday);
		}
		
	}

	private void cleanHoliday(){
		holiday= new Date();
		annualHoliday=false;
		dscHoliday="";	
	}
	
	private List<Date> holidaysDate(){
		//DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
		for(int i=0;i<allHolidays.size();i++){
			holidayDate.add(allHolidays.get(i).getDateHoliday());
//			logger.debug("ADD: "+allHolidays.get(i).getDateHoliday());
		}
		return holidayDate;
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

	
	
	//GETTERS E SETTERS 
	
	public String getDscHoliday() {
		return dscHoliday;
	}

	public void setDscHoliday(String dscHoliday) {
		this.dscHoliday = dscHoliday;
	}

	public Date getHoliday() {
		return holiday;
	}

	public void setHoliday(Date holiday) {
		this.holiday = holiday;
	}

	public boolean isAnnualHoliday() {
		return annualHoliday;
	}

	public void setAnnualHoliday(boolean annualHoliday) {
		this.annualHoliday = annualHoliday;
	}

	public List<Holiday> getAllHolidays() {
		return allHolidays;
	}

	public void setAllHolidays(List<Holiday> allHolidays) {
		this.allHolidays = allHolidays;
	}

	public List<Date> getHolidayDate() {
		return holidayDate;
	}

	public void setHolidayDate(List<Date> holidayDate) {
		this.holidayDate = holidayDate;
	}

	
	
	
}
