package sckr.tool;

import java.util.List;
import java.util.Map;

/**
 * Show
 *
 * @version 0.1
 * @authors Hefa
 * @date 2019-05-31 08:49
 */

public class Show {

    public static void showConfig(List<Map<String, String>> config)
    {
        System.out.println("检测到有以下接口配置：");
        for (int i = 0; i < config.size(); i++) {
            Map<String, String> map = config.get(i);
            System.err.println(map.get("id") + "\t" + map.get("name"));
        }
        System.out.println("请输入要查询的接口编号：");
    }


    public static void showFields(Map<String, String> config)
    {
        System.out.println("该接口将查询以下字段：");
        String cloums = config.get("cloums");
        String[] split = cloums.split(",");
        for (String cloum :split) {
            System.err.println(cloum + "\t") ;
        }
    }

    public static void showExport()
    {
        System.out.println("正在导出请稍候..");
    }

    public static void showExit()
    {
        System.out.println("谢谢使用再见！");
    }




}
