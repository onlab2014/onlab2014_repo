/**
 * AuthenticationService.java
 */
package hu.bme.aut.wman.service;

import static hu.bme.aut.wman.utils.StringUtils.isEmpty;

import java.util.Arrays;

import javax.ejb.EJB;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author Imre Szekeres
 * @version "%I%, %G%"
 */
public class AuthenticationService implements UserDetailsService {

	@EJB(mappedName = "java:module/UserService")
	private UserService userService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		String password = userService.selectPasswordOf(username);
		if (isEmpty( password ))
			throw new UsernameNotFoundException(username);
		return new User(username, password, Arrays.asList(new GrantedAuthority[] { new SimpleGrantedAuthority("ROLE_USER") }));
	}
}