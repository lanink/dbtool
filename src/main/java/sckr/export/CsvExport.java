package sckr.export;

import sckr.config.Export;
import sckr.core.ExportDriver;

import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * CsvExport
 *
 * @version 0.1
 * @authors Hefa
 * @date 2019-05-30 13:48
 */

public class CsvExport extends ExportDriver{

    private static CsvExport instance;

    public static ExportDriver getInstance()
    {
        if (null == instance){
            instance = new CsvExport();
        }
        return instance;
    }

    public String export(Map<String, String> config ,List<Map<String, Object>> data) throws IOException
    {

        String table = config.get("table");
        String path = config.get("path");
        String[] header = config.get("cloums").split(",");

        String fileName = table + "_" +String.valueOf(System.currentTimeMillis());

        File file = createFile(path , fileName);
        
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
                file), "GB2312"), 1024);


        for (int i = 0; i < data.size(); i++) {
            Map<String, Object> map = data.get(i);
            if (i == 0) {
                writerHeader(writer,header);
            }
            writeRow(writer,map,header);
        }

        writer.flush();

        return file.getName();

    }

    private File createFile(String path , String name) throws IOException
    {
        String filePath;
        String end = path.substring(path.length() - 1);
        if (end.equals("/") || end.equals("\\")){
            filePath = path + name + "." + Export.CSV.getType();
        }else {
            filePath = path + "/" + name + "." + Export.CSV.getType();
        }

        File file = new File(filePath);

        File parent = file.getParentFile();

        if (null != parent && !parent.exists()){
            parent.mkdirs();
        }

        file.createNewFile();

        return file;
    }

    public void writerHeader(BufferedWriter writer , String[] header) throws IOException
    {
        StringBuffer buffer;
        for (int i = 0; i < header.length; i++) {
            buffer = new StringBuffer();
            buffer.append(header[i].toLowerCase() + ",");
            writer.write(buffer.toString());
        }

        writer.newLine();
    }
    
    public void writeRow(BufferedWriter writer , Map<String, Object> map, String[] header) throws IOException
    {
        StringBuffer buffer;

        for (int i = 0; i < header.length; i++) {
            buffer = new StringBuffer();
            buffer.append(map.get(header[i]) + ",");
            writer.write(buffer.toString());
        }

        writer.newLine();
    }
}
