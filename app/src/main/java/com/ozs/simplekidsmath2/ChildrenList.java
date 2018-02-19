package com.ozs.simplekidsmath2;

import android.content.Context;
import android.util.Log;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import android.os.Environment;

/**
 * Created by z_savion on 08/02/2018.
 */
public class ChildrenList {

    public final static String DEFAULT_CHILD= "0e4c99e6-401f-4300-bb23-d1dd6246796a";
    private static ChildrenList instance = null;
    private static ArrayList<Child> childArrayList;
    private XmlPullParserFactory xmlPullParserFactory;
    static String  filename = "mydata.xml";
    static FileOutputStream outputStream;
    private Context myContext;
    private Integer startMenuId=122;
    private String m_selectedChildUUID="";
    private ChildrenListPresentor m_clistPresentor=null;

    public static ChildrenList getInstance()
    {
        if(instance==null)
            instance= new ChildrenList();
        return instance;
    }

    public void setContext(Context context)
    {
        myContext = context;
        if (m_clistPresentor!=null) {
            m_clistPresentor.setContext(myContext);
        }
    }

    private ChildrenList()
    {
        childArrayList = new ArrayList<>(); m_clistPresentor=new ChildrenListPresentor(myContext);
    }

    public void Add(Child child)
    {
        Add(child,true);
    }

    public void Add(Child child,boolean withSave)
    {
        // Assign Menu Id
        childArrayList.add(child);
        int newMenuId=childArrayList.size()+startMenuId-1;
        child.setMenuId(newMenuId);

        if(withSave) {
            SaveData();
        }
    }

