package sckr;
import sckr.core.DBDriver;
import sckr.core.ExportDriver;
import sckr.tool.Help;
import sckr.tool.Show;
import sckr.tool.SqlBuilder;

import java.io.Console;
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
 *                          5、未严格统一调用和设计分布
 *
 * @version 0.1
 * @authors Hefa
 * @date 2019-05-30 08:55
 */

public class Krdbt {
    public static void main(String[] args)throws Exception {

        Console console = System.console();

        if (null != console) {
            System.out.println("欢迎使用本软件");
            try{
                run(console);
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("发生了错误：" +  e.getCause());
            }
        }else{
            System.err.println("please open in naturo command line！");
        }

    }

    private static File getxmlPath(String[] args) throws Exception
    {
        String xmlPath;

        if (null != args && args.length > 0 && Help.isEmpty(args[0])){
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


    private static void run(Console console) throws Exception
    {

        List<Map<String, String>> configs;

        Map<String, String> config;

        while (true) {

            configs = rederConfig();

            Show.showConfig(configs);

            // 选择配置
            config = selectConfig(configs , console);

            if (null == config){
                Show.showExit();
                return;
            }

            // 输入条件
            Show.showFields(config);

            String condition = getCondition(config,console);

            if (null == config){
                Show.showExit();
                return;
            }

            // 查询导出
            Show.showExport();
            doRun(config , condition);
        }
    }


    private static String readCmd(Console console)
    {
        String cmd = console.readLine();

        // 校验 TODO

        return cmd;
    }


    private static void doRun(Map<String,String> config , String condition)
    {
        try{
            String sql = SqlBuilder.build(config.get("dbtype"), config.get("table"), config.get("cloums"), condition);

            DBDriver driver = DBDriver.getDriver(config.get("dbtype"));

            driver.init(config.get("host"),config.get("port"),config.get("database"),config.get("user"),config.get("password"));

            List<Map<String, Object>> list = driver.runSQl(sql);

            ExportDriver export = ExportDriver.getDriver(config.get("type"));

            String filename = export.export(config, list);

            System.out.println("文件 " + filename + " 已导出到 " + config.get("path") + " 目录");
        }catch (Exception e){
//            e.printStackTrace();
            System.out.println("导出失败" + e.getMessage());
        }

    }


    private static List<Map<String, String>> rederConfig() throws Exception
    {
        File file = getxmlPath(null);

        List<Map<String, String>> config = Help.readKrdbtXml(file);

        return config;
    }


    private static Map<String, String> selectConfig(List<Map<String, String>> configs , Console console)
    {
        String number;
        while (true){
            number = readCmd(console);

            if (number.toLowerCase().equals("exit")){
                break;
            }

            for (int i = 0; i < configs.size(); i++) {
                Map<String, String> map = configs.get(i);
                if (map.get("id").equals(number)) {
                    return map;
                }
            }

            System.out.println("请输入正确的编号：");

        }

        return null;
    }

    private static String getCondition(Map<String, String> config, Console console)
    {

        String[] cloums = config.get("cloums").split(",");

        StringBuffer condition = new StringBuffer();

        boolean write = false;

        boolean end = false;

        while (true){
            System.out.println("请输入查询条件，单独一行输入@符号停止输入：");

            String where = readCmd(console);

            if (where.toLowerCase().equals("exit")){
                break;
            }

            if (where.toLowerCase().equals("@")){
                end = true;
                break;
            }

            for (int i = 0; i < cloums.length; i++) {
                String cloum = cloums[i];
                if (where.indexOf(cloum) != -1){
                    write = true;
                }
            }

            if (write){
                condition.append(where + ",");
                write = false;
            }else {
                System.out.println("输入的条件不正确，请重新输入：");
            }

        }

        if (end){
            if (condition.length() > 0){
                condition.deleteCharAt(condition.length()-1);
            }

            return condition.toString();
        }else {
            return null;
        }
    }







}
