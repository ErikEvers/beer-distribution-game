package org.han.ica.asd.c.wrapper;

import org.han.ica.asd.c.model.dao_model.IDaoModel;
import org.han.ica.asd.c.model.domain_objects.IDomainModel;

public interface IWrapper <T > {
    T startWrapping(T model);
    T wrapToDaoModel(IDomainModel model);
    T wrapToDomainModel(IDaoModel model);
}
