package sckr;
import sckr.core.DBDriver;
import sckr.core.ExportDriver;
import sckr.tool.Help;
import sckr.tool.SqlBuilder;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Krdbt
 *
 * @notice 基础版本 请慎重使用 1、该版本未对sql注入进行校验
 *                          2、该版本未对高级sql语法支持和解析
 *                          3、该版本部分功能未测试（oracle部分）
 *                          4、该版本未对运行速度要求，没有使用多线程处理
 *
 * @version 0.1
 * @authors Hefa
 * @date 2019-05-30 08:55
 */

public class Krdbt {
    public static void main(String[] args)throws Exception {

        File file = getxmlPath(args);

        List<Map<String, String>> config = Help.readKrdbtXml(file);

        Map<String, String> exportconfig = config.get(config.size() - 1);


        for (int i = 0; i < config.size() - 1; i++) {
            Map<String, String> map = config.get(i);

            map.putAll(exportconfig);

            String sql = SqlBuilder.build(map.get("dbtype"), map.get("table"), map.get("cloums"), map.get("wheres"));

            DBDriver driver = DBDriver.getDriver(map.get("dbtype"));

            driver.init(map.get("host"),map.get("port"),map.get("database"),map.get("user"),map.get("password"));

            List<Map<String, Object>> list = driver.runSQl(sql);

            ExportDriver export = ExportDriver.getDriver(map.get("type"));

            export.export(map.get("path") , list);
        }

    }

    private static File getxmlPath(String[] args) throws Exception
    {
        String xmlPath;

        if (args.length > 0 && Help.isEmpty(args[0])){
            xmlPath = args[0];
        }else {
            xmlPath = "krdbt.xml";
        }

        File file = new File(xmlPath);

        if (null == file || !file.exists()){
            throw new Exception("please make sure the file exist");
        }

        return file;
    }

    private static void run(){

    }
}