    public void SaveData() {
        try {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // root elements
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("ChildrenList");
            doc.appendChild(rootElement);

            // Childrens elements
            Element selectedChildUUID = doc.createElement("SelectedChildUUID");
            selectedChildUUID.appendChild(doc.createTextNode(m_selectedChildUUID));
            rootElement.appendChild(selectedChildUUID);
            Element childrensx = doc.createElement("Childrens");
            rootElement.appendChild(childrensx);

            for (int i = 0; i < childArrayList.size(); i++) {
                Child child = childArrayList.get(i);

                Element childx = doc.createElement("Child");
                Element childidx=doc.createElement("ChildId");
                Element namex = doc.createElement("Name");
                Element imgnamex = doc.createElement("ImgName");
                Element createdatex = doc.createElement("CreateDate");
                Element updatedatex = doc.createElement("UpdateDate");

                Element minparamx = doc.createElement("minParam");
                Element maxparamx=doc.createElement("maxParam");

                Element addNox = doc.createElement("addNo");
                Element subNox=doc.createElement("subNo");
                Element multNox = doc.createElement("multNo");
                Element divNox = doc.createElement("divNo");

                Element addTimex = doc.createElement("addTime");
                Element subTimex=doc.createElement("subTime");
                Element multTimex = doc.createElement("multTime");
                Element divTimex = doc.createElement("divTime");

                Element addGx = doc.createElement("addG");
                Element subGx=doc.createElement("subG");
                Element multGx = doc.createElement("multG");
                Element divGx = doc.createElement("divG");

                Element isAddx = doc.createElement("isAdd");
                Element isSubx=doc.createElement("isSub");
                Element isMultx = doc.createElement("isMult");
                Element isDivx = doc.createElement("isDiv");

                minparamx.appendChild(doc.createTextNode(child.getMinparam().toString()));
                maxparamx.appendChild(doc.createTextNode(child.getMaxparam().toString()));

                childidx.appendChild(doc.createTextNode(child.getChildId()));
                namex.appendChild(doc.createTextNode(child.getName()));
                imgnamex.appendChild(doc.createTextNode(child.getImgName()));
                createdatex.appendChild(doc.createTextNode(child.getCreateDate().toString()));
                updatedatex.appendChild(doc.createTextNode(child.getUpdateDate().toString()));

                addNox.appendChild(doc.createTextNode(child.getAddNo().toString()));
                subNox.appendChild(doc.createTextNode(child.getSubNo().toString()));
                multNox.appendChild(doc.createTextNode(child.getMultNo().toString()));
                divNox.appendChild(doc.createTextNode(child.getDivNo().toString()));

                addTimex.appendChild(doc.createTextNode(child.getAddTime().toString()));
                subTimex.appendChild(doc.createTextNode(child.getSubTime().toString()));
                multTimex.appendChild(doc.createTextNode(child.getMultTime().toString()));
                divTimex.appendChild(doc.createTextNode(child.getDivTime().toString()));

                addGx.appendChild(doc.createTextNode(child.getAddG().toString()));
                subGx.appendChild(doc.createTextNode(child.getSubG().toString()));
                multGx.appendChild(doc.createTextNode(child.getMultG().toString()));
                divGx.appendChild(doc.createTextNode(child.getDivG().toString()));

                isAddx.appendChild(doc.createTextNode(child.getAdd() ? "1":"0"));
                isSubx.appendChild(doc.createTextNode(child.getSub() ? "1":"0"));
                isMultx.appendChild(doc.createTextNode(child.getMult() ? "1":"0"));
                isDivx.appendChild(doc.createTextNode(child.getDiv() ? "1":"0"));

                childx.appendChild(childidx);
                childx.appendChild(namex);
                childx.appendChild(imgnamex);
                childx.appendChild(createdatex);
                childx.appendChild(updatedatex);

                childx.appendChild(minparamx);
                childx.appendChild(maxparamx);

                childx.appendChild(addNox);
                childx.appendChild(subNox);
                childx.appendChild(multNox);
                childx.appendChild(divNox);

                childx.appendChild(addTimex);
                childx.appendChild(subTimex);
                childx.appendChild(multTimex);
                childx.appendChild(divTimex);

                childx.appendChild(addGx);
                childx.appendChild(subGx);
                childx.appendChild(multGx);
                childx.appendChild(divGx);

                childx.appendChild(isAddx);
                childx.appendChild(isSubx);
                childx.appendChild(isMultx);
                childx.appendChild(isDivx);

                childrensx.appendChild(childx);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            DOMSource source = new DOMSource(doc);
            String newFileName = myContext.getApplicationInfo().dataDir+"/" + filename;
            StreamResult result = new StreamResult(new File(newFileName));

            transformer.transform(source, result);
            Log.d("XML Saved","Saved");

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        }

        if (MainActivity.TRACE_FLAG) {
            // Copy XML File to download Directory

            // For Debug, copy the file into Downloads Directory
            File rootDownload = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            String newFileName = myContext.getApplicationInfo().dataDir+"/" + filename;
            File root = new File(newFileName);
            File dst = new File(rootDownload, filename + ".txt");
            if (dst.exists())
            {   // Delete OLD File
                dst.delete();
            }
            if (root.exists()) {
                // Copy the file
                try (InputStream in = new FileInputStream(root)) {
                    try (OutputStream out = new FileOutputStream(dst)) {
                        // Transfer bytes from in to out
                        byte[] buf = new byte[1024];
                        int len;
                        while ((len = in.read(buf)) > 0) {
                            out.write(buf, 0, len);
                        }
                        out.flush();
                        out.close();
                        in.close();
                    } catch (Exception ex1) {
                        Log.e("copy XML", ex1.getMessage());
                        ex1.printStackTrace();
                    }
                } catch (Exception ex1) {
                    Log.e("Pic2Download", ex1.getMessage());
                    ex1.printStackTrace();
                }
            }
        }
    }
    public void LoadData(){
        try {
            String newFileName = myContext.getApplicationInfo().dataDir+"/" + filename;
            File fXmlFile = new File(newFileName);
            if (!fXmlFile.exists()){
                // No Data File
                // Insert default Child
                Child childx=new Child(DEFAULT_CHILD,"Child");
                this.Add(childx,false);
                CopyDefaultImage();
                childx.setImgName(DEFAULT_CHILD+".jpg");
                // Set the current child
                setSelectedChild(childx);
                SaveData();
                return;
            }
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            //optional, but recommended
            //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            //doc.getDocumentElement().normalize();

            // Get Selected Child UUID
            NodeList nListSE=doc.getDocumentElement().getElementsByTagName("SelectedChildUUID");
            if (nListSE.getLength()==1)
            {
                Node node=nListSE.item(0);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element eSelectedChildUUID = (Element) nListSE.item(0);
                    m_selectedChildUUID = eSelectedChildUUID.getTextContent();
                }
            }

            // Get ALL Childrens
            NodeList nList = doc.getDocumentElement().getElementsByTagName("Childrens");
            if (nList.getLength()==1)
            {
                Node node=nList.item(0);

                if (node.getNodeType() == Node.ELEMENT_NODE) {

                    childArrayList.clear();

                    Element nChildrens = (Element) nList.item(0);
                    NodeList nListC = nChildrens.getElementsByTagName("Child");
                    // Loop on all the childrens
                    for (int i = 0; i < nListC.getLength(); i++) {
                        Node nChild= nListC.item(i);
                        if (nChild.getNodeType() == Node.ELEMENT_NODE) {
                            Element eChild = (Element) nListC.item(i);
                            // Parse Child Data
                            String childid=eChild.getElementsByTagName("ChildId").item(0).getTextContent();
                            String name=eChild.getElementsByTagName("Name").item(0).getTextContent();
                            String imgName=eChild.getElementsByTagName("ImgName").item(0).getTextContent();
                            String createDate=eChild.getElementsByTagName("CreateDate").item(0).getTextContent();
                            String updateDate=eChild.getElementsByTagName("UpdateDate").item(0).getTextContent();

                            String minparam=eChild.getElementsByTagName("minParam").item(0).getTextContent();
                            String maxparam=eChild.getElementsByTagName("maxParam").item(0).getTextContent();


                            String addNo=eChild.getElementsByTagName("addNo").item(0).getTextContent();
                            String subNo=eChild.getElementsByTagName("subNo").item(0).getTextContent();
                            String multNo=eChild.getElementsByTagName("multNo").item(0).getTextContent();
                            String divNo=eChild.getElementsByTagName("divNo").item(0).getTextContent();

                            String addTime=eChild.getElementsByTagName("addTime").item(0).getTextContent();
                            String subTime=eChild.getElementsByTagName("subTime").item(0).getTextContent();
                            String multTime=eChild.getElementsByTagName("multTime").item(0).getTextContent();
                            String divTime=eChild.getElementsByTagName("divTime").item(0).getTextContent();

                            String addG=eChild.getElementsByTagName("addG").item(0).getTextContent();
                            String subG=eChild.getElementsByTagName("subG").item(0).getTextContent();
                            String multG=eChild.getElementsByTagName("multG").item(0).getTextContent();
                            String divG=eChild.getElementsByTagName("divG").item(0).getTextContent();

                            String isAdd=eChild.getElementsByTagName("isAdd").item(0).getTextContent();
                            String isSub=eChild.getElementsByTagName("isSub").item(0).getTextContent();
                            String isMult=eChild.getElementsByTagName("isMult").item(0).getTextContent();
                            String isDiv=eChild.getElementsByTagName("isDiv").item(0).getTextContent();


                            Child child=new Child(childid,name);

                            child.setImgName(imgName);
                            Long lcreateDate=Long.parseLong(createDate);
                            child.setCreateDate(lcreateDate);
                            Long lupdateDate=Long.parseLong(updateDate);
                            child.setCreateDate(lupdateDate);

                            child.setMinparam(Integer.parseInt(minparam));
                            child.setMaxparam(Integer.parseInt(maxparam));

                            child.setAddNo(Integer.parseInt(addNo));
                            child.setSubNo(Integer.parseInt(subNo));
                            child.setMultNo(Integer.parseInt(multNo));
                            child.setDivNo(Integer.parseInt(divNo));

                            child.setAddTime(Long.parseLong(addTime));
                            child.setSubTime(Long.parseLong(subTime));
                            child.setMultTime(Long.parseLong(multTime));
                            child.setDivTime(Long.parseLong(divTime));

                            child.setAddG(Integer.parseInt(addG));
                            child.setSubG(Integer.parseInt(subG));
                            child.setMultG(Integer.parseInt(multG));
                            child.setDivG(Integer.parseInt(divG));

                            child.setAdd(Integer.parseInt(isAdd)==1);
                            child.setSub(Integer.parseInt(isSub)==1);
                            child.setMult(Integer.parseInt(isMult)==1);
                            child.setDiv(Integer.parseInt(isDiv)==1);

                            this.Add(child,false);
                            // childArrayList.add(child);
                        }
                    }
                    if (childArrayList.size()==0)
                    {
                        Child childx=new Child(DEFAULT_CHILD,"Child");
                        this.Add(childx,false);
                        CopyDefaultImage();
                        childx.setImgName(DEFAULT_CHILD+".jpg");
                        // Set the current child
                        setSelectedChild(childx);
                    }
                    this.SaveData();
                    // Check to see if there are children at all
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void CopyDefaultImage(){
        m_clistPresentor.CopyDefaultImage(DEFAULT_CHILD);
    }

    public Child[] getChildrenArray(){
        Object[] objs=childArrayList.toArray();
        if (objs.length>0)
        {
            // Change the array type from object to child
            Child[] children=new Child[objs.length];
            for(int i=0;i<objs.length;i++)
            {
                Child child=(Child) objs[i];
                children[i]=child;
            }
            return children;
        }
        else
        {
            return null;
        }
    }
    public int GetMinMenuId()
    {
        int min=Integer.MAX_VALUE;
        for(int i = 0; i < this.childArrayList.size(); i++) {
            Child child = childArrayList.get(i);
            if (child.getMenuId()<min){
                min=child.getMenuId();
            }
        }
        return min;
    }

    public int GetMaxMenuId()
    {
        int max=Integer.MIN_VALUE;
        for(int i = 0; i < this.childArrayList.size(); i++) {
            Child child = childArrayList.get(i);
            if (child.getMenuId()>max){
                max=child.getMenuId();
            }
        }
        return max;
    }
    public int GetItemsSize() {
        return childArrayList.size();
    }

    public Child GetChildByMenuId(int menueId){
        for(int i=0;i<childArrayList.size();i++){
            Child child=childArrayList.get(i);
            if (child.getMenuId()==menueId) {
                return child;
            }
        }
        return null;
    }

    public boolean CompareChildren(Child child1,Child child2){
        String childId1 = child1.getChildId().toLowerCase().trim().replaceAll("[\\p{Cc}\\p{Cf}\\p{Co}\\p{Cn}]", "?");
        byte[] a = childId1.getBytes(StandardCharsets.UTF_16); // Java 7+ only
        String childId2 = child2.getChildId().toLowerCase().trim().replaceAll("[\\p{Cc}\\p{Cf}\\p{Co}\\p{Cn}]", "?");
        byte[] b = childId2.getBytes(StandardCharsets.UTF_16); // Java 7+ only

        if (a.length!=b.length){
            return false;
        }
        for(int j=0;j<a.length;j++){
            if (a[j]!=b[j]){
                return false;
            }
        }
        return true;
    }

    public Child GetChildByChildId(String childId){

            String childId1 = childId.toLowerCase().trim().replaceAll("[\\p{Cc}\\p{Cf}\\p{Co}\\p{Cn}]", "?");
            byte[] a = childId1.getBytes(StandardCharsets.UTF_16); // Java 7+ only

            for (int i = 0; i < childArrayList.size(); i++) {
                Child child = childArrayList.get(i);
                String childId2 = child.getChildId().toLowerCase().trim().replaceAll("[\\p{Cc}\\p{Cf}\\p{Co}\\p{Cn}]", "?");
                byte[] b = childId2.getBytes(StandardCharsets.UTF_16); // Java 7+ only
                boolean bIdentical=true;
                if (a.length!=b.length){
                    continue;
                }
                for(int j=0;j<a.length;j++){
                    if (a[j]!=b[j]){
                        bIdentical=false;
                        break;
                    }
                }
                if (!bIdentical)
                {
                    continue;
                }
                else{ // Match Found
                    return child;
                }
            }
            return null;
    }

    public Integer GetChildIndexByChildId(String childId){

        String childId1 = childId.toLowerCase().trim().replaceAll("[\\p{Cc}\\p{Cf}\\p{Co}\\p{Cn}]", "?");
        byte[] a = childId1.getBytes(StandardCharsets.UTF_16); // Java 7+ only

        for (int i = 0; i < childArrayList.size(); i++) {
            Child child = childArrayList.get(i);
            String childId2 = child.getChildId().toLowerCase().trim().replaceAll("[\\p{Cc}\\p{Cf}\\p{Co}\\p{Cn}]", "?");
            byte[] b = childId2.getBytes(StandardCharsets.UTF_16); // Java 7+ only
            boolean bIdentical=true;
            if (a.length!=b.length){
                continue;
            }
            for(int j=0;j<a.length;j++){
                if (a[j]!=b[j]){
                    bIdentical=false;
                    break;
                }
            }
            if (!bIdentical)
            {
                continue;
            }
            else{ // Match Found
                return i;
            }
        }
        return -1;
    }

    public void RemoveChild(Child child){
        Child selectedChild=getSelectedChild();
        boolean isSelected=false;
        selectedChild=getSelectedChild();
        if (selectedChild==null) {
            isSelected=true;
        }else{
            isSelected=this.CompareChildren(selectedChild,child);
        }
        Integer idx=GetChildIndexByChildId(child.getChildId());
        m_clistPresentor.DeletePic(child.getImgName());
        childArrayList.remove(child);
        boolean loadFlag=false;
        if (isSelected)
        {
            if (childArrayList.size()>0)
            {
                Child schild=childArrayList.get(0);
                setSelectedChild(schild);
            }
            else
            {
                // The Load will select the default item
                loadFlag=true;
            }
        }
        SaveData();
        if (loadFlag){
            LoadData();
        }
    }

    public void setSelectedChild(Child child){
        setSelectedChildUUID(child.getChildId());
    }

    public Child getSelectedChild(){
        return GetChildByChildId(getSelectedChildUUID());
    }

    public String getSelectedChildUUID() {
        return m_selectedChildUUID;
    }

    public void setSelectedChildUUID(String m_selectedChild) {
        if (!m_selectedChild.equals(this.m_selectedChildUUID)) {
            this.m_selectedChildUUID = m_selectedChild;
            SaveData();
        }
    }
}