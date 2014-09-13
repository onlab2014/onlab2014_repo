package hu.bme.aut.tomeesample.web;

import hu.bme.aut.tomeesample.model.ActionType;
import hu.bme.aut.tomeesample.model.State;
import hu.bme.aut.tomeesample.model.StateNavigationEntry;
import hu.bme.aut.tomeesample.model.Workflow;
import hu.bme.aut.tomeesample.service.ActionTypeService;
import hu.bme.aut.tomeesample.service.StateNavigationEntryService;
import hu.bme.aut.tomeesample.service.StateService;
import hu.bme.aut.tomeesample.service.WorkflowService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;

/**
 * @author Gergely Várkonyi
 */
@Named
@RequestScoped
public class StateManager {

	private static final Logger logger = Logger.getLogger(StateManager.class);

	@Inject
	StateService stateService;
	@Inject
	WorkflowService workflowService;
	@Inject
	ActionTypeService actionTypeService;
	@Inject
	StateNavigationEntryService stateNavigationEntryService;
	private List<State> stateList;

	private String name;
	private String description;

	private ActionType selectedActionType;
	private State selectedNextState;

	/**
	 * @return All state
	 */
	public List<State> getStateList() {
		if (getIdParam("parentId") == null) {
			stateList = stateService.findRootStatesByWorkflowId(getIdParam("workflowId"));
		} else {
			stateList = stateService.findChildrenByParentId(getIdParam("parentId"));
			if (stateList == null)
			{
				return new ArrayList<State>();
			}
		}
		logger.debug("States listed: " + stateList.size());
		logger.debug(stateList);
		return stateList;
	}

	/**
	 * Creates a state from the attributes and saves it, then adds it to the
	 * current workflow.
	 */
	public String addState() {
		// Get parameters
		Long workflowId = getIdParam("workflowId");
		Long parentId = getIdParam("parentId");

		try {
			logger.debug("Try to create new state for workflowID: " + workflowId);
			Workflow workflow = workflowService.findById(workflowId);

			// Create new state
			State newState = new State(name, description, false);

			// Set parent if parentId is not null
			if (parentId != null) {
				State parent = null;
				parent = stateService.findById(parentId);
				logger.debug("Parent: " + parent.toString());
				stateService.createWithParent(parent, newState);
			} else {
				// Persist newState
				stateService.create(newState);
				logger.debug("New state created: " + newState.toString());
			}

			// Add state to workflow
			workflowService.setWorkflowToState(workflow, newState);
		} catch (Exception e) {
			logger.debug("Error while creating state");
			logger.debug(e);
			e.printStackTrace();
		}

		return createReturnString();
	}

	/**
	 * Deletes the given state
	 * 
	 * @param state
	 *            State to delete
	 */
	public String deleteState(State state) {
		try {
			logger.debug("Deleting actionType: " + state.toString());
			stateService.removeDetached(state);
		} catch (Exception e) {
			logger.debug("Error while deleting actionType");
			logger.debug(e.getMessage());
			e.printStackTrace();
		}
		return createReturnString();
	}

	public String deleteActionTypeFromState(StateNavigationEntry stateNavigationEntry) {
		try {
			// remove actionType
			State state = stateNavigationEntry.getParent();
			state.removeNexState(stateNavigationEntry);
			// save changes
			stateService.update(state);
			stateNavigationEntryService.removeDetached(stateNavigationEntry);
		} catch (Exception e) {
			logger.debug("Error while deleting actionType from state");
			logger.debug(e.getMessage());
			e.printStackTrace();
		}
		return createReturnString();
	}

	public String addActionTypeToState(State state) {
		try {
			// Get selected actionType and state
			if (selectedActionType != null && selectedNextState != null) {

				StateNavigationEntry stateNavigationEntry = new
						StateNavigationEntry(selectedActionType, selectedNextState, state);

				state.addNextState(stateNavigationEntry);

				stateNavigationEntryService.create(stateNavigationEntry);
			}
		} catch (Exception e) {
			logger.debug("Error while adding actionType for state");
			logger.debug(e.getMessage());
			e.printStackTrace();
		}
		return createReturnString();
	}

