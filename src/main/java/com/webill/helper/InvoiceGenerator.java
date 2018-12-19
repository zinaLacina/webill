package com.webill.helper;


import com.webill.domain.Bills;
import com.webill.domain.Setting;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

public class InvoiceGenerator {
    public Bills bills;
    public Setting setting;
    public BaseFont bfBold;
    public BaseFont bf;
    public int pageNumber = 1;

    public InvoiceGenerator(Bills bills,Setting setting) {
        this.bills = bills;
        this.setting=setting;
    }

    public void createPDF (String pdfFilename){

        Document doc = new Document();
        PdfWriter docWriter = null;
        initializeFonts();

        try {
            String path = "/tmp/" + pdfFilename+".pdf";
            docWriter = PdfWriter.getInstance(doc , new FileOutputStream(path));
            doc.addAuthor("ZINA LACINA");
            doc.addCreationDate();
            doc.addProducer();
            doc.addCreator("cleanenery.net");
            doc.addTitle("Invoice");
            doc.setPageSize(PageSize.LETTER);

            doc.open();
            PdfContentByte cb = docWriter.getDirectContent();

            boolean beginPage = true;
            int y = 0;

            for(int i=0; i < 1; i++ ){
                if(beginPage){
                    beginPage = false;
                    generateLayout(doc, cb);
                    generateHeader(doc, cb);
                    y = 615;
                }
                generateDetail(doc, cb, i, y);
                y = y - 15;
                if(y < 50){
                    printPageNumber(cb);
                    doc.newPage();
                    beginPage = true;
                }
            }
            printPageNumber(cb);

        }
        catch (DocumentException dex)
        {
            dex.printStackTrace();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            if (doc != null)
            {
                doc.close();
            }
            if (docWriter != null)
            {
                docWriter.close();
            }
        }
    }





    public void generateLayout(Document doc, PdfContentByte cb)  {

        try {

            cb.setLineWidth(1f);

            // Invoice Header box layout
            cb.rectangle(420,700,150,60);
            cb.moveTo(420,720);
            cb.lineTo(570,720);
            cb.moveTo(420,740);
            cb.lineTo(570,740);
            cb.moveTo(480,700);
            cb.lineTo(480,760);
            cb.stroke();

            // Invoice Header box Text Headings
            createHeadings(cb,422,743,"Account No.");
            createHeadings(cb,422,723,"Invoice No.");
            createHeadings(cb,422,703,"Invoice Date");

            // Invoice Detail box layout
            cb.rectangle(20,50,550,600);
            cb.moveTo(20,630);
            cb.lineTo(570,630);
            cb.moveTo(50,50);
            cb.lineTo(50,650);
            cb.moveTo(150,50);
            cb.lineTo(150,650);
            cb.moveTo(430,50);
            cb.lineTo(430,650);
            cb.moveTo(500,50);
//            cb.lineTo(500,650);
//            cb.moveTo(510,50);
//            cb.lineTo(510,650);
//            cb.moveTo(550,50);
//            cb.lineTo(550,650);
            cb.stroke();

            // Invoice Detail box Text Headings
//            createHeadings(cb,22,633,"N");
//            createHeadings(cb,52,633,"Item Number");
//            createHeadings(cb,152,633,"Item Description");
//            createHeadings(cb,432,633,"Price");
//            createHeadings(cb,502,633,"Ext Price");
            createHeadings(cb,22,633,"N");
            createHeadings(cb,52,633,"Last reading");
            createHeadings(cb,152,633,"reading");
            createHeadings(cb,432,633,"consomption");
            createHeadings(cb,502,633,"Unit price");
            createHeadings(cb,600,633,"Total Price");
            createHeadings(cb,650,633,"Ext Price");

            //add the images
            String imagePath="https://s3-ap-northeast-1.amazonaws.com/webill-s3-bucket/"+setting.getCompanyLogo();
            Image companyLogo = Image.getInstance(""+imagePath);
            companyLogo.setAbsolutePosition(25,700);
            companyLogo.scalePercent(25);
            doc.add(companyLogo);

        }

        catch (DocumentException dex){
            dex.printStackTrace();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

    }

    public void generateHeader(Document doc, PdfContentByte cb)  {

        try {

            createHeadings(cb,200,750,""+setting.getCompanyName());
            createHeadings(cb,200,735,"Address Line 1"+setting.getCompanyAddress());
            createHeadings(cb,200,720,"Address Line 2");
            createHeadings(cb,200,705,"City, State - ZipCode");
            createHeadings(cb,200,690,"Japan");

            createHeadings(cb,482,743,"ABC0001"+setting.getCompanyNumber());
            createHeadings(cb,482,723,"123456");
            createHeadings(cb,482,703,"12/1/2018");

        }

        catch (Exception ex){
            ex.printStackTrace();
        }

    }

    public void createHeadings(PdfContentByte cb, float x, float y, String text){


        cb.beginText();
        cb.setFontAndSize(bfBold, 8);
        cb.setTextMatrix(x,y);
        cb.showText(text.trim());
        cb.endText();

    }

    public void generateDetail(Document doc, PdfContentByte cb, int index, int y)  {
        DecimalFormat df = new DecimalFormat("0.00");

        try {

//            createContent(cb,48,y,String.valueOf(index+1),PdfContentByte.ALIGN_RIGHT);
//            createContent(cb,52,y, "ITEM" + String.valueOf(index+1),PdfContentByte.ALIGN_LEFT);
//            createContent(cb,152,y, "Product Description - SIZE " + String.valueOf(index+1),PdfContentByte.ALIGN_LEFT);

            int consumption = bills.getCurrentMonthReading()-bills.getLastMonthReading();
            createContent(cb,48,y,String.valueOf(index+1),PdfContentByte.ALIGN_RIGHT);
            createContent(cb,52,y,String.valueOf(bills.getLastMonthReading()),PdfContentByte.ALIGN_RIGHT);
            createContent(cb,100,y,String.valueOf(bills.getCurrentMonthReading()),PdfContentByte.ALIGN_RIGHT);
            createContent(cb,152,y,String.valueOf(consumption),PdfContentByte.ALIGN_RIGHT);
            createContent(cb,200,y,String.valueOf(bills.getBillSettingApp().getPricePerKW()),PdfContentByte.ALIGN_RIGHT);

            double price =consumption*bills.getBillSettingApp().getPricePerKW();
            double totalPrice = price+price*bills.getBillSettingApp().getTax()-bills.getBillSettingApp().getDiscount();
            if(consumption<1){
                price = bills.getBillSettingApp().getProcessing();
                totalPrice = bills.getBillSettingApp().getProcessing();
            }

            createContent(cb,498,y, df.format(price),PdfContentByte.ALIGN_RIGHT);
            createContent(cb,568,y, df.format(totalPrice),PdfContentByte.ALIGN_RIGHT);

        }

        catch (Exception ex){
            ex.printStackTrace();
        }

    }

    public void createContent(PdfContentByte cb, float x, float y, String text, int align){


        cb.beginText();
        cb.setFontAndSize(bf, 8);
        cb.showTextAligned(align, text.trim(), x , y, 0);
        cb.endText();

    }

    public void printPageNumber(PdfContentByte cb){


        cb.beginText();
        cb.setFontAndSize(bfBold, 8);
        cb.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Page No. " + (pageNumber+1), 570 , 25, 0);
        cb.endText();

        pageNumber++;

    }

    public void initializeFonts(){


        try {
            bfBold = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
            bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);

        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
