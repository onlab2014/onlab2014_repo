/**
 * ActionService.java
 */
package hu.bme.aut.tomeesample.service;


import hu.bme.aut.tomeesample.model.Action;
import javax.persistence.*;
import java.util.List;
import javax.ejb.*;
/**
 * @author Imre Szekeres
 * @version "%I%, %G%"
 */
@LocalBean
public class ActionService {

	@PersistenceContext
	EntityManager em;
	
	public void create(Action action){
		em.persist(action);
	}
	
	public void update(Action action){
		em.merge(action);
	}
	
	public void remove(Action action){
		em.remove(action);
	}
	
	public List<Action> findAll(){
		return em.createNamedQuery("Action.findAll", Action.class).getResultList();
	}
	
	public Action findById(Long id){
		try{
			TypedQuery<Action> select =  em.createNamedQuery("Action.findById", Action.class);
			select.setParameter("id", id);
			return select.getSingleResult();
		}catch(Exception e){
			return null;
		}
	}
	
	public Action findByType(Long typeId){
		try{
			TypedQuery<Action> select =  em.createNamedQuery("Action.findById", Action.class);
			select.setParameter("typeId", typeId);
			return select.getSingleResult();
		}catch(Exception e){
			return null;
		}
	}
}
