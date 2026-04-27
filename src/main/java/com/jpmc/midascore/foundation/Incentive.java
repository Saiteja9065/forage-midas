package com.jpmc.midascore.foundation;

import lombok.Data;


public class Incentive {
    private float amount;

    public Incentive(){}

    public Incentive(float amount){
        this.amount = amount;
    }

    public float getAmount(){
        return amount;
    }
    public void setAmount(float amount){
        this.amount = amount;
    }
}
