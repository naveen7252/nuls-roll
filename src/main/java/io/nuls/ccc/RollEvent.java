package io.nuls.ccc;

import io.nuls.ccc.model.RollResult;
import io.nuls.contract.sdk.Event;

public class RollEvent implements Event {

    private RollResult rollResult;

    public RollEvent(RollResult rollResult){
        this.rollResult = rollResult;
    }
    public RollResult getRollResult() {
        return rollResult;
    }

    public void setRollResult(RollResult rollResult) {
        this.rollResult = rollResult;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RollEvent rollEvent = (RollEvent) o;

        return rollResult.equals(rollEvent.rollResult);
    }

    @Override
    public int hashCode() {
        return rollResult.hashCode();
    }

    @Override
    public String toString() {
        return "RollEvent{" +
                "rollResult:" + rollResult +
                '}';
    }
}
