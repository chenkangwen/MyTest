package com.example.testservice;


 
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

 
public class QRCodeGenerator {




    public static void generateQRCodeImage(String text, int width, int height, String filePath) throws WriterException, IOException {
        // 创建二维码数据矩阵
        BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height);
        
        // 将数据矩阵输出到图片文件
        Path path = FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
    }
    
    public static void main(String[] args) {
        String text = "Hello, World!"; // 要编码的文字
        int width = 300; // 二维码图片宽度
        int height = 300; // 二维码图片高度
        String filePath = "D:\\BaiduNetdiskDownload\\qrcode.png"; // 输出文件路径和名称
        
        try {
            generateQRCodeImage(text, width, height, filePath);
            System.out.println("QR Code generated successfully!");
        } catch (WriterException | IOException e) {
            System.err.println("Could not generate QR Code, WriterException :: " + e.getMessage());
        }
    }
}