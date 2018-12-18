package org.han.ica.asd.c.businessrule.mocks;

import org.han.ica.asd.c.gamevalue.GAME_VALUE;

public class GenerateOrderMock {
    public int getReplacementValue(GAME_VALUE gameValue, int facilityId) {
        if (gameValue != null) {
            return 10;//wordt gedaan door een andere klasse
        }
        return 0;
    }
}
