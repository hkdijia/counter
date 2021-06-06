package com.gotkx.counter.util;

import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class SHA1Utils {

    public static String uploadDutyExcel(MultipartFile multfile) throws Exception {
        // 获取文件名
        String fileName = multfile.getOriginalFilename();
        // 获取文件后缀
        String prefix=fileName.substring(fileName.lastIndexOf("."));
        // 用uuid作为文件名，防止生成的临时文件重复
        final File excelFile = File.createTempFile(UUID.randomUUID().toString(), prefix);
        // MultipartFile to File
        multfile.transferTo(excelFile);

        //你的业务逻辑

        //程序结束时，删除临时文件
        deleteFile(excelFile);
        return "";
    }

    /**
     * 删除
     *
     * @param files
     */
    private static void deleteFile(File... files) {
        for (File file : files) {
            if (file.exists()) {
                boolean delete = file.delete();
                System.out.println(delete);
            }
        }
    }

    public static String getFileSHA1(File file) {
        MessageDigest md = null;
        FileInputStream fis = null;
        StringBuilder sha1Str = new StringBuilder();
        try {
            fis = new FileInputStream(file);
            MappedByteBuffer mbb = fis.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            md = MessageDigest.getInstance("SHA-1");
            md.update(mbb);
            byte[] digest = md.digest();
            String shaHex = "";
            for (int i = 0; i < digest.length; i++) {
                shaHex = Integer.toHexString(digest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    sha1Str.append(0);
                }
                sha1Str.append(shaHex);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                }
            }
        }
        return sha1Str.toString();
    }

    public static void main(String[] args) throws Exception {
        //String fileSHA1 = getFileSHA1(new File("C:\\Users\\11812\\OneDrive\\qq图片\\QQ图片20160310185239.jpg"));
        //8b5d5697666efc961141f515244a404ede5a7731
        //String fileSHA1 = getFileSHA1(new File("G:\\Temp\\1111.jpg"));

//        String fileSHA1 = getFileSHA1(new File("C:\\Users\\11812\\OneDrive\\qq图片\\QQ图片20201017172402.jpg"));
//        System.out.println(fileSHA1.length());

//        MultipartFile file = new CommonsMultipartFile(new FileItem());
//        uploadDutyExcel(file);
    }

}
