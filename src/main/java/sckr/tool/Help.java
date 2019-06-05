package sckr.tool;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
                        value.deleteCharAt(value.length()-1);
                        content.put(nodeName , value.toString());
                    }
                }
                result.add(content);
            }

//            // export
//            NodeList export = document.getElementsByTagName("export");
//            NodeList childNodes = export.item(0).getChildNodes();
//            Map<String, String> content = new HashMap<>();
//            for (int i = 0; i < childNodes.getLength(); i++) {
//                Node item = childNodes.item(i);
//                if (item.getNodeType() == Node.ELEMENT_NODE){
//                    String nodeName = item.getNodeName();
//                    String nodeValue = item.getFirstChild().getNodeValue();
//                    content.put(nodeName, nodeValue);
//                }
//            }
//            result.add(content);

        }catch (ParserConfigurationException e){
            e.printStackTrace();
        }catch (SAXException|IOException e){
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args) {
        createFile(new File("/Users/hefa/Downloads/sql/sql"), "Tb","/Users/hefa/Downloads/tt");
    }

    public static void createFile(File file, String entityName , String path)
    {

        BufferedReader reader;
        StringBuffer content = null;

        try {
            reader = new BufferedReader(new FileReader(file));

            String line = null;
            boolean flag = false;
            for (;null != (line = reader.readLine());)
            {
                if (flag){
                    setColumn(line, content);
                }else {
                    content = setTable(line, entityName, content);
                    if (null != content)
                    {
                        flag = true;
                    }
                }


            }
            reader.close();

            String filePath;
            String end = path.substring(path.length() - 1);
            if (end.equals("/") || end.equals("\\")){
                filePath = path + entityName + ".java";
            }else {
                filePath = path + "/" + entityName + ".java";
            }

            File out = new File(filePath);

            File parent = file.getParentFile();

            if (null != parent && !parent.exists()){
                parent.mkdirs();
            }

            file.createNewFile();

            writeFile(content, out);

        }catch (Exception e){
            e.printStackTrace();
        }
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

    private static StringBuffer setTable(String s, String entityName, StringBuffer content)
    {
        Pattern pattern = Pattern.compile("CREATE TABLE T\\d+\\(", Pattern.CASE_INSENSITIVE);

        Matcher matcher = pattern.matcher(s);

        if (matcher.find()){
            String[] split = s.split(" ");
            String table = split[split.length - 1];

            if (table.endsWith("(")){
                table = table.substring(0,table.length()-1);
            }

            if (null == content)
            {
                content = new StringBuffer();
            }

            content.append("package krdc;\n\r\n\rimport lombok.Data;\n\r\n\r" +
                    "import javax.persistence.Column;\n\r" +
                    "import javax.persistence.Entity;\n\r" +
                    "import javax.persistence.Id;\n\r" +
                    "import javax.persistence.Table;\n\r" +
                    "import java.util.Date;\n\r" +
                    "@Data\n\r@Entity\n\r@Table(name = \""+table+"\" )\n\rpublic class "+entityName+"\n\r{\n\r");

            return content;
        }

        return null;
    }


    private static StringBuffer setColumn(String s, StringBuffer content)
    {
        if (s.indexOf("comment") > -1){
            String[] comment = s.split("comment");
            String[] colums = comment[0].split(" ");
            String column = getCloumn(colums[0]);
            String mark = getComment(comment[1]);
            String type = getType(comment[0]);
            String field = getField(column);
            content.append("\t/*"+mark+"*/\n\r\t@Column(name = \""+column+"\")\n\r\tprivate "+type+" "+field+";\n\r");
        }else {
            content.append("\n\r}");
        }


        return content;
    }

    private static String getCloumn(String column)
    {
        return column.trim();
    }


    private static String getComment(String comment)
    {
        comment = comment.trim();

        if (comment.endsWith(","))
        {
            comment = comment.substring(0, comment.length() - 1);
        }

        if (comment.startsWith("\"") || comment.startsWith("'"))
        {
            comment = comment.substring(1);
        }

        if (comment.endsWith("\"") || comment.endsWith("'"))
        {
            comment = comment.substring(0, comment.length() - 1);
        }

        return comment;
    }

    private static String getType(String s)
    {
        String[] split = s.split(" ");
        String type = split[1].trim().toLowerCase();

        if (type.indexOf("(") > -1){
            type = type.substring(0, type.indexOf("("));
        }

        switch (type){
            case "tinyint":
            case "smallint":
            case "mediumint":
            case "int":
                type = "Integer";
                break;
            case "bigint":
                type = "Long";
                break;
            case "float":
            case "double":
                type = "Double";
                break;
            case "decimal":
                type = "BigDecimal";
                break;
            case "char":
            case "varchar":
            case "tinytext":
            case "text":
            case "mediumtext":
            case "longtext":
                type = "String";
                break;
            case "date":
            case "time":
            case "datetime":
            case "timestamp":
                type = "Date";
                break;
            default:
                type = "String";
                break;
        }
        return type;
    }

    private static void writeFile(StringBuffer conten, File file) throws IOException
    {
        FileOutputStream fos = new FileOutputStream(file);
        OutputStreamWriter out = new OutputStreamWriter(fos, "UTF-8");
        out.write(conten.toString());
        out.close();
    }

    private static String getField(String s)
    {
        String field = "";
        if (s.indexOf("_") > -1)
        {
            String[] split = s.split("_");
            for (int i = 0; i < split.length; i++) {
                field += getField(split[i]);
            }
        }else {
            String start = s.substring(0, 1);
            field = start.toUpperCase() + s.substring(1);
        }
        return field;
    }


}
