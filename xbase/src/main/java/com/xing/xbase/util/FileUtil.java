package com.xing.xbase.util;

import android.content.Context;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * 文件
 * Created by mzj on 2017/2/10.
 */
public class FileUtil {
    private static String TAG = "FileUtil";
    public static String Path_Main;
    public static String Path_Temp = Path_Main + "/temp";

    public static void init(String packname) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Path_Main = Environment.getExternalStorageDirectory() + "/" + packname;
            Path_Temp = Path_Main + "/temp";
            delFile(Path_Temp);
            createFile(Path_Main);
            createFile(Path_Temp);
        }
    }

    /**
     * 创建文件
     *
     * @param path 路径
     */
    public static void createFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    /**
     * 保存信息到指定文件,需要自己创建上级目录
     *
     * @param mission 信息
     */
    public static void saveStringToFile(String mission, String filepath) {
        File file = new File(filepath);
        if (file.exists()) {
            if (file.delete()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(file), "utf-8");
            BufferedWriter writer = new BufferedWriter(write);
            writer.write(mission);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 复制Assets到指定路径
     *
     * @param context    上下文
     * @param assetsName 指定Assets的文件名全称
     * @param savePath   保存路径
     */
    public static void copyAssets(Context context, String assetsName, String savePath) {
        String filename = savePath + "/" + assetsName;
        File dir = new File(savePath);
        try {
            if (dir.exists()) {
                dir.delete();
            }
            dir.createNewFile();
            if (!(new File(filename)).exists()) {
                InputStream is = context.getResources().getAssets().open(assetsName);
                FileOutputStream fos = new FileOutputStream(filename);
                byte[] buffer = new byte[7168];
                int count;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                fos.close();
                is.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 复制文件到指定路径
     *
     * @param oldPath 原路径
     * @param newPath 保存路径
     */
    public static void copyNormal(String oldPath, String newPath) {
        try {
            int byteread;
            File oldfile = new File(oldPath);
            File newfile = new File(newPath);
            if (oldfile.exists()) { //文件存在时
                if (!newfile.exists()) {
                    newfile.createNewFile();
                }
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                while ((byteread = inStream.read(buffer)) != -1) {
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取文本文件内容
     *
     * @param filePathAndName 带有完整绝对路径的文件名
     * @return 返回文本文件的内容
     */
    public static String readTxt(String filePathAndName) {
        StringBuilder str = new StringBuilder("");
        String st;
        try {
            FileInputStream fs = new FileInputStream(filePathAndName);
            InputStreamReader isr = new InputStreamReader(fs, "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            try {
                String data;
                while ((data = br.readLine()) != null) {
                    str.append(data).append(" ");
                }
            } catch (Exception e) {
                str.append(e.toString());
            }
            st = str.toString();
        } catch (IOException es) {
            st = "";
        }
        return st;
    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件，该目录不删除
     *
     * @param path 将要删除的文件目录
     */
    public static boolean delFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
                delFolder(path + "/" + tempList[i]);//再删除空文件夹
                flag = true;
            }
        }
        return flag;
    }

    public static void delFolder(String folderPath) {
        try {
            delFile(folderPath); //删除完里面所有内容
            String filePath = folderPath;
            filePath = filePath.toString();
            java.io.File myFilePath = new java.io.File(filePath);
            myFilePath.delete(); //删除空文件夹
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