	public String getNextStateName(ActionType actionType, State state) {
		try {
			List<StateNavigationEntry> navEntries = stateNavigationEntryService.findByParentId(state.getId());
			for (StateNavigationEntry stateNavigationEntry : navEntries) {
				State nextState = stateNavigationEntry.getNextState();
				State parent = stateNavigationEntry.getParent();

				ActionType nav_at = stateNavigationEntry.getActionType();
				Long nav_at_id = nav_at.getId();
				Long actual_at_id = actionType.getId();

				if (nav_at_id.equals(actual_at_id)) {
					return nextState.getName();
				}
			}
		} catch (Exception e) {
			logger.debug("Error while get next state name", e);
			logger.debug(e.getMessage());
		}
		return "Next state not found :(";
	}

	public void selectedActionTypeChanged(ValueChangeEvent e) {
		selectedActionType = (ActionType) e.getNewValue();
	}

	public void selectedNextStateChanged(ValueChangeEvent e) {
		selectedNextState = (State) e.getNewValue();
	}

	public List<ActionType> getActionTypeList(State state) {
		ArrayList<ActionType> actionTypes = new ArrayList<>();
		if (state == null) {
			return actionTypes;
		}
		for (StateNavigationEntry stateNavigationEntry : stateNavigationEntryService.findByParentId(state.getId())) {
			actionTypes.add(stateNavigationEntry.getActionType());
		}
		logger.debug(actionTypes);
		return actionTypes;
	}

	public List<StateNavigationEntry> getNavStateEntries(State state) {
		List<StateNavigationEntry> navEntries = stateNavigationEntryService.findByParentId(state.getId());
		return navEntries;
	}

	public Long getIdParam(String paramName) {
		FacesContext context = FacesContext.getCurrentInstance();
		Map<String, String> paramMap = context.getExternalContext().getRequestParameterMap();
		String param = paramMap.get(paramName);
		if (param != null && !"".equals(param)) {
			return Long.valueOf(param);
		} else {
			return null;
		}
	}

	public boolean isInitial(State state) {
		return state.isInitial();
	}

	public String setInitial(State newInitState) {
		Long workflowId = getIdParam("workflowId");

		try {
			logger.debug("Try to get workflow with ID: " + workflowId);
			Workflow workflow = workflowService.findById(workflowId);
			State oldInitState = workflow.getInitialState();
			oldInitState.setInitial(false);
			stateService.update(oldInitState);
			newInitState.setInitial(true);
			stateService.update(newInitState);
			logger.debug("Initial state setting ended.");
		} catch (IllegalArgumentException illExc) {
			logger.warn("There was no initial state.");
			newInitState.setInitial(true);
			stateService.update(newInitState);
		} catch (Exception e) {
			logger.debug("Error while setting initial state");
			logger.debug(e.getMessage());
			e.printStackTrace();
		}
		return createReturnString();
	}

	public String createReturnString() {
		String returnString = "/auth/man/states.xhtml?workflowId=" + getIdParam("workflowId");
		Long parentId = getIdParam("parentId");
		if (parentId != null) {
			returnString += "&parentId=" + parentId;
		}
		return returnString;
	}

	public String getTitle() {
		Workflow workflow = workflowService.findById(getIdParam("workflowId"));
		String title = workflow.getName();
		Long parentId = getIdParam("parentId");
		if (parentId != null) {
			title += ": " + parentsName(parentId);
		}
		return title;
	}

	public String parentsName(Long parentId) {
		State parent = stateService.findById(parentId);
		String parentsName = "";
		while (parent != null) {
			parentsName += parent.getName() + " ";
			parent = parent.getParent();
		}
		return parentsName;
	}

	public List<State> getStateForWorkflow() {
		if (getIdParam("workflowId") != null) {
			stateList = stateService.findRootStatesByWorkflowId(getIdParam("workflowId"));
		} else {
			stateList = new ArrayList<>();
		}
		return stateList;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public ActionType getSelectedActionType() {
		return selectedActionType;
	}

	public void setSelectedActionType(ActionType selectedActionType) {
		this.selectedActionType = selectedActionType;
	}

	public State getSelectedNextState() {
		return selectedNextState;
	}

	public void setSelectedNextState(State selectedNextState) {
		this.selectedNextState = selectedNextState;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}