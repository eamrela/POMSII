/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vodafone.poms.ii.helpers;

import java.math.BigInteger;

/**
 *
 * @author Amr
 */
public class CustomerSummary {
 
    private BigInteger totalPoValue;
    private BigInteger totalMdRecieved;
    private BigInteger totalMdDeserved;
    private BigInteger totalRemainingFromMd;
    private BigInteger totalMdValue;
    private BigInteger totalMdNotYetInvoiced;
    private BigInteger totalRemainingInPo;
    private BigInteger totalCommittedCost;

    public BigInteger getTotalPoValue() {
        return totalPoValue;
    }

    public void setTotalPoValue(BigInteger totalPoValue) {
        if(this.totalPoValue == null){
            this.totalPoValue = BigInteger.ZERO;
            this.totalPoValue = this.totalPoValue.add(totalPoValue);
        }else{
            this.totalPoValue = this.totalPoValue.add(totalPoValue);
        }
    }

    public BigInteger getTotalMdRecieved() {
        return totalMdRecieved;
    }

    public void setTotalMdRecieved(BigInteger totalMdRecieved) {
        if(this.totalMdRecieved == null){
            this.totalMdRecieved = BigInteger.ZERO;
            this.totalMdRecieved = this.totalMdRecieved.add(totalMdRecieved);
        }else{
            this.totalMdRecieved = this.totalMdRecieved.add(totalMdRecieved);
        }
    }

    public BigInteger getTotalMdDeserved() {
        return totalMdDeserved;
    }

    public void setTotalMdDeserved(BigInteger totalMdDeserved) {
        if(this.totalMdDeserved == null){
            this.totalMdDeserved = BigInteger.ZERO;
            this.totalMdDeserved = this.totalMdDeserved.add(totalMdDeserved);
        }else{
            this.totalMdDeserved = this.totalMdDeserved.add(totalMdDeserved);
        }
    }

    public BigInteger getTotalRemainingFromMd() {
        return totalRemainingFromMd;
    }

    public void setTotalRemainingFromMd(BigInteger totalRemainingFromMd) {
        if(this.totalRemainingFromMd == null){
            this.totalRemainingFromMd = BigInteger.ZERO;
            this.totalRemainingFromMd = this.totalRemainingFromMd.add(totalRemainingFromMd);
        }else{
            this.totalRemainingFromMd = this.totalRemainingFromMd.add(totalRemainingFromMd);
        }
    }

    public BigInteger getTotalMdValue() {
        return totalMdValue;
    }

    public void setTotalMdValue(BigInteger totalMdValue) {
        if(this.totalMdValue == null){
            this.totalMdValue = BigInteger.ZERO;
            this.totalMdValue = this.totalMdValue.add(totalMdValue);
        }else{
            this.totalMdValue = this.totalMdValue.add(totalMdValue);
        }
    }

    public BigInteger getTotalMdNotYetInvoiced() {
        if(totalMdRecieved!=null && totalMdValue!=null){
        totalMdNotYetInvoiced = totalMdRecieved.subtract(totalMdValue);
        }
        return totalMdNotYetInvoiced;
    }

    public void setTotalMdNotYetInvoiced(BigInteger totalMdNotYetInvoiced) {
        this.totalMdNotYetInvoiced = totalMdNotYetInvoiced;
    }

    public BigInteger getTotalRemainingInPo() {
        return totalRemainingInPo;
    }

    public void setTotalRemainingInPo(BigInteger totalRemainingInPo) {
         if(this.totalRemainingInPo == null){
            this.totalRemainingInPo = BigInteger.ZERO;
            this.totalRemainingInPo = this.totalRemainingInPo.add(totalRemainingInPo);
        }else{
            this.totalRemainingInPo = this.totalRemainingInPo.add(totalRemainingInPo);
        }
    }

    public BigInteger getTotalCommittedCost() {
        return totalCommittedCost;
    }

    public void setTotalCommittedCost(BigInteger totalCommittedCost) {
        this.totalCommittedCost = totalCommittedCost;
    }
    
    
    
}
