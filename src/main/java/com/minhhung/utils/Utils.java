package com.minhhung.utils;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class Utils {

    //https://stackoverflow.com/questions/5936403/html-to-xhtml-conversion-in-java
    public static String convertToXhtml(String html) throws UnsupportedEncodingException {

//        Tidy tidy = new Tidy();
//        tidy.setInputEncoding(UTF_8);
//        tidy.setOutputEncoding(UTF_8);
//        tidy.setXHTML(true);
//        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes(UTF_8));
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        tidy.parseDOM(inputStream, outputStream);
//        return outputStream.toString(UTF_8);

        final Document document = Jsoup.parse(html);
        document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
        return document.html();
    }


    public static byte[] getQRCodeImage(String text, int width, int height) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "png", byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            return null;
        }
    }

    public static void convertPdfToImage(byte[] data, String destinationDir, String fileName) {
        try {
            File destinationFile = new File(destinationDir);
            if (!destinationFile.exists()) {
                destinationFile.mkdir();
                System.out.println("Folder Created -> " + destinationFile.getAbsolutePath());
            }

            PDDocument document = PDDocument.load(new ByteArrayInputStream(data));
            @SuppressWarnings("unchecked")
            List<PDPage> list = document.getDocumentCatalog().getAllPages();

            // int pageNumber = 1;
            for (PDPage page : list) {
                BufferedImage image = page.convertToImage();
                File outputfile = new File(destinationDir + fileName + ".png");
                //File outputfile = new File(destinationDir + fileName + "_" + pageNumber + ".png");
                ImageIO.write(image, "png", outputfile);
                // pageNumber++;
            }
            document.close();
            System.out.println("Image saved at -> " + destinationFile.getAbsolutePath());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String decodeQRCode(File qrCodeimage) throws IOException {
//        BufferedImage bufferedImage = ImageIO.read(qrCodeimage);
//        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
//        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
//
//        try {
//            Result result = new MultiFormatReader().decode(bitmap);
//            return result.getText();
//        } catch (NotFoundException e) {
//            System.out.println("There is no QR code in the image");
//            return null;
//        }

        String result = null;
        BufferedImage image;
        try {
            image = ImageIO.read(qrCodeimage);
            if (null != image) {
                LuminanceSource source = new BufferedImageLuminanceSource(image);
                BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

                Hashtable<DecodeHintType, String> hints = new Hashtable<DecodeHintType, String>();
                hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");

                Result res = new MultiFormatReader().decode(bitmap, hints);
                result = res.getText();
            } else {
                throw new IllegalArgumentException ("Could not decode image.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return result;
        }
    }
}