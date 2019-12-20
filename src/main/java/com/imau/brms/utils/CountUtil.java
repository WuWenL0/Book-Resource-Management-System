package com.imau.brms.utils;

import java.io.*;

public class CountUtil {
    public static Long Get_Visit_Count(String txtFilePath) {

        try {
            File file = new File(txtFilePath);
            if(!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if(!file.exists()){
                file.createNewFile();
                PrintWriter pw = new PrintWriter(txtFilePath);
                pw.write("0");
                pw.flush();
                pw.close();
            }
            //读取文件(字符流)
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(txtFilePath),"UTF-8"));
            //循环读取数据
            String str = null;
            StringBuffer content = new StringBuffer();
            while ((str = in.readLine()) != null) {
                content.append(str);
            }
            //关闭流
            in.close();

            //System.out.println(content);
            // 解析获取的数据
            Long count = Long.valueOf(content.toString());
            count ++; // 访问量加1
            //写入相应的文件
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(txtFilePath),"UTF-8"));
            out.write(String.valueOf(count));

            //清楚缓存
            out.flush();
            //关闭流
            out.close();

            return count;
        }
        catch (Exception e){
            e.printStackTrace();
            return 0L;
        }


    }

}
