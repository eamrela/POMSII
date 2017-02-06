/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vodafone.poms.ii.helpers;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 *
 * @author Amr
 */
public class CustomerSummary {
 
    private BigDecimal totalPoValue;
    private BigDecimal totalMdRecieved;
    private BigDecimal totalMdDeserved;
    private BigDecimal totalRemainingFromMd;
    private BigDecimal totalMdValue;
    private BigDecimal totalMdNotYetInvoiced;
    private BigDecimal totalRemainingInPo;
    private BigDecimal totalCommittedCost;

    public BigDecimal getTotalPoValue() {
        return totalPoValue;
    }

    public void setTotalPoValue(BigDecimal totalPoValue) {
        if(this.totalPoValue == null){
            this.totalPoValue = BigDecimal.ZERO;
            this.totalPoValue = this.totalPoValue.add(totalPoValue);
        }else{
            this.totalPoValue = this.totalPoValue.add(totalPoValue);
        }
    }

    public BigDecimal getTotalMdRecieved() {
        return totalMdRecieved;
    }

    public void setTotalMdRecieved(BigDecimal totalMdRecieved) {
        if(this.totalMdRecieved == null){
            this.totalMdRecieved = BigDecimal.ZERO;
            this.totalMdRecieved = this.totalMdRecieved.add(totalMdRecieved);
        }else{
            this.totalMdRecieved = this.totalMdRecieved.add(totalMdRecieved);
        }
    }

    public BigDecimal getTotalMdDeserved() {
        return totalMdDeserved;
    }

    public void setTotalMdDeserved(BigDecimal totalMdDeserved) {
        if(this.totalMdDeserved == null){
            this.totalMdDeserved = BigDecimal.ZERO;
            this.totalMdDeserved = this.totalMdDeserved.add(totalMdDeserved);
        }else{
            this.totalMdDeserved = this.totalMdDeserved.add(totalMdDeserved);
        }
    }

    public BigDecimal getTotalRemainingFromMd() {
        return totalRemainingFromMd;
    }

    public void setTotalRemainingFromMd(BigDecimal totalRemainingFromMd) {
        if(this.totalRemainingFromMd == null){
            this.totalRemainingFromMd = BigDecimal.ZERO;
            this.totalRemainingFromMd = this.totalRemainingFromMd.add(totalRemainingFromMd);
        }else{
            this.totalRemainingFromMd = this.totalRemainingFromMd.add(totalRemainingFromMd);
        }
    }

    public BigDecimal getTotalMdValue() {
        return totalMdValue;
    }

    public void setTotalMdValue(BigDecimal totalMdValue) {
        if(this.totalMdValue == null){
            this.totalMdValue = BigDecimal.ZERO;
            this.totalMdValue = this.totalMdValue.add(totalMdValue);
        }else{
            this.totalMdValue = this.totalMdValue.add(totalMdValue);
        }
    }

    public BigDecimal getTotalMdNotYetInvoiced() {
        if(totalMdRecieved!=null && totalMdValue!=null){
        totalMdNotYetInvoiced = totalMdRecieved.subtract(totalMdValue);
        }
        return totalMdNotYetInvoiced;
    }

    public void setTotalMdNotYetInvoiced(BigDecimal totalMdNotYetInvoiced) {
        this.totalMdNotYetInvoiced = totalMdNotYetInvoiced;
    }

    public BigDecimal getTotalRemainingInPo() {
        return totalRemainingInPo;
    }

    public void setTotalRemainingInPo(BigDecimal totalRemainingInPo) {
         if(this.totalRemainingInPo == null){
            this.totalRemainingInPo = BigDecimal.ZERO;
            this.totalRemainingInPo = this.totalRemainingInPo.add(totalRemainingInPo);
        }else{
            this.totalRemainingInPo = this.totalRemainingInPo.add(totalRemainingInPo);
        }
    }

    public BigDecimal getTotalCommittedCost() {
        return totalCommittedCost;
    }

    public void setTotalCommittedCost(BigDecimal totalCommittedCost) {
        this.totalCommittedCost = totalCommittedCost;
    }
    
    
    
}
