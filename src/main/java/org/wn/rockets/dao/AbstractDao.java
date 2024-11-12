package org.wn.rockets.dao;

/**
 * An interface, containing common methods for DAO interfaces.
 * @param <Entity>  Type of entity to be stored.
 */
public interface AbstractDao<Entity> {

    /**
     * Adds new entity of given type to repository, or updates existing one. Entity name will be used as identifier.
     * @param entity Objects with data.
     */
    void save(Entity entity);

    /**
     * Removes every entity present in the store.
     */
    void removeAll();
}
