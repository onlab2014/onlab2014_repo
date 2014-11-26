/**
 * UserTransferObject.java
 */
package hu.bme.aut.wman.view.objects.transfer;

import hu.bme.aut.wman.model.User;

import java.util.Arrays;
import java.util.List;

/**
 * @author Imre Szekeres
 * @version "%I%, %G%"
 */
public class UserTransferObject extends User {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5868958621058535692L;
	private String confirmPassword;
	private String domainName;
	private String userRoles;


	public UserTransferObject() {
		super("", "", "", "");
		this.confirmPassword = this.domainName = this.userRoles = "";
	}
	
	/**
	 * @return the confirmPassword
	 */
	public String getConfirmPassword() {
		return confirmPassword;
	}

	/**
	 * @param confirmPassword the confirmPassword to set
	 */
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	/**
	 * @return the domainName
	 */
	public String getDomainName() {
		return domainName;
	}

	/**
	 * @param domainName the domainName to set
	 */
	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	/**
	 * @return the userRoles
	 */
	public String getUserRoles() {
		return userRoles;
	}

	/**
	 * @param userRoles the userRoles to set
	 */
	public void setUserRoles(String userRoles) {
		this.userRoles = userRoles;
	}
	
	public List<String> userRoles() {
		return Arrays.asList(userRoles.split("\\|"));
	}
	
	public User asUser() {
		return new User(username, password, email, description);
	}
}