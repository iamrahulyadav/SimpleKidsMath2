package com.ozs.simplekidsmath2;

import android.content.Context;

import android.content.SharedPreferences;
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
import java.io.UnsupportedEncodingException;
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

    private static ChildrenList instance = null;
    private static ArrayList<Child> childArrayList;
    private XmlPullParserFactory xmlPullParserFactory;
    static String  filename = "mydata.xml";
    static FileOutputStream outputStream;
    private Context myContext;
    private Integer startMenuId=122;
    private String m_selectedChildUUID="";

    public static ChildrenList getInstance()
    {
        if(instance==null)
            instance= new ChildrenList();
        return instance;
    }

    public void setContext(Context context)
    {
        myContext = context;
    }

    private ChildrenList()
    {
        childArrayList = new ArrayList<>();
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

                childidx.appendChild(doc.createTextNode(child.getChildId()));
                namex.appendChild(doc.createTextNode(child.getName()));
                imgnamex.appendChild(doc.createTextNode(child.getImgName()));
                createdatex.appendChild(doc.createTextNode(child.getCreateDate().toString()));
                updatedatex.appendChild(doc.createTextNode(child.getUpdateDate().toString()));
                childx.appendChild(childidx);
                childx.appendChild(namex);
                childx.appendChild(imgnamex);
                childx.appendChild(createdatex);
                childx.appendChild(updatedatex);

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
                }
                        Element eSelectedChildUUID = (Element) nListSE.item(0);
                        m_selectedChildUUID = eSelectedChildUUID.getTextContent();
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

                            Child child=new Child(childid,name);

                            child.setImgName(imgName);
                            Long lcreateDate=Long.parseLong(createDate);
                            child.setCreateDate(lcreateDate);
                            Long lupdateDate=Long.parseLong(updateDate);
                            child.setCreateDate(lupdateDate);

                            this.Add(child,false);
                            // childArrayList.add(child);
                        }
                    }
                    this.SaveData();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public Child GetChildByChildId(String childId){

            String childId1 = childId.toLowerCase().trim().replaceAll("[\\p{Cc}\\p{Cf}\\p{Co}\\p{Cn}]", "?");
            byte[] a = childId1.getBytes(StandardCharsets.UTF_16); // Java 7+ only

            for (int i = 0; i < childArrayList.size(); i++) {
                Child child = childArrayList.get(i);
                String childId2 = child.getChildId().toLowerCase().trim().replaceAll("[\\p{Cc}\\p{Cf}\\p{Co}\\p{Cn}]", "?");
                byte[] b = childId1.getBytes(StandardCharsets.UTF_16); // Java 7+ only
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