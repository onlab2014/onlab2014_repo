package hu.bme.aut.wman.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Entity implementation class for Projects
 * 
 * @version "%I%, %G%"
 */
@SuppressWarnings("serial")
@Entity
@NamedQueries({
		@NamedQuery(name = "Project.findAllByWorkflowName", query = "SELECT p FROM Project p WHERE p.workflow.name=:name")
})
public class Project extends AbstractEntity {

	public static final String NQ_FIND_BY_WORKFLOW_NAME = "Project.findAllByWorkflowName";

	public static final String PR_NAME = "name";
	public static final String PR_CURRENT_STATE = "currentState";
	public static final String PR_DESCRIPTION = "description";
	public static final String PR_WORKFLOW = "workflow";
	public static final String PR_COMMENTS = "comments";
	public static final String PR_PROJECT_ASSIGNMENTS = "projectAssignments";
	public static final String PR_HISTORY_ENTRIES = "historyEntries";

	@NotNull
	@Size(min = 5, max = 16)
	@Column(unique = true)
	private String name;

	@Size(min = 13, max = 512)
	private String description;

	@NotNull
	@OneToOne(fetch = FetchType.EAGER)
	private State currentState;

	@NotNull
	@ManyToOne
	private Workflow workflow;

	@OneToMany(mappedBy = "project")
	private List<HistoryEntry> historyEntries;

	@OneToMany(mappedBy = "project", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	private Set<ProjectAssignment> projectAssignments;

	@OneToMany(mappedBy = "project", cascade = CascadeType.REMOVE)
	private List<Comment> comments;

	@NotNull
	private Boolean active = true;

	public Project() {
		super();
	}

	public Project(String name, String description, Workflow workflow) {
		this.name = name;
		this.description = description;
		this.workflow = workflow;
		this.workflow.addProject(this);
		this.currentState = this.workflow.getInitialState();
		this.projectAssignments = new HashSet<>();
		this.historyEntries = new ArrayList<>();
		this.comments = new ArrayList<>();
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public State getCurrentState() {
		return currentState;
	}

	public Workflow getWorkflow() {
		return workflow;
	}

	public List<HistoryEntry> getHistoryEntries() {
		return historyEntries;
	}

	/**
	 * @param historyEntries
	 *            the historyEntries to set
	 */
	public void setHistoryEntries(List<HistoryEntry> historyEntries) {
		this.historyEntries = historyEntries;
	}

	public Set<ProjectAssignment> getProjectAssignments() {
		return projectAssignments;
	}

	/**
	 * @param projectAssignments
	 *            the projectAssignments to set
	 */
	public void setProjectAssignments(Set<ProjectAssignment> projectAssignments) {
		this.projectAssignments = projectAssignments;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setCurrentState(State currentState) {
		this.currentState = currentState;
	}

	public void setWorkflow(Workflow workflow) {
		this.workflow = workflow;
	}

	/**
	 * Add {@link HistoryEntry} to this Project
	 * 
	 * @param historyEntry
	 *            {@link HistoryEntry} to add
	 */
	public void addHistoryEntry(HistoryEntry historyEntry) {
		if (historyEntries == null) {
			historyEntries = new ArrayList<HistoryEntry>();
		}
		historyEntries.add(historyEntry);
	}

	/**
	 * Add {@link ProjectAssignment} to this Project
	 * 
	 * @param projectAssignment
	 *            {@link ProjectAssignment} to add
	 */
	public void addProjectAssignment(ProjectAssignment projectAssignment) {
		if (this.getProjectAssignments() == null) {
			this.setProjectAssignments(new HashSet<ProjectAssignment>());
		}
		projectAssignments.add(projectAssignment);
	}

	/**
	 * Remove {@link HistoryEntry} from this Project
	 * 
	 * @param historyEntry
	 *            {@link HistoryEntry} to remove
	 */
	public boolean removeHistoryEntry(HistoryEntry historyEntry) {
		return historyEntries.remove(historyEntry);
	}

	/**
	 * Remove {@link ProjectAssignment} from this Project
	 * 
	 * @param projectAssignment
	 *            {@link ProjectAssignment} to remove
	 */
	public boolean removeProjectAssignment(ProjectAssignment projectAssignment) {
		return projectAssignments.remove(projectAssignment);
	}

	/**
	 * @return the comments
	 */
	public List<Comment> getComments() {
		return comments;
	}

	/**
	 * @param comments
	 *            the comments to set
	 */
	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	/**
	 * Add {@link Comment} to this Project
	 * 
	 * @param comment
	 *            {@link Comment} to add
	 */
	public void addComment(Comment comment) {
		if (comments == null) {
			comments = new ArrayList<Comment>();
		}
		comments.add(comment);
	}

	/**
	 * Remove {@link Comment} from this Project
	 * 
	 * @param comment
	 *            {@link Comment} to remove
	 */
	public void removeComment(Comment comment) {
		comments.remove(comment);
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
		if (!(obj instanceof Project)) {
			return false;
		}
		Project other = (Project) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

	public Boolean isActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}
}
