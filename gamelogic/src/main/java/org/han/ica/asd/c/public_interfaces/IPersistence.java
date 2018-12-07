package org.han.ica.asd.c.public_interfaces;

import org.han.ica.asd.c.domain.Order;

public interface IPersistence {
    void saveOrder(Order order);
}
