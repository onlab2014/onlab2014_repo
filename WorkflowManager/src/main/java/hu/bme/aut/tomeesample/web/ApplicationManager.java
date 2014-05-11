/**
 * ApplicationManager.java
 */
package hu.bme.aut.tomeesample.web;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author Imre Szekeres
 * @version "%I%, %G%"
 */
@Named
@ApplicationScoped
@SuppressWarnings("serial")
public class ApplicationManager implements java.io.Serializable {

	public static final String LOG_PROPERTIES = "resources/conf/log4j.properties";
	private static final Logger logger = Logger.getLogger(ApplicationManager.class);

	@PostConstruct
	public void init() {
		PropertyConfigurator.configure(LOG_PROPERTIES);
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("subject", null);
		logger.debug("logger configured..");
	}

	public String getLoggerName() {
		return logger.getName();
	}

	public void setLoggerName(String name) {
	}
}
