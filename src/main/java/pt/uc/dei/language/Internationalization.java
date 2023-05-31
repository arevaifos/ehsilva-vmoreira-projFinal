package pt.uc.dei.language;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;

import org.apache.log4j.Logger;

import pt.uc.dei.backingbeans.Activities;

/**
 * 
 * 
 *         Class relative to each language available to the user, considering
 *         different Locale's. In the Map countries the key's are associated
 *         with the respective locale (value).
 * 
 *
 */
@Named
@SessionScoped
public class Internationalization implements Serializable {

	private static final long serialVersionUID = 1L;
	final static Logger logger = Logger.getLogger(Internationalization.class);

	private String locale;
	private static Map<String, Object> countries;

	/**
	 * Empty constructor of the class. The Map countries is instantiated and the
	 * predefined languages are added with the respective Locale's.
	 */
	public Internationalization() {
		countries = new LinkedHashMap<String, Object>();
		countries.put("Portugues", Locale.getDefault());
		countries.put("English", Locale.US);
	}

	/**
	 * The method clears the Map countries and adds the languages indicated in
	 * the Resource Bundle with the respective Locale's
	 * 
	 * @param locale
	 *            String locale associated with the language
	 */
	public void setLocale(String locale) {
		ResourceBundle myMessages = getResourceBundle();
		countries.clear();
		countries.put(myMessages.getString("Portuguese"), Locale.getDefault());
		countries.put(myMessages.getString("English"), Locale.US);
		this.locale = locale;
	}

	public ResourceBundle getResourceBundle() {
		ResourceBundle myMessages = ResourceBundle.getBundle("language.messages",
				FacesContext.getCurrentInstance().getViewRoot().getLocale());
		return myMessages;
	}

	/**
	 * Method to change the locale based on the choice of the user and setting
	 * the locale as the chosen.
	 * 
	 * @param e
	 *            ValueChangeEvent event triggered
	 */
	public void changeLocale(ValueChangeEvent e) {
		String newLocale = e.getNewValue().toString();
		for (Map.Entry<String, Object> entries : countries.entrySet()) {
			if (entries.getValue().toString().equals(newLocale)) {
				FacesContext.getCurrentInstance().getViewRoot().setLocale((Locale) entries.getValue());
			}
		}
	}
	


	/**
	 * @return the countries
	 */
	public Map<String, Object> getCountries() {
		return countries;
	}

	/**
	 * @param countries
	 *            the countries to set
	 */
	public void setCountries(Map<String, Object> countries) {
		Internationalization.countries = countries;
	}

	/**
	 * @return the locale
	 */
	public String getLocale() {
		if(locale==null || locale.isEmpty()){
			locale=Locale.getDefault().getLanguage();
		}
		else{
//		logger.debug(locale);
		}
		return locale;
	}

	
}
