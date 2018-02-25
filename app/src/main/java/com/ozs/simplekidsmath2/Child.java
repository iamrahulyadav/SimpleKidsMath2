package com.ozs.simplekidsmath2;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by z_savion on 08/02/2018.
 */

public class Child {

    private String   childId;
    private long     createdate;
    private long     updatedate;
    private String   name;
    private String   imgName;
    private Integer  menuId;
    private Integer  minparam=1;
    private Integer  maxparam=20;
    private Integer  addNo=0;
    private Integer  subNo=0;
    private Integer  multNo=0;
    private Integer  divNo=0;
    private Long     addTime=0L;
    private Long     subTime=0L;
    private Long     multTime=0L;
    private Long     divTime=0L;
    private Integer  addG=0;
    private Integer  subG=0;
    private Integer  multG=0;
    private Integer  divG=0;
    private Boolean  isAdd=true;
    private Boolean  isSub=true;
    private Boolean  isMult=true;
    private Boolean  isDiv=true;
    private Boolean  isAllowMinusResult=false;


    public Child(String name) {
        UUID uuid = UUID.randomUUID();
        String randomUUIDString = uuid.toString();
        this.childId = randomUUIDString;
        this.setName(name);
        Init();
    }
    public Child(String uuid,String name) {
        this.childId = uuid;
        this.setName(name);
        Init();
    }

    protected void Init()
    {
        this.createdate =new Date().getTime();
        this.updatedate = this.createdate;
        this.menuId=0;
    }

    public String getChildId() {
        return childId;
    }

    public Integer getMenuId() {
        return menuId;
    }
    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }
    private void setUpdateDatex() {
        this.updatedate =new Date().getTime();
    }
    public Long getCreateDate()
    {
        return this.createdate;
    }
    public Long getUpdateDate()
    {
        return this.updatedate;
    }
    public String getCreateDateStr()
    {
        Date dt= new Date();
        dt.setTime(this.createdate);
        return new SimpleDateFormat("yyMMddHHmmssZ").format(dt);
    }
    public String getUpdateDateStr()
    {
        Date dt= new Date();
        dt.setTime(this.updatedate);
        return new SimpleDateFormat("yyMMddHHmmssZ").format(dt);
    }
    public void setCreateDate(long createdate) {
        this.createdate = createdate;
    }
    public void setUpdateDate(long updatedate) {
        this.createdate = updatedate;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public String toString()
    {
        return this.getName();
    }

    public Integer getMinparam() {
        return minparam;
    }

    public void setMinparam(Integer minparam) {
        this.minparam = minparam;
    }

    public Integer getMaxparam() {
        return maxparam;
    }

    public void setMaxparam(Integer maxparam) {
        this.maxparam = maxparam;
    }

    public Integer getAddNo() {
        return addNo;
    }

    public void setAddNo(Integer addNo) {
        this.addNo = addNo;
    }

    public Integer getSubNo() {
        return subNo;
    }

    public void setSubNo(Integer subNo) {
        this.subNo = subNo;
    }

    public Integer getMultNo() {
        return multNo;
    }

    public void setMultNo(Integer multNo) {
        this.multNo = multNo;
    }

    public Integer getDivNo() {
        return divNo;
    }

    public void setDivNo(Integer divNo) {
        this.divNo = divNo;
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

    public Integer getAddG() {
        return addG;
    }

    public void setAddG(Integer addG) {
        this.addG = addG;
    }

    public Integer getSubG() {
        return subG;
    }

    public void setSubG(Integer subG) {
        this.subG = subG;
    }

    public Integer getMultG() {
        return multG;
    }

    public void setMultG(Integer multG) {
        this.multG = multG;
    }

    public Integer getDivG() {
        return divG;
    }

    public void setDivG(Integer divG) {
        this.divG = divG;
    }

    public Boolean getAdd() {
        return isAdd;
    }

    public void setAdd(Boolean add) {
        isAdd = add;
    }

    public Boolean getSub() {
        return isSub;
    }

    public void setSub(Boolean sub) {
        isSub = sub;
    }

    public Boolean getMult() {
        return isMult;
    }

    public void setMult(Boolean mult) {
        isMult = mult;
    }

    public Boolean getDiv() {
        return isDiv;
    }

    public void setDiv(Boolean div) {
        isDiv = div;
    }

    public Boolean getAllowMinusResult() {
        return isAllowMinusResult;
    }

    public void setAllowMinusResult(Boolean allowMinusResult) {
        isAllowMinusResult = allowMinusResult;
    }
}
