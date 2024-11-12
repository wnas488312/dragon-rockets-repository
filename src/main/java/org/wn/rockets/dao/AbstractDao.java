package org.wn.rockets.dao;

/**
 * An interface, containing common methods for DAO interfaces.
 * @param <ID>      Type of identifier of a entity that will be stored.
 * @param <Entity>  Type of entity to be stored.
 */
public interface AbstractDao<ID, Entity> {

    /**
     * Adds new entity of given type to repository, or updates existing one. Entity name will be used as identifier.
     * @param entity Objects with data.
     */
    void save(Entity entity);

    /**
     * Removes every entity present in the store.
     */
    void removeAll();

    /**
     * Returns true if entity with given id exists in the store, false otherwise.
     * @param identifier    Identifier of a searched entity.
     * @return              True if exists.
     */
    boolean exists(ID identifier);
}
