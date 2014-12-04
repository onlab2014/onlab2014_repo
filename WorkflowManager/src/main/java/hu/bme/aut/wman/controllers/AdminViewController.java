/**
 * AdminViewController.java
 */
package hu.bme.aut.wman.controllers;

import static hu.bme.aut.wman.controllers.LoginController.userIDOf;
import hu.bme.aut.wman.service.DomainService;
import hu.bme.aut.wman.service.PrivilegeService;
import hu.bme.aut.wman.service.RoleService;
import hu.bme.aut.wman.service.UserService;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Imre Szekeres
 * @version "%I%, %G%"
 */
@Controller
public class AdminViewController extends AbstractController {

	public static final String ROOT_URL = "/admin";
	public static final String PRIVILEGES = ROOT_URL + "/privileges";
	public static final String ROLES = ROOT_URL + "/roles";
	public static final String DOMAINS = ROOT_URL + "/domains";
	public static final String USERS = ROOT_URL + "/users";
	
	private static final Map<String, String> NAVIGATION_TABS;
	
	static {
		NAVIGATION_TABS = new HashMap<>();
		NAVIGATION_TABS.put(NAV_PREFIX + PRIVILEGES, "Privileges");
		NAVIGATION_TABS.put(NAV_PREFIX + ROLES, "Roles");
		NAVIGATION_TABS.put(NAV_PREFIX + DOMAINS, "Domains");
		NAVIGATION_TABS.put(NAV_PREFIX + USERS, "Users");
	}

	@EJB(mappedName = "java:module/UserService")
	private UserService userService;
	@EJB(mappedName = "java:module/RoleService")
	private RoleService roleService;
	@EJB(mappedName = "java:module/DomainService")
	private DomainService domainService;
	@EJB(mappedName = "java:module/PrivilegeService")
	private PrivilegeService privilegeService;

	
	@SuppressWarnings("deprecation")
	@RequestMapping(value = ROOT_URL, method = RequestMethod.GET)
	public String admin(Model model) {
		return redirectTo(AdminViewController.USERS);
	}

	@RequestMapping(value = ROLES, method = RequestMethod.GET)
	public String adminRoles(Model model, HttpSession session) {
		setAdminRolesContent(model, userIDOf(session), domainService);
		return navigateToFrame("admin_roles", model);
	}

	@RequestMapping(value = DOMAINS, method = RequestMethod.GET)
	public String adminDomains(Model model, HttpSession session) {
		setAdminDomainsContent(model, userIDOf(session), domainService);
		return navigateToFrame("admin_domains", model);
	}

	@RequestMapping(value = PRIVILEGES, method = RequestMethod.GET)
	public String adminPrivileges(Model model) {
		model.addAttribute("privileges", privilegeService.selectAll());
		return navigateToFrame("admin_privileges", model);
	}
	
	@RequestMapping(value = USERS, method = RequestMethod.GET)
	public String adminUsers(Model model, HttpSession session) {
		Long subjectID = userIDOf(session);
		setAdminUsersContent(model, subjectID, userService);
		return navigateToFrame("admin_users", model);
	}
	
	@Override
	public Map<String, String> getNavigationTabs() {
		return NAVIGATION_TABS;
	}
	
	public static final void setAdminRolesContent(Model model, Long subjectID, DomainService domainService) {
		model.addAttribute("domains", domainService.domainsOf( subjectID ));
		model.addAttribute("selectPrivilegesUrl", PrivilegesController.ROOT_URL);
		model.addAttribute("selectCreateFormUrl", RolesController.CREATE_FORM);
		model.addAttribute("selectUpdateFormUrl", RolesController.UPDATE_FORM);
		model.addAttribute("deleteRoleAction", RolesController.DELETE);
		model.addAttribute("navigationTabs", NAVIGATION_TABS);
	}
	
	public static final void setAdminDomainsContent(Model model, Long subjectID, DomainService domainService) {
		model.addAttribute("domains", domainService.domainsOf( subjectID ));
		model.addAttribute("selectRolesUrl", RolesController.ROOT_URL);
		model.addAttribute("selectCreateFormUrl", DomainsController.CREATE_FORM);
		model.addAttribute("selectUpdateFormUrl", DomainsController.UPDATE_FORM);
		model.addAttribute("deleteDomainAction", DomainsController.DELETE);
		model.addAttribute("navigationTabs", NAVIGATION_TABS);
	}
	
	public static final void setAdminUsersContent(Model model, Long subjectID, UserService userService) {
		model.addAttribute("users", userService.selectUsersInDomainOf( subjectID ));
		model.addAttribute("selectDomainsForUrl", UsersController.DOMAINS);
		model.addAttribute("selectDomainNamesUrl", DomainsController.NAMES);
		model.addAttribute("selectRolesForUrl", RolesController.ROOT_URL);
		model.addAttribute("selectCreateFormUrl", UsersController.CREATE_FORM);
		model.addAttribute("selectUpdateFormUrl", UsersController.UPDATE_FORM);
		model.addAttribute("deleteUserAction", UsersController.DELETE);
		model.addAttribute("detailsUserAction", UsersController.PROFILE);
		model.addAttribute("navigationTabs", NAVIGATION_TABS);
	}
}
