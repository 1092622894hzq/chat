package com.hzq.utils;

import java.io.*;
import java.util.Objects;

/**
 * @Auther: blue
 * @Date: 2019/10/3
 * @Description: 流的工具类
 * @version: 1.0
 */
public class FileUtil {

    /**
     * 将二进制流变为图片
     * @param buff 二进制流
     * @param file 文件
     */
    public static void ByteToPhoto(byte[] buff,File file){
        OutputStream os = null;
//        //1 .创建源
//        File file = new File(path);
//        CheckPath(file);
        try {
            //2. 选择流,并且
            os = new FileOutputStream(file,true);
            //3.操作
            os.write(buff,0,buff.length);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            //4. 关闭流
            close(os);
        }
    }

    /**
     * 将图片变为二进制流
     * @param path 获取图片的路径
     * @return 返回二进制流
     */
    public static byte[] getBytesFromPhoto(String path){
        InputStream is;
        ByteArrayOutputStream baos = null;
        try {
            //1. 源
            File file = new File(path);
            CheckPath(file);
            //2. 流
            is = new FileInputStream(file);
            //已经自动生成缓冲容器
            baos = new ByteArrayOutputStream();
            //3.缓冲容器-->是is的缓冲容器
            byte[] buff = new byte[1024];
            int len = -1;//缓冲长度
            while ((len=is.read(buff))!=-1){
                baos.write(buff,0,len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Objects.requireNonNull(baos).toByteArray();
    }

    /**
     * 复制流操作
     * @param is  输入流
     * @param os 输出流
     */
    public static void copy(InputStream is, OutputStream os){
        //1. is的缓冲容器
        byte[] isBuff = new byte[1024];
        //2. is.的读取长度
        int len = -1;
        try {
            while ((len=is.read(isBuff))!=-1){
                os.write(isBuff,0,len);
                os.flush();
            }
        }catch (Exception e){
            e.getStackTrace();
        }finally {
            close(is,os);
        }
    }

    /**
     * 关闭流
     * @param ios 要关闭的流
     */
    private static void close(Closeable... ios){
        for (Closeable io:ios){ //前提是继承了Closeable接口
            try {
                if (io!=null)
                    io.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 确保文件创建成功
     * @param file 文件
     */
    public static void CheckPath(File file) {
        if (!file.exists() && !file.mkdirs()) {
            CheckPath(file);
        }
    }

    public static void main(String[] args) {
        File file = new File ("D:\\images", "1fdgasdg3.png");
        byte[] buf =  getBytesFromPhoto("D:\\images\\defaultAvatar.jpg");
        ByteToPhoto(buf,file);
    }

}
