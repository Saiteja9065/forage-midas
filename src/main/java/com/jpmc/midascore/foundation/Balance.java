package com.jpmc.midascore.foundation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Balance {
    private float amount;

    public Balance() {
    }

    public Balance(float amount) {
        this.amount = amount;
    }

    @JsonProperty("balance")
    public float getAmount() {
        return amount;
    }

    @JsonProperty("balance")
    public void setAmount(float amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Balance {amount=" + amount + "}";
    }
}