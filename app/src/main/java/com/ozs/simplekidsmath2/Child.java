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

    public Child(String name) {
        UUID uuid = UUID.randomUUID();
        String randomUUIDString = uuid.toString();
        this.childId = randomUUIDString;
        this.setName(name);
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

}
