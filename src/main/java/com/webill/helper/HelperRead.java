package com.webill.helper;

import com.google.zxing.*;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;

import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.imageio.ImageIO;
import com.asprise.ocr.Ocr;

public class HelperRead {

    //Calculate month
    public static Date deadlineMonth(Integer numberMonth){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, numberMonth);
        Date result = cal.getTime();
        return result;
    }

    //calculate the amount of the bill


    // Crack image OCR
    public static String crackImage(String filePath) {
        String textonly = null;
        Ocr.setUp(); // one time setup
        Ocr ocr = new Ocr(); // create a new OCR engine
        ocr.startEngine("eng", Ocr.SPEED_FASTEST); // English
        //String s = ocr.recognize(new File[] {new File(filePath)}, Ocr.RECOGNIZE_TYPE_ALL, Ocr.OUTPUT_FORMAT_PLAINTEXT);
         textonly = ocr.recognize(new File[] {new File(filePath)},Ocr.RECOGNIZE_TYPE_TEXT, Ocr.OUTPUT_FORMAT_PLAINTEXT);
         textonly = textonly.replace("*","0");
         textonly = textonly.replaceAll("\\D+","");
        System.out.println("Result: " + textonly);
// ocr more images here ...
        ocr.stopEngine();

        return textonly;

    }

//    public static String readFromOcr(MultipartFile file) throws IOException, TesseractException {
//
//        String readText = "";
//        BufferedImage img = ImageIO.read(file.getInputStream());
//        Tesseract tesseract = new Tesseract();
//        File tessDataFolder = LoadLibs.extractTessResources("tessdata");
//        //Set the tessdata path
//        tesseract.setDatapath(tessDataFolder.getAbsolutePath());
//        //readText = tesseract.doOCR(img);
//
//        return !tesseract.doOCR(img).isEmpty()?tesseract.doOCR(img):readText;
//    }

    public static String checkQrCode(String filePath) throws IOException, NotFoundException {
        String qrText = "";
        File imageFile = new File(filePath);
        BufferedImage image = ImageIO.read(imageFile);
        int[] pixels = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
        RGBLuminanceSource source = new RGBLuminanceSource(image.getWidth(), image.getHeight(), pixels);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        MultiFormatReader reader = new MultiFormatReader();
        qrText = reader.decode(bitmap).getText();
        System.out.println("The detected QR code is: " + qrText);
        return qrText;
    }

//    public static double[] getCordinate(String filePath){
//        javaxt.io.Image image = new javaxt.io.Image("D:\\codeTest\\arun.jpg");
//        double[] gps = image.getGPSCoordinate();
//    }

    public static boolean checkIfImage(MultipartFile file){
        String mimetic= file.getContentType();
        String type = mimetic.split("/")[0];
        if(type.equals("image"))
            return true;
        else
            return false;
    }


    public static void generateQRCodeImage(String text, int width, int height, String filePath)
        throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        Path path = FileSystems.getDefault().getPath(filePath);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
    }

    public static boolean isNullOrEmpty( final List< ? > c ) {
        return c == null || c.isEmpty();
    }


    public static String[] getSplitString(String chaine,String separtor){
        return chaine.split(""+separtor);
    }

    public static boolean minimunGapBetweenLong(double value,double valueToCompare){
        return Math.abs(valueToCompare-value)<1;
    }

    public static void main(String args[]) {
        System.out.print(minimunGapBetweenLong(55.55555,58.6656)+"");
    }
    public static boolean isMacOS() {
        String osName = System.getProperty("os.name").toLowerCase();
        boolean isMacOs = osName.startsWith("mac os x");
        return isMacOs;
    }
}
