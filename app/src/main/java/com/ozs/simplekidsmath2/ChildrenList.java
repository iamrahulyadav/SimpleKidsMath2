package com.ozs.simplekidsmath2;

import android.content.Context;
import android.util.Log;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
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

    private XmlPullParserFactory xmlPullParserFactory;
    static String  filename = "mydata.xml";
    static FileOutputStream outputStream;
    static String newline = "\r\n";

    private Context myContext;
    private static ChildrenList instance = null;
    private static ArrayList<Child> childArrayList;

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
        childArrayList.add(child);
        SaveData();
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
            Element childrensx = doc.createElement("Childrens");
            rootElement.appendChild(childrensx);

            for (int i = 0; i < childArrayList.size(); i++) {
                Child child = childArrayList.get(i);

                Element childx = doc.createElement("Child");
                Element namex = doc.createElement("Name");
                Element imgnamex = doc.createElement("ImgName");
                Element createdatex = doc.createElement("CreateDate");
                Element updatedatex = doc.createElement("UpdateDate");

                namex.appendChild(doc.createTextNode(child.getName()));
                imgnamex.appendChild(doc.createTextNode(child.getImgName()));
                createdatex.appendChild(doc.createTextNode(child.getCreateDate().toString()));
                updatedatex.appendChild(doc.createTextNode(child.getUpdateDate().toString()));
                childx.appendChild(namex);
                childx.appendChild(imgnamex);
                childx.appendChild(createdatex);
                childx.appendChild(updatedatex);

                childrensx.appendChild(childx);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
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
    }
    public void LoadData(){
        try {

            String newFileName = myContext.getApplicationInfo().dataDir+"/" + filename;
            File fXmlFile = new File(newFileName);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            //optional, but recommended
            //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            //doc.getDocumentElement().normalize();

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
                        Node nChild= nListC.item(0);
                        if (nChild.getNodeType() == Node.ELEMENT_NODE) {
                            Element eChild = (Element) nListC.item(0);
                            // Parse Child Data

                            String name=eChild.getElementsByTagName("Name").item(0).getTextContent();
                            String imgName=eChild.getElementsByTagName("ImgName").item(0).getTextContent();
                            String createDate=eChild.getElementsByTagName("CreateDate").item(0).getTextContent();
                            String updateDate=eChild.getElementsByTagName("UpdateDate").item(0).getTextContent();

                            Child child=new Child(name);
                            child.setImgName(imgName);
                            Long lcreateDate=Long.parseLong(createDate);
                            child.setCreateDate(lcreateDate);
                            Long lupdateDate=Long.parseLong(updateDate);
                            child.setCreateDate(lupdateDate);

                            childArrayList.add(child);
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}