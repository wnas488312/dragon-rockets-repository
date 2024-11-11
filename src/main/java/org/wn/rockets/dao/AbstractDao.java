package org.wn.rockets.dao;

/**
 * An interface, containing common methods for DAO interfaces.
 * @param <Entity>  Type of entity to be stored.
 * @param <ID>      Type of entity identifier.
 * @param <Status>  Type of status enum (used only in changeStatus method).
 */
public sealed interface AbstractDao<Entity, ID, Status> permits RocketsDao, MissionsDao{

    /**
     * Adds new entity of given type to repository. Entity name will be used as identifier.
     * @param entity Objects with data.
     */
    void addNew(Entity entity);

    /**
     * Changes status of given entity.
     * @param identifier    Identifier of a given entity.
     * @param status        Status of entity to be assigned.
     */
    void changeStatus(ID identifier, Status status);
}
