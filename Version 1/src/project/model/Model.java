package project.model;

import java.io.*;
import java.net.URL;

public class Model {

    private String readFromInputStream(InputStream inputStream)
            throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br
                     = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }


    public String[] readFromFile(String path) {
        String[] ret = null;
        try {
            ret = readFromInputStream(new FileInputStream(path)).split("\r");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public String readFromXtmhlFile(String path) {
        String ret = null;
        try {
            ret = readFromInputStream(new FileInputStream(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public void printStringArray(String[] array) {
        for (String s : array) {
            System.out.println(s);
        }
    }

    public void GetXHTML (String adress) {
        try {
            // Create a URL object from the user-entered URL string
            URL url = new URL(adress);

            // Open a stream to the URL and read the XHTML into a StringBuilder
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder xhtml = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                xhtml.append(line);
            }

            // Print the XHTML
            //System.out.println(xhtml);
            PrintWriter writer = new PrintWriter("xhtml.txt", "UTF-8");
            writer.println(xhtml);
            writer.close();
        } catch (Exception e) {
            // Print an error message if the URL is invalid or there is a problem reading the XHTML
            System.out.println("Error: Invalid URL or problem reading XHTML");
        }
    }

}
