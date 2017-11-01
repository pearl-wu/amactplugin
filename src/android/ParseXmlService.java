package com.bais.amactplugin;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by pearl on 2017/11/1.
 */

public class ParseXmlService {
    public HashMap<String, String> parseXml(InputStream inStream) throws Exception{
        HashMap<String, String> hashMap = new HashMap<String, String>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();// 實例化一個文檔
        DocumentBuilder builder = factory.newDocumentBuilder();// 通過文檔獲取一個文檔
        Document document = builder.parse(inStream);// 通過文檔通過文檔構建器構建一個文檔實例
        Element root = document.getDocumentElement();//獲取XML文件根節點
        NodeList childNodes = root.getChildNodes();
        for (int j = 0; j < childNodes.getLength(); j++){
            Node childNode = (Node) childNodes.item(j);
            if (childNode.getNodeType() == Node.ELEMENT_NODE){
                Element childElement = (Element) childNode;
                if ("version".equals(childElement.getNodeName())){
                    hashMap.put("version",childElement.getFirstChild().getNodeValue());
                }else if (("name".equals(childElement.getNodeName()))){
                    hashMap.put("name",childElement.getFirstChild().getNodeValue());
                }else if (("url".equals(childElement.getNodeName()))){
                    hashMap.put("url",childElement.getFirstChild().getNodeValue());
                }
            }
        }
        return hashMap;
    }
}
