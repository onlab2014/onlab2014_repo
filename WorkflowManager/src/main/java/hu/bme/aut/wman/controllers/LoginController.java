/**
 * LoginController.java
 */
package hu.bme.aut.wman.controllers;


import java.util.Map;
import java.util.HashMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.stereotype.Controller;
import javax.servlet.http.HttpServletRequest;
import hu.bme.aut.wman.service.UserService;
import org.springframework.ui.Model;
import hu.bme.aut.wman.model.User;
import javax.ejb.EJB;
/**
 * 
 * @author Imre Szekeres
 * @version "%I%, %G%"
 */
@Controller
public class LoginController {
	
	@EJB(mappedName="java:module/UserService")
	private UserService userService;


	@RequestMapping(value="/", method=RequestMethod.GET)
	public String home(Model model, HttpServletRequest request) {

		if(request.getSession().getAttribute("subject") != null) {
			model.addAttribute("message", "Welcome to WorkflowManager!");
			model.addAttribute("pageName", "index");
			return "wman_frame";
		}
		return "redirect:/login";
	}

	@RequestMapping(value="/login", method=RequestMethod.GET)
	public String getLogin(Model model) {

		User subject = new User();
		model.addAttribute("subject", subject);
		model.addAttribute("pageName", "login");
		return "login";
	}


	@RequestMapping(value="/login", method=RequestMethod.POST)
	public String postLogin(@ModelAttribute("subject") User subject, HttpServletRequest request, Model model){

		User user;
		if((user = doAuthenticate(subject)) != null) {
			request.getSession().setAttribute("subject", user);
			return "redirect:/";
		}
		subject.setPassword("");
		model.addAttribute("loginError", "Username or password is invalid!");
		return "login";
	}


	private final User doAuthenticate(User subject) {

		User user = userService.selectByName(subject.getUsername());
		if(user == null)
			return null;
		return user.getPassword().equals(subject.getPassword()) ? user : null;
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String register(@ModelAttribute("subject") User user, HttpServletRequest request, Model model) {
		Map<String, String> validationErrors;
		if((validationErrors = doValidate(user, request)).size() <= 0) {
			// TODO: userService.save(user);
			request.getSession().setAttribute("subject", user);
			// TODO: add a newbie Role to the user..
		}
		model.addAttribute("validationErrors", validationErrors);
		return "redirect:/";
	}
	
	private final Map<String, String> doValidate(User user, HttpServletRequest request) {
		// TODO:
		System.out.println("user.username: " + user.getUsername());
		System.out.println("user.password: " + user.getPassword());
		System.out.println("passwordAgain: " + request.getParameter("passwordAgain"));
		System.out.println("user.email: " + user.getEmail());
		System.out.println("user.description: " + user.getDescription());
		return new HashMap<String, String>(0);
	}
}