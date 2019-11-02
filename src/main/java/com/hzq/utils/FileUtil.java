package com.hzq.utils;

import com.hzq.enums.FileTypeEnum;

import java.io.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * @Auther: blue
 * @Date: 2019/10/3
 * @Description: 处理文件的工具类
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
     * 将对象序列化
     * @param object 对象
     * @return 返回字节流
     */
    public static <T> byte[] serialize(T object) {
        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
          close(baos,oos);
        }
        return null;
    }

    /**
     * 反序列化
     * @param bytes 字节流
     * @return 对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T unserialize(byte[] bytes) {
        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;
        try {
            bais = new ByteArrayInputStream(bytes);
            ois = new ObjectInputStream(bais);
            return (T)ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(bais,ois);
        }
        return null;
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
        boolean result = file.mkdirs();
        if (!file.exists() && !result) {
            CheckPath(file);
        }
    }

    /** 获取文件头信息，该方法可以获取所有文件的类型
     * 可以将byte（8位）转换成int（32位），然后利用Integer.toHexString(int)来转换成16进制字符串。
     * 原理：把一个byte转换成两个用16进制字符，即把高4位和低4位转换成相应的16进制字符，并组合这两个16进制字符串，
     * 从而得到byte的16进制字符串。同理，相反的转换也是将两个16进制字符转换成一个byte。
     * 与运算的作用
     * 1.byte的大小为8bits而int的大小为32bits
     * 2.java的二进制采用的是补码形式
     * 所以与负数&的时候负数会自动给补码补位1，这样就会有误差
     * 而0xff默认是整形，所以，一个byte跟0xff相与会先将那个byte转化成整形运算，这样，结果中的高的24个比特就总会被清0，于是结果总是我们想要的。
     * @param is 输入流
     * @return 返回枚举
     */
    @SuppressWarnings("all")
    public static FileTypeEnum getFileType(InputStream is) throws IOException {
        byte[] src = new byte[28];
        is.read(src, 0, 28);
        StringBuilder stringBuilder = new StringBuilder("");
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF; //与运算，oxFF=1111 1111
            String hv = Integer.toHexString(v).toUpperCase();
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        FileTypeEnum[] fileTypes = FileTypeEnum.values();
        for (FileTypeEnum fileType : fileTypes) {
            if (stringBuilder.toString().startsWith(fileType.getValue())) {
                return fileType;
            }
        }
        return null;
    }

    public static boolean isPhoto(FileTypeEnum type) {
        if (type == FileTypeEnum.JPEG || type == FileTypeEnum.GIF) {
            return true;
        }
        return type == FileTypeEnum.PNG;
    }


    public static void main(String[] args) {
//        File file = new File ("D:\\images", "1fdgasdg3.png");
//        byte[] buf =  getBytesFromPhoto("D:\\images\\defaultAvatar.jpg");
//        ByteToPhoto(buf,file);
        Timestamp timestamp = Timestamp.valueOf("2019-10-14 23:38:50");
        System.out.println(timestamp.getTime() + "--" + timestamp);
    }

}
