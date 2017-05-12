package de.hepisec.gaecms.controller;

import com.googlecode.objectify.Key;
import static com.googlecode.objectify.ObjectifyService.ofy;
import de.hepisec.validation.Validation;
import de.hepisec.validation.ValidationException;
import java.util.List;

/**
 *
 * @author Hendrik Pilz
 * @param <T> entity class
 */
public abstract class BaseController<T> {
    private Class<T> clazz;
    
    public BaseController(Class<T> clazz) {
        this.clazz = clazz;
    }
    
    /**
     * Find an entity using its numeric id
     * 
     * @param id
     * @return the entity or null
     */
    public T findById(long id) {
        if (id == 0) {
            return null;
        }
        
        return ofy().load().type(clazz).id(id).now();
    }
    
    /**
     * Find an entity using its parent
     * 
     * @param parentClass
     * @param parentId
     * @param id
     * @return 
     */    
    public T findByIdWithParent(Class<?> parentClass, long parentId, long id) {
        if (parentId == 0 || id == 0) {
            return null;
        }
        
        return ofy().load().key(Key.create(Key.create(parentClass, parentId), clazz, id)).now();        
    }
    
    /**
     * Find an entity using its parent
     * 
     * @param parentClass
     * @param parentId
     * @param id
     * @return 
     */    
    public T findByIdWithParent(Class<?> parentClass, String parentId, long id) {
        if (parentId == null || id == 0) {
            return null;
        }
        
        return ofy().load().key(Key.create(Key.create(parentClass, parentId), clazz, id)).now();        
    }    
    
    /**
     * Find an entity using its String id
     * 
     * Attention: String ids and long ids are not the same: "123" != 123
     * 
     * @param id
     * @return the entity or null
     */
    public T findById(String id) {
        if (id == null) {
            return null;
        }
        
        return ofy().load().type(clazz).id(id).now();
    }
    
    /**
     * Find an entity using its parent
     * 
     * @param parentClass
     * @param parentId
     * @param id
     * @return 
     */
    public T findByIdWithParent(Class<?> parentClass, long parentId, String id) {
        if (parentId == 0 || id == null) {
            return null;
        }
        
        return ofy().load().key(Key.create(Key.create(parentClass, parentId), clazz, id)).now();        
    }
    
    /**
     * Find an entity using its parent
     * 
     * @param parentClass
     * @param parentId
     * @param id
     * @return 
     */    
    public T findByIdWithParent(Class<?> parentClass, String parentId, String id) {
        if (parentId == null || id == null) {
            return null;
        }
        
        return ofy().load().key(Key.create(Key.create(parentClass, parentId), clazz, id)).now();        
    }      
    
    /**
     * Get limit entries starting at offset
     * 
     * @param offset
     * @param limit
     * @return List of entries with a maximum of limit entries
     */
    public List<T> findAll(int offset, int limit) {
        return ofy().load().type(clazz).limit(limit).offset(offset).list();
    }    
    
    /**
     * Save the entity in the datastore
     * 
     * @param entity
     * @return the Key of the stored entity
     * @throws de.hepisec.validation.ValidationException
     */
    public Key<T> save(T entity) throws ValidationException {
        Validation validation = new Validation();
        validation.validate(entity);
        return ofy().save().entity(entity).now();
    }
    
    /**
     * Delete an entity using its numeric id
     * 
     * @param id 
     */
    public void delete(long id) {
        if (id == 0) {
            return;
        }
        
        ofy().delete().type(clazz).id(id).now();
    }
    
    /**
     * Delete an entity using its String id
     * 
     * Attention: String ids and long ids are not the same: "123" != 123
     * 
     * @param id 
     */
    public void delete(String id) {
        ofy().delete().type(clazz).id(id).now();
    }
    
    /**
     * Delete the entity
     * 
     * @param entity 
     */
    public void delete(T entity) {
        ofy().delete().entity(entity).now();
    }
       
    /**
     * Count all the entities of type T
     * 
     * @return the number of entities
     */
    public int count() {
        return ofy().load().type(clazz).count();
    }
}
