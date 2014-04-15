package com.ridecharge.android.screenflow;

import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

/**
 * Created by ahuttner on 4/14/14.
 */
public class HtmlWriter {


    public static String writeIndex(String path) {

        String includeFile = "main_include.html";

        File dir = new File(path);
        dir.mkdir();

        File file = new File(path + "/index.html");


        try {
            FileOutputStream out = new FileOutputStream(file, true);
            PrintWriter pw = new PrintWriter(out);

            pw.print("<html>");
            pw.print("<table>");
            pw.print(String.format("<iframe src=\"%s\" seamless width=\"900\" height=\"1000\" />", includeFile));
            pw.print("</table>");
            pw.print("</html>");

            pw.flush();
            pw.close();

            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return includeFile;
    }


    /**
     * @param fileName the "includedFIle" from the call to writeIndex
     */
    public synchronized static String writeMajorStep(String path, String fileName, MajorStep majorStep) {
        String includeFile = fileName.substring(0, fileName.lastIndexOf('.') - 1)
                + "_" + System.currentTimeMillis()
                + ".html";

        File file = new File(path + "/" + fileName);

        try {
            FileOutputStream out = new FileOutputStream(file, true);
            PrintWriter pw = new PrintWriter(out);


            pw.print("<tr>");
            pw.print("<td>");
            pw.print("<h1>" + majorStep.activity + "</h1>");
            pw.print("<img width=\"200\" border=\"2\" src=\"" + majorStep.screenCap + "\" />");
            pw.print("</td>");
            pw.print("<td>");
            pw.print(String.format("<iframe src=\"%s\" seamless width=\"500\" height=\"500\" />", includeFile));
            pw.print("</td>");
            pw.print("</tr>");

            pw.flush();
            pw.close();

            out.flush();
            out.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return includeFile;
    }

    public synchronized static void writeMinorStep(String path, String fileName, MajorStep majorStep) {
        File file = new File(path + "/" + fileName);
        HashMap<String, Integer> messagesMap = majorStep.messagesMap;

        try {
            FileOutputStream out = new FileOutputStream(file, false);
            PrintWriter pw = new PrintWriter(out);

            for (String message : messagesMap.keySet()) {
                int count = messagesMap.get(message);
                pw.print(String.format("<h3>%s    %d</h3></p>", message, count));
            }

            pw.flush();
            pw.close();

            out.flush();
            out.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
