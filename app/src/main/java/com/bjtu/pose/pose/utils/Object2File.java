package com.bjtu.pose.pose.utils;

import java.io.*;

/**
 * @author Li Haitao
 * @version 1.0
 * Description: 写对象与读对象，在本项目中用于模型的写和读
 */
public class Object2File {
    /**
     * @param filepath 提供路径
     * @param obj 要保存的对象
     * Description:把对象写到文件
     */
    public static void writeObjectToFile(Object obj, String filepath) {
        File file = new File(filepath);
        FileOutputStream out;

        try {
            out = new FileOutputStream(file);
            ObjectOutputStream objout = new ObjectOutputStream(out);
            objout.writeObject(obj);
            objout.flush();
            objout.close();
            System.out.println("write object success!");
        } catch (IOException e) {
            System.out.println("write object failed!");
            e.printStackTrace();
        }
    }

    /**
     * @param filepath 提供读取路径
     * Description:读取文件到对象
     */
    public static Object readObjectFromFile(String filepath) {
        Object temp = null;
        File file = new File(filepath);
        FileInputStream in;
        try {
            in = new FileInputStream(file);
            ObjectInputStream objin = new ObjectInputStream(in);
            temp = objin.readObject();
            objin.close();
//            System.out.println("read object success!");
        } catch (IOException e) {
            System.out.println("read object failed! IOException");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("read object failed! ClassNotFoundException");
            e.printStackTrace();
        }

        return temp;
    }
}
