package com.yahid.lightanalyzer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
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

import com.yahid.lightanalyzer.model.RoadDataVO;

public class LocalProxy {

    private static final String FILE_EXTENSION = ".xls";
    private static final int FIRST_COL = 2;
    private static final int FIRST_ROW = 2;


    public static File createExcelSheet(Context context,RoadDataVO proj) {
        WritableWorkbook workbook;
        WritableSheet sheet;
        try {
            Date date = new Date();
            Timestamp ts = new Timestamp(date.getTime());
            File file = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
            workbook = Workbook.createWorkbook(new File(file,proj.getStreetName()+LocalProxy.FILE_EXTENSION));
            sheet = workbook.createSheet("First Sheet", 0);
            Label streetNameLbl = new Label(0,0, "Street name");
            Label streetName = new Label(1, 0, proj.getStreetName());
            sheet.addCell(streetNameLbl);
            sheet.addCell(streetName);
            Label poleHeightLbl = new Label(0,1,"Pole height");
            Number poleHeight = new Number(1, 1, proj.getPoleHeight());
            sheet.addCell(poleHeightLbl);
            sheet.addCell(poleHeight);

            for (int i = 0; i < proj.getLaneCount(); i++) {
                for (int j = 0; j < proj.getLaneLength(); j++) {
                    double value = proj.getDataPoint(i, j);
                    Number num = new Number(FIRST_COL + j,FIRST_ROW + i, value);
                    sheet.addCell(num);
                }
            }

            // All sheets and cells added. Now write out the workbook


            File actualFile = new File(file.getAbsolutePath(),proj.getStreetName() + ".xls");
            workbook.setOutputFile(actualFile);

            workbook.write();
            workbook.close();


            /*
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutput out = null;
            byte[] yourBytes;
            try {
                out = new ObjectOutputStream(bos);
                out.writeObject(workbook);
                yourBytes = bos.toByteArray();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException ex) {
                    // ignore close exception
                }
                try {
                    bos.close();
                } catch (IOException ex) {
                    // ignore close exception
                }
            }




            FileOutputStream fOut = new FileOutputStream(actualFile);
            fOut.write(yourBytes);
            fOut.close();
*/

            return actualFile;
            /*
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
*/


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
        return null;
    }

    public static void saveProject(Context context,String fileName, String projectAsJson) {
        try {
            File myFile = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),fileName + ".lap");
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
        File[] files = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).listFiles();
        for (File file : files) {
                if(file.getName().endsWith(".lap")){
                    inFiles.add(file);
            }
        }
        return inFiles;
    }
}
