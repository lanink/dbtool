package sckr.core;

import sckr.config.Export;
import sckr.export.CsvExport;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * ExportDriver
 *
 * @version 0.1
 * @authors Hefa
 * @date 2019-05-30 13:45
 */

public abstract class ExportDriver {

    public static ExportDriver getDriver(String type) throws ClassNotFoundException
    {
        String t = type.toLowerCase();
        if (t.equals(Export.CSV.getType())){
            return CsvExport.getInstance();
        }else {
            throw new ClassNotFoundException(type + " is not supported！");
        }
    }

    public abstract void export(String path, List<Map<String, Object>> data) throws IOException;

}
