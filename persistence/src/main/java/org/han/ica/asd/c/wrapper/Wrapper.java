package org.han.ica.asd.c.wrapper;

import org.han.ica.asd.c.model.IModel;
import org.han.ica.asd.c.model.dao_model.IDaoModel;
import org.han.ica.asd.c.model.domain_objects.IDomainModel;

import java.io.InvalidObjectException;

public abstract class Wrapper {
    public IModel startWrapping(IModel model) throws InvalidObjectException{
        if(model instanceof IDaoModel){
            return wrapToDomainModel((IDaoModel) model);
        }
        else if(model instanceof IDomainModel){
            return wrapToDaoModel((IDomainModel) model);
        }
        else {
            throw new InvalidObjectException("Object does not implement IDomainModel or IDaoModel");
        }
    }

    abstract IDaoModel wrapToDaoModel(IDomainModel model);
    abstract IDomainModel wrapToDomainModel(IDaoModel model);
}
