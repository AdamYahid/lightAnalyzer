package com.yahid.lightanalyzer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.sql.Timestamp;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.text.GetChars;
import android.webkit.MimeTypeMap;

public class LocalProxy {

    private static final String FILE_EXTENSION = ".xls";
    private static final int FIRST_COL = 2;
    private static final int FIRST_ROW = 2;


    public static void createExcelSheet(Context context,ProjectVO proj) {
        WritableWorkbook workbook;
        WritableSheet sheet;
        try {
            Date date = new Date();
            Timestamp ts = new Timestamp(date.getTime());
            File file = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
            workbook = Workbook.createWorkbook(new File(file,proj.getStreetName()+LocalProxy.FILE_EXTENSION));
            sheet = workbook.createSheet("First Sheet", 0);
            Label streetName = new Label(1, 0, proj.getStreetName());
            sheet.addCell(streetName);
            Number poleHeight = new Number(1, 1, proj.getPoleHeight());
            sheet.addCell(poleHeight);

            for (int i = 0; i < proj.getHeight(); i++) {
                for (int j = 0; j < proj.getWidth(); j++) {
                    double value = proj.getValue(i, j);
                    Number num = new Number(FIRST_COL + j,FIRST_ROW + i, value);
                    sheet.addCell(num);
                }
            }

            // All sheets and cells added. Now write out the workbook
            workbook.write();
            workbook.close();

            String to[] = {"adam.yahid@gmail.com"};

            Intent i = new Intent(Intent.ACTION_SEND);

            MimeTypeMap mime = MimeTypeMap.getSingleton();
            String mimeTypeForXLSFile = mime.getMimeTypeFromExtension(".xls");

            i.putExtra(Intent.EXTRA_EMAIL,to);
            i.putExtra(Intent.EXTRA_SUBJECT, proj.getStreetName());
            i.putExtra(Intent.EXTRA_TEXT, "message");
            i.setType("message/rfc822");
            //i.setType(mimeTypeForXLSFile);
            i.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + file.getAbsolutePath()));
            context.startActivity(Intent.createChooser(i,"send email:"));


        } catch (IOException e) {
            e.getStackTrace();
        } catch (RowsExceededException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (WriteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void saveProject(Context context,String fileName, String projectAsJson) {
        try {
            File myFile = new File(context.getFilesDir(),fileName + ".lap");
            myFile.createNewFile();
            FileOutputStream fOut = new FileOutputStream(myFile);
            OutputStreamWriter myOutWriter =
                    new OutputStreamWriter(fOut);
            myOutWriter.append(projectAsJson);
            myOutWriter.close();
            fOut.close();
        } catch (Exception e) {
        }
    }

    public static ArrayList<File> getFileList(Context context) {
        ArrayList<File> inFiles = new ArrayList<File>();
        File[] files = context.getFilesDir().listFiles();
        for (File file : files) {
                if(file.getName().endsWith(".lap")){
                    inFiles.add(file);
            }
        }
        return inFiles;
    }
}
