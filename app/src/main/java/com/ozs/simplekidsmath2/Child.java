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
    private Integer  minparamdiv=1;
    private Integer  maxparamdiv=15;
    private Integer  minparammult=1;
    private Integer  maxparammult=10;
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
    private Integer  addNo2=0;
    private Integer  subNo2=0;
    private Integer  multNo2=0;
    private Integer  divNo2=0;
    private Long     addTime2=0L;
    private Long     subTime2=0L;
    private Long     multTime2=0L;
    private Long     divTime2=0L;
    private Integer  addG2=0;
    private Integer  subG2=0;
    private Integer  multG2=0;
    private Integer  divG2=0;
    private Long     lastScoreReset=0L;
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

    public Integer getMinparamdiv() {
        return minparamdiv;
    }

    public void setMinparamdiv(Integer minparamdiv) {
        this.minparamdiv = minparamdiv;
    }

    public Integer getMaxparamdiv() {
        return maxparamdiv;
    }

    public void setMaxparamdiv(Integer maxparamdiv) {
        this.maxparamdiv = maxparamdiv;
    }

    public Integer getMinparammult() {
        return minparammult;
    }

    public void setMinparammult(Integer minparammult) {
        this.minparammult = minparammult;
    }

    public Integer getMaxparammult() {
        return maxparammult;
    }

    public void setMaxparammult(Integer maxparammult) {
        this.maxparammult = maxparammult;
    }

    public Integer getAddNo2() {
        return addNo2;
    }

    public void setAddNo2(Integer addNo2) {
        this.addNo2 = addNo2;
    }

    public Integer getSubNo2() {
        return subNo2;
    }

    public void setSubNo2(Integer subNo2) {
        this.subNo2 = subNo2;
    }

    public Integer getMultNo2() {
        return multNo2;
    }

    public void setMultNo2(Integer multNo2) {
        this.multNo2 = multNo2;
    }

    public Integer getDivNo2() {
        return divNo2;
    }

    public void setDivNo2(Integer divNo2) {
        this.divNo2 = divNo2;
    }

    public Long getAddTime2() {
        return addTime2;
    }

    public void setAddTime2(Long addTime2) {
        this.addTime2 = addTime2;
    }

    public Long getSubTime2() {
        return subTime2;
    }

    public void setSubTime2(Long subTime2) {
        this.subTime2 = subTime2;
    }

    public Long getMultTime2() {
        return multTime2;
    }

    public void setMultTime2(Long multTime2) {
        this.multTime2 = multTime2;
    }

    public Long getDivTime2() {
        return divTime2;
    }

    public void setDivTime2(Long divTime2) {
        this.divTime2 = divTime2;
    }

    public Integer getAddG2() {
        return addG2;
    }

    public void setAddG2(Integer addG2) {
        this.addG2 = addG2;
    }

    public Integer getSubG2() {
        return subG2;
    }

    public void setSubG2(Integer subG2) {
        this.subG2 = subG2;
    }

    public Integer getMultG2() {
        return multG2;
    }

    public void setMultG2(Integer multG2) {
        this.multG2 = multG2;
    }

    public Integer getDivG2() {
        return divG2;
    }

    public void setDivG2(Integer divG2) {
        this.divG2 = divG2;
    }

    public Long getLastScoreReset() {
        return lastScoreReset;
    }

    public void setLastScoreReset(Long lastScoreReset) {
        this.lastScoreReset = lastScoreReset;
    }
}
