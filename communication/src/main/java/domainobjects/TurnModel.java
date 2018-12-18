package domainobjects;

import java.io.Serializable;

public class TurnModel implements Serializable {
    private int bestelling;

    public TurnModel(int bestelling) {
        this.bestelling = bestelling;
    }
}
