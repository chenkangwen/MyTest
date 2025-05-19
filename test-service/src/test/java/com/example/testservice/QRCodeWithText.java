package com.example.testservice;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class QRCodeWithText {

    public static void main(String[] args) throws Exception {
        String text = "https://www.baidu.com/";
        String desc = "狮岭一";
        int width = 300;
        int height = 350; // 额外高度用于文字
        
        // 生成二维码矩阵
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        BitMatrix matrix = new MultiFormatWriter().encode(
            text, BarcodeFormat.QR_CODE, width, height-50, hints);
        
        // 转换为BufferedImage
        BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(matrix);
        
        // 创建带文字的图片
        BufferedImage combined = new BufferedImage(
            width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = combined.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        
        // 绘制二维码
        g.drawImage(qrImage, 0, 0, null);
        
        // 添加文字
        g.setColor(Color.BLACK);
        g.setFont(new Font("宋体", Font.BOLD, 16));
        FontMetrics metrics = g.getFontMetrics();
        int textWidth = metrics.stringWidth(desc);
        int x = (width - textWidth) / 2;
        g.drawString(desc, x, height - 20);
        
        // 输出图片
        ImageIO.write(combined, "png", new File("qrcode_with_text.png"));
        g.dispose();
    }
}
