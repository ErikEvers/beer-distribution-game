package org.han.ica.asd.c.wrapper;

import org.han.ica.asd.c.model.dao_model.IDaoModel;
import org.han.ica.asd.c.model.domain_objects.IDomainModel;

public class RoundWrapper implements Wrapper {

    @Override
    public IDaoModel wrapToDaoModel(IDomainModel model) {
        return null;
    }

    @Override
    public IDomainModel wrapToDomainModel(IDaoModel model) {
        return null;
    }
}
