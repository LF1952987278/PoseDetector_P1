package com.bjtu.pose.pose.utils;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ComTools {

    public static List<String> traverse_dir(String path,String file_suffix){
        List<String> myList = new ArrayList<>();
        ComTools.findFileList(new File(path),myList,file_suffix);
        return myList;
    }

    private static void findFileList(File dir, List<String> fileNames, String file_suffix) {
        if(dir.exists())
            System.out.println(dir + " exsit");
        if(dir.isDirectory()){
            System.out.println(dir + " is Dictionary");
        }
        if (!dir.exists() || !dir.isDirectory()) {// 判断是否存在目录
            System.out.println("return");
            return;
        }
        String[] files = dir.list();// 读取目录下的所有目录文件信息
        if(dir.list() == null){
            System.out.println("zero");
        }

        for (int i = 0; i < files.length; i++) {// 循环，添加文件名或回调自身
            File file = new File(dir, files[i]);
            if (file.isFile()) {// 如果文件
                if(file.getName().endsWith(file_suffix)){
                    fileNames.add(dir + "/" + file.getName());// 添加文件全路径名
                }
            } else {// 如果是目录
                findFileList(file, fileNames, file_suffix);// 回调自身继续查询
            }
        }
    }
}
