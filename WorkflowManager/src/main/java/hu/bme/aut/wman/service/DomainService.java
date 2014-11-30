/**
 * DomainService.java
 */
package hu.bme.aut.wman.service;

import hu.bme.aut.wman.exceptions.EntityNotDeletableException;
import hu.bme.aut.wman.model.Domain;
import hu.bme.aut.wman.model.DomainAssignment;
import hu.bme.aut.wman.service.validation.DomainValidator;
import hu.bme.aut.wman.service.validation.ValidationEngine;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
/**
 * Handles the database access regarding the <code>Domain</code> instances, the business logic
 * corresponding to the CRUD operations of them.
 * 
 * @author Imre Szekeres
 * @version "%I%, %G%"
 */
@LocalBean
@Stateless
public class DomainService extends AbstractDataService<Domain> {

	public static final String DEFAULT_DOMAIN = "System";
	public static final String DEFAULT_ROLE = "System Viewer";
	
	@Inject
	private DomainAssignmentService domainAssignmentService;
	private ValidationEngine<Domain> validator;

	/**
	 * Initializes the validation mechanism used.
	 * */
	@PostConstruct
	public void setup() {
		validator = new DomainValidator();
	}

	/**
	 * Validates the given <code>Domain</code> object against its given <code>ValidationConstraint</code>s 
	 * as Java annotation using Bean Validation mechanisms.
	 * 
	 * @param domain
	 * @return the {@link java.util.Map} representation of (<code>propertyName</code>, <code>errorMessage</code>) entries
	 * */
	public Map<String, String> validate(Domain domain) {
		Map<String, String> errors = validator.validate( domain );
		if (selectByName(domain.getName()) != null)
			errors.put("name", "Domain " + domain.getName() + " already exists!");
		return errors;
	}
	
	
	/**
	 * Retrieves all <code>Domain</code> names stored in the database.
	 * 
	 * @return a list of {@link Domain} names
	 * */
	public List<String> selectAllNames() {
		return callNamedQuery( Domain.NQ_FIND_ALL_NAMES,
				               new ArrayList<Map.Entry<String, Object>>(0),
				               String.class );
	}

	/**
	 * Retrieves an occurrence of a <code>Domain</code> specified by the given name.
	 * 
	 * @param domain
	 * @return the {@link Domain} instance corresponding to it
	 * */
	public Domain selectByName(String domain) {
		List<Map.Entry<String, Object>> parameters = new ArrayList<Map.Entry<String, Object>>(1);
		parameters.add(new AbstractMap.SimpleEntry<String, Object>("domainName", domain));
		
		List<Domain> domains = callNamedQuery("Domain.findByName", parameters);
		return (domains.size() > 0) ? domains.get(0) : null;
	}

	/**
	 * Retrieves an occurrence of a <code>Domain</code> specified by the given roleID.
	 * 
	 * @param roleID
	 * @return the {@link Domain} instance corresponding to the given roleID
	 * */
	public Domain selectByRoleID(long roleID) {
		List<Map.Entry<String, Object>> parameters = new ArrayList<Map.Entry<String, Object>>(1);
		parameters.add(new AbstractMap.SimpleEntry<String, Object>("roleID", roleID));
		
		List<Domain> domains = callNamedQuery("Domain.findByRoleID", parameters);
		return (domains.size() > 0) ? domains.get(0) : null;
	}

	/**
	 * @see {@link AbstractDataService#getEntityClass()}
	 * */
	@Override
	protected Class<Domain> getEntityClass() {
		return Domain.class;
	}

	/**
	 * @see {@link AbstractDataService#delete(hu.bme.aut.wman.model.AbstractEntity)}
	 * */
	@Override
	public void delete(Domain domain) throws EntityNotDeletableException {
		List<DomainAssignment> assignments = domainAssignmentService.selectByDomainName(domain.getName());
		for(DomainAssignment da : assignments)
			domainAssignmentService.delete( da );
		super.delete( domain );
	}
}
