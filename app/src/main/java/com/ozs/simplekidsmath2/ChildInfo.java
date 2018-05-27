package com.ozs.simplekidsmath2;

public class ChildInfo {

    private Integer  addTotal=0;
    private Integer  subTotal=0;
    private Integer  multTotal=0;
    private Integer  divTotal=0;
    private Long     addTime=0L;
    private Long     subTime=0L;
    private Long     multTime=0L;
    private Long     divTime=0L;
    private Integer  addGood=0;
    private Integer  subGood=0;
    private Integer  multGood=0;
    private Integer  divGood=0;
    private Long     lastScoreReset=0L;


    public Integer getAddTotal() {
        return addTotal;
    }

    public void setAddTotal(Integer addTotal) {
        this.addTotal = addTotal;
    }

    public Integer getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(Integer subTotal) {
        this.subTotal = subTotal;
    }

    public Integer getMultTotal() {
        return multTotal;
    }

    public void setMultTotal(Integer multTotal) {
        this.multTotal = multTotal;
    }

    public Integer getDivTotal() {
        return divTotal;
    }

    public void setDivTotal(Integer divTotal) {
        this.divTotal = divTotal;
    }

    public Long getAddTime() {
        return addTime;
    }

    public void setAddTime(Long addTime) {
        this.addTime = addTime;
    }

    public Long getSubTime() {
        return subTime;
    }

    public void setSubTime(Long subTime) {
        this.subTime = subTime;
    }

    public Long getMultTime() {
        return multTime;
    }

    public void setMultTime(Long multTime) {
        this.multTime = multTime;
    }

    public Long getDivTime() {
        return divTime;
    }

    public void setDivTime(Long divTime) {
        this.divTime = divTime;
    }

    public Integer getAddGood() {
        return addGood;
    }

    public void setAddGood(Integer addGood) {
        this.addGood = addGood;
    }

    public Integer getSubGood() {
        return subGood;
    }

    public void setSubGood(Integer subGood) {
        this.subGood = subGood;
    }

    public Integer getMultGood() {
        return multGood;
    }

    public void setMultGood(Integer multGood) {
        this.multGood = multGood;
    }

    public Integer getDivGood() {
        return divGood;
    }

    public void setDivGood(Integer divGood) {
        this.divGood = divGood;
    }

    public Integer getAddBad() {
        return (getAddTotal()-getAddGood());
    }
    public Integer getSubBad() {
        return (getSubTotal()-getSubGood());
    }
    public Integer getMultBad() {
        return (getMultTotal()-getMultGood());
    }
    public Integer getDivBad() {
        return (getDivTotal()-getDivGood());
    }

    public Long getLastScoreReset() {
        return lastScoreReset;
    }

    public void setLastScoreReset(Long lastScoreReset) {
        this.lastScoreReset = lastScoreReset;
    }
}
