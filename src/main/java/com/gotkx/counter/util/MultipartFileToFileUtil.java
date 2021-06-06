package com.gotkx.counter.util;


import org.springframework.web.multipart.MultipartFile;

import java.io.*;

public class MultipartFileToFileUtil {

    /**
     * MultipartFile 转 File
     *
     * @param file
     * @throws Exception
     */
    public static File multipartFileToFile(MultipartFile file) throws Exception {

        File toFile = null;
        if (file.equals("") || file.getSize() <= 0) {
            file = null;
        } else {
            InputStream ins = null;
            ins = file.getInputStream();
            toFile = new File(file.getOriginalFilename());
            inputStreamToFile(ins, toFile);
            ins.close();
        }
        return toFile;
    }

    public static File tFileToMFile(File file) {
        File toFile = null;
        InputStream ins = null;
        try {
            ins = new FileInputStream(file);
            toFile = new File(file.getName());
            inputStreamToFile(ins, toFile);
            ins.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return toFile;
    }

    //获取流文件
    private static void inputStreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        //File file = tFileToMFile(new File("G:\\Temp\\dbeaver-agent-latest\\dbeaver-agent\\shasum.txt"));
        File file = new File("G:\\Temp\\dbeaver-agent-latest\\dbeaver-agent\\shasum.txt");
        InputStream fileInputStream = new FileInputStream(new File("G:\\Temp\\dbeaver-agent-latest\\dbeaver-agent\\shasum.txt"));
        File toFile = new File(file.getName());
        inputStreamToFile(fileInputStream,toFile);
        System.out.println(toFile);
    }

    /**
     * 删除本地临时文件
     *
     * @param file
     */
    public static void delteTempFile(File file) {
        if (file != null) {
            File del = new File(file.toURI());
            del.delete();
        }
    }
}