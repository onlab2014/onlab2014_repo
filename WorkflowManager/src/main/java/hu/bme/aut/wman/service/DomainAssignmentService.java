/**
 * DomainAssignmentService.java
 */
package hu.bme.aut.wman.service;

import hu.bme.aut.wman.model.Domain;
import hu.bme.aut.wman.model.DomainAssignment;
import hu.bme.aut.wman.model.Role;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 * @author Imre Szekeres
 * @version "%I%, %G%"
 */
@LocalBean
@Stateless
public class DomainAssignmentService extends AbstractDataService<DomainAssignment> {

	private static final long serialVersionUID = -7130755663605495578L;

	/**
	 * Retrieves the <code>DomainAssignment</code>s corresponding to the given <code>Domain</code> specified
	 * by its name.
	 * 
	 * @param domin
	 * @return the {@link DomainAssignmnet}s owned by the {@link Domain} given
	 * */
	public List<DomainAssignment> selectByDomainName(String domain) {
		List<Map.Entry<String, Object>> parameters = new ArrayList<>(1);
		parameters.add(new AbstractMap.SimpleEntry<String, Object>("domainName", domain));
		return callNamedQuery(DomainAssignment.NQ_FIND_BY_DOMAIN, parameters);
	}

	/**
	 * Retrieves the <code>DomainAssignment</code>s corresponding to the given <code>User</code> specified
	 * by its id.
	 * 
	 * @param userID
	 * @return the {@link DomainAssignmnet}s binding the given {@link User} to a specific {@link Domain}
	 * */
	public List<DomainAssignment> selectByUserID(Long userID) {
		List<Map.Entry<String, Object>> parameters = new ArrayList<>(1);
		parameters.add(new AbstractMap.SimpleEntry<String, Object>("userID", userID));
		return callNamedQuery(DomainAssignment.NQ_FIND_BY_USER_ID, parameters);
	}

	/**
	 * Retrieves the specific <code>DomainAssignment</code> that binds the given <code>User</code> to
	 * the given <code>Domain</code>.
	 * 
	 * @param userID
	 * @param domin
	 * @return the {@link DomainAssignmnet} found
	 * */
	public DomainAssignment selectByDomainFor(Long userID, String domain) {
		List<Map.Entry<String, Object>> parameters = new ArrayList<>(2);
		parameters.add(new AbstractMap.SimpleEntry<String, Object>("userID", userID));
		parameters.add(new AbstractMap.SimpleEntry<String, Object>("domainName", domain));
		List<DomainAssignment> assignments = callNamedQuery(DomainAssignment.NQ_FIND_BY_DOMAIN_FOR, parameters);
		
		return (assignments.size() > 0) ? assignments.get(0) : null;
	}

	/**
	 * @param username
	 * @param domain
	 * @return the {@link DomainAssignment} binding {@link User} to the {@link Domain}
	 * 
	 * @see {@link DomainAssignmentService#selectByDomainFor(String, String)}
	 * */
	public DomainAssignment selectByDomainFor(String username, String domain) {
		List<Map.Entry<String, Object>> parameters = new ArrayList<>(2);
		parameters.add(new AbstractMap.SimpleEntry<String, Object>("username", username));
		parameters.add(new AbstractMap.SimpleEntry<String, Object>("domainName", domain));
		List<DomainAssignment> assignments = callNamedQuery(DomainAssignment.NQ_FIND_BY_USERNAME_FOR, parameters);
		
		return (assignments.size() > 0) ? assignments.get(0) : null;
	}

	/**
	 * Attempts to delete the <code>DomainAssignment</code> given by its id without querying the whole entity
	 * into the memory.
	 * 
	 * @param assignmentId
	 * @return the rows affected
	 * */
	public int deleteAssignmentById(Long assignmentId) {
		List<Map.Entry<String, Object>> parameters = new ArrayList<>(2);
		parameters.add(new AbstractMap.SimpleEntry<String, Object>("id", assignmentId));
		return executeNamedQuery(DomainAssignment.NQ_DELETED_BY_ID, parameters);
	}

	/**
	 * Attempts to delete the all <code>DomainAssignment</code>s specified by the identifier of the <code>User</code> 
	 * being assigned to any <code>Domain</code> by them.
	 * 
	 * @param userID
	 * @return the rows affected
	 * */
	public int deleteByUserID(Long userID) {
		List<Map.Entry<String, Object>> parameters = new ArrayList<>(2);
		parameters.add(new AbstractMap.SimpleEntry<String, Object>("userID", userID));
		return executeNamedQuery(DomainAssignment.NQ_DELETED_BY_USER_ID, parameters);
	}

	/**
	 * Attempts to delete the all <code>DomainAssignment</code>s specified by the identifier of the <code>Domain</code>.
	 * 
	 * @param domainID
	 * @return the rows affected
	 * */
	public int deleteByDomainID(Long domainID) {
		List<Map.Entry<String, Object>> parameters = new ArrayList<>(2);
		parameters.add(new AbstractMap.SimpleEntry<String, Object>("domainID", domainID));
		return executeNamedQuery(DomainAssignment.NQ_DELETED_BY_DOMAIN_ID, parameters);
	}

	/**
	 * Retrieves the <code>Domain</code> names and <code>DomainAssignment</code> ids as a <code>Map</code>
	 * using the domain name as a key.
	 * 
	 * @param userID
	 * @return a {@link Map} of <domainName, domainAssignmentID> pairs
	 * */
	public Map<String, Long> selectDomainsAndIds(Long userID) {
		List<Map.Entry<String, Object>> parameters = new ArrayList<>(2);
		parameters.add(new AbstractMap.SimpleEntry<String, Object>("userID", userID));
		return assignmentsMapOf(callNamedQuery(DomainAssignment.NQ_FIND_DOMAINS_AND_IDS, parameters, Object[].class));
	}

	/**
	 * Helper method for building a <code>Map</code> containing <domainName, domainAssignmentId> pairs
	 * from the result of calling the <code>NamedQuery</code> <code>NQ_FIND_DOMAINS_AND_IDS</code> in the 
	 * class <code>DomainAssignment</code>
	 * 
	 * @param entries
	 * @return a {@link Map}
	 * */
	protected Map<String, Long> assignmentsMapOf(List<Object[]> entries) {
		Map<String, Long> map = new HashMap<>(0);
		for(Object[] e : entries)
			map.put(e[0].toString(), (Long) e[1]);
		return map;
	}

	/**
	 * Obtains all the <code>Domain</code> names and all the <code>Role</code> names the <code>User</code> 
	 * specified by its user id has.
	 * 
	 * @param userID
	 * @return a {@link Map} containing its {@link Role}s ordered by the {@link Domain}s they are defined in
	 * */
	public Map<String, List<String>> assignmentsOf(Long userID) {
		List<DomainAssignment> assignments = selectByUserID( userID );
		Map<String, List<String>> results = new HashMap<>();
		if (assignments.isEmpty())
			return results;
		
		List<String> roles;
		for(DomainAssignment da : assignments) {
			roles = new ArrayList<>();
			for(Role r : da.getUserRoles())
				roles.add( r.getName() );
			results.put(da.getDomain().getName(), roles);
		}
		return results;
	}

	/**
	 * @see {@link AbstractDataService#getClass()}
	 * */
	@Override
	protected Class<DomainAssignment> getEntityClass() {
		return DomainAssignment.class;
	}
}
