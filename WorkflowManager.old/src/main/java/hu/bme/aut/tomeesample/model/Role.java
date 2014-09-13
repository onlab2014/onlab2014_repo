/**
 * Role.java
 * */
package hu.bme.aut.tomeesample.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

/**
 * Entity implementation class for Entity: Role
 * 
 * @author Imre Szekeres
 * @version "%I%, %G%"
 */
@Entity
@SuppressWarnings("serial")
public class Role implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	private String name;

	@ManyToMany(targetEntity = ActionType.class, fetch = FetchType.EAGER)
	private Set<ActionType> actionTypes;

	@ManyToMany(targetEntity = hu.bme.aut.tomeesample.model.User.class, mappedBy = "roles")
	private Set<User> users;

	public Role() {
		super();
		this.users = new HashSet<>();
		this.actionTypes = new HashSet<>();
	}

	public Role(String name) {
		this();
		this.name = name;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the users
	 */
	public Set<User> getUsers() {
		return users;
	}

	/**
	 * Add {@link User} to this Role
	 * 
	 * @param user
	 *            the {@link User} to add
	 * @return true if the {@link User} is added
	 */
	public boolean addUser(User user) {
		return this.users.add(user);
	}

	/**
	 * Remove {@link User} from this Role
	 * 
	 * @param user
	 *            the {@link User} to remove
	 * @return true if the {@link User} is removed
	 */
	public boolean removeUser(User user) {
		return this.users.remove(user);
	}

	/**
	 * Add {@link ActionType} to this Role
	 * 
	 * @param actionType
	 *            the {@link ActionType} to add
	 * @return true if the {@link ActionType} is added
	 */
	public boolean addActionType(ActionType actionType) {
		return actionTypes.add(actionType);
	}

	/**
	 * Remove {@link ActionType} from this Role
	 * 
	 * @param actionType
	 *            the {@link ActionType} to remove
	 * @return true if the {@link ActionType} is removed
	 */
	public boolean removeActionType(ActionType actionType) {
		return actionTypes.remove(actionType);
	}

	/**
	 * @return the actionTypes
	 */
	public Set<ActionType> getActionTypes() {
		return actionTypes;
	}

	/**
	 * @param actionTypes
	 *            the actionTypes to set
	 */
	public void setActionTypes(Set<ActionType> actionTypes) {
		this.actionTypes = actionTypes;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Role)) {
			return false;
		}
		Role other = (Role) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return name;
	}
}