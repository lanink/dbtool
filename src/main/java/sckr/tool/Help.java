package sckr.tool;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Help
 *
 * @version 0.1
 * @authors Hefa
 * @date 2019-05-30 09:24
 */

public class Help {

    // 仅读取特定 krdbt 内容格式的 xml 文件
    public static List<Map<String, String>> readKrdbtXml(File file){
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        List<Map<String, String>> result = new ArrayList<>();

        try{
            DocumentBuilder documentBuilder = builderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);

            // source
            NodeList sources = document.getElementsByTagName("source");
            for (int i = 0; i < sources.getLength(); i++) {
                Node source = sources.item(i);
                Map<String, String> content = new HashMap<>();
                // 读取属性
                NamedNodeMap nodeMap = source.getAttributes();
                for (int j = 0; j < nodeMap.getLength(); j++) {
                    Node item = nodeMap.item(j);
                    String nodeName = item.getNodeName();
                    String nodeValue = item.getNodeValue();
                    content.put(nodeName , nodeValue);
                }
                // 读取子节点 cloums 和 wheres
                NodeList childNodes = source.getChildNodes();
                for (int j = 0; j < childNodes.getLength(); j++) {
                    Node node = childNodes.item(j);
                    if (node.getNodeType() == Node.ELEMENT_NODE){
                        String nodeName = node.getNodeName();
                        String nodeValue = node.getTextContent().trim();
                        String[] split = nodeValue.split(" ");
                        StringBuffer value = new StringBuffer();
                        for (int k = 0; k < split.length; k++) {
                            if (isEmpty(split[k])) continue;
                            value.append(split[k].trim() + ",");
                        }
                        content.put(nodeName , value.toString());
                    }
                }
                result.add(content);
            }

            // export
            NodeList export = document.getElementsByTagName("export");
            NodeList childNodes = export.item(0).getChildNodes();
            Map<String, String> content = new HashMap<>();
            for (int i = 0; i < childNodes.getLength(); i++) {
                Node item = childNodes.item(i);
                if (item.getNodeType() == Node.ELEMENT_NODE){
                    String nodeName = item.getNodeName();
                    String nodeValue = item.getFirstChild().getNodeValue();
                    content.put(nodeName, nodeValue);
                }
            }
            result.add(content);
        }catch (ParserConfigurationException e){
            e.printStackTrace();
        }catch (SAXException|IOException e){
            e.printStackTrace();
        }
        return result;
    }

    public static boolean isEmpty(String s)
    {
        int strLen;
        if (s != null && (strLen = s.length()) != 0) {
            for(int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(s.charAt(i))) {
                    return false;
                }
            }
        }
        return true;
    }


}
