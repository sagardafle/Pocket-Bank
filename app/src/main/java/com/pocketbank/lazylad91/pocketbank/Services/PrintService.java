package com.pocketbank.lazylad91.pocketbank.Services;

import android.os.Environment;
import android.util.Log;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.pocketbank.lazylad91.pocketbank.Model.Transaction;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Parteek on 7/31/2016.
 */
public class PrintService {
    private static ArrayList<Transaction> transactionList;
    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD);
    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.NORMAL, BaseColor.RED);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
            Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.BOLD);
    public static String LOG_TAG="printservice";
    public static File createFolder(ArrayList<Transaction> transactions) throws FileNotFoundException, DocumentException  {
        transactionList = transactions;
        File pdfFolder = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), "Pocket Bank Monthly Statement");
        if (!pdfFolder.exists()) {
            pdfFolder.mkdir();
            Log.i(LOG_TAG, "Pdf Directory created");
        }

        Date date = new Date() ;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);

        File myFile = new File(pdfFolder + timeStamp + ".pdf");
        OutputStream output = new FileOutputStream(myFile);
        Document document = new Document(PageSize.LETTER);
        PdfWriter.getInstance(document, output);
        document.open();

        addMetaData(document);
        addTitlePage(document);
        addContent(document,transactionList);
        document.addTitle("Expense Summary Powered by PocketBank");
        if(document!=null) {
            document.close();
            return myFile;
        }
        return null;

    }

    private static void addContent(Document document,ArrayList<Transaction> transactionList) {
        try {
            createTable(document,transactionList);
        } catch (BadElementException e) {
            e.printStackTrace();
        }
    }

    private static void createTable(Document document,ArrayList<Transaction> transactionList)
            throws BadElementException {
        document.newPage();
        PdfPTable table = new PdfPTable(5);
        table.setSpacingBefore(3.0f);
        PdfPCell c1 = new PdfPCell(new Phrase("Date"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

/*        c1 = new PdfPCell(new Phrase("Payment Mode"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);*/

        c1 = new PdfPCell(new Phrase("Category"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Notes"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Location"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Amount"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        table.setHeaderRows(1);
        if(transactionList.size()!=0){
            for (Transaction txn : transactionList) {

                table.addCell(txn.getDate() + "-" + txn.getMonth() + " " + txn.getYear());
                //table.addCell(txn.getPaymentMode().getCardType());
                table.addCell(txn.getCategory().getName());
                table.addCell(txn.getNotes());
                table.addCell(txn.getLocation().getName());
                table.addCell(txn.getAmount() + "");
            }
        }
        else{
            table.addCell("");
            //table.addCell(txn.getPaymentMode().getCardType());
            table.addCell("");
            table.addCell("");
            table.addCell("");
            table.addCell("");
        }
        try {
            document.add(table);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

    }

    private static void addTitlePage(Document document) {
        // Start a new page
        document.newPage();

        Paragraph preface = new Paragraph();
        // We add one empty line
        addEmptyLine(preface, 1);
        // Lets write a big header
        preface.add(new Paragraph("Monthly Statement", catFont));

        addEmptyLine(preface, 1);
        // Will create: Report generated by: _name, _date
        preface.add(new Paragraph("Report generated by: " + "PocketBank" + ", " + new Date(), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                smallBold));

    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    private static void addMetaData(Document document) {
        document.addTitle("Monthly Statement");
        document.addSubject("Powered by Pocket Bank");
        document.addKeywords("Expenses, Sum, Category");
        document.addAuthor("Power Bank");
        document.addCreator("Power Bank");
    }


}
