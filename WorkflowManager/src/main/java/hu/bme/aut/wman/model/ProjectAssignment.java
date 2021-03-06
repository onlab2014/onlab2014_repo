/**
 * ProjectAssignment.java
 * */
package hu.bme.aut.wman.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;

/**
 * Entity implementation class for Entity: ProjectAssignment
 *
 * @version "%I%, %G%"
 */
@Entity
@SuppressWarnings("serial")
@NamedQueries({
	@NamedQuery(name = "ProjectAssignment.findByProjectId", query = "SELECT pa FROM ProjectAssignment pa WHERE pa.project.id=:id"),
	@NamedQuery(name = "ProjectAssignment.findByUser", query = "SELECT pa FROM ProjectAssignment pa " + "WHERE pa.user.username=:username")
})
public class ProjectAssignment extends AbstractEntity {

	public static final String NQ_FIND_BY_USER_NAME = "ProjectAssignment.findByUser";
	public static final String NQ_FIND_BY_PROJECT_ID = "ProjectAssignment.findByProjectId";

	public static final String PR_USER = "user";
	public static final String PR_PROJECT = "project";

	@NotNull
	@ManyToOne
	private User user;

	@NotNull
	@ManyToOne
	private Project project;

	@Deprecated
	public ProjectAssignment() {
		super();
	}

	public ProjectAssignment(User user, Project project) {
		super();
		this.user = user;
		this.project = project;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
		// this.user.addProjectAssignment(this);
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
		// this.project.addProjectAssignment(this);
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
		if (!(obj instanceof ProjectAssignment)) {
			return false;
		}
		ProjectAssignment other = (ProjectAssignment) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}
}
