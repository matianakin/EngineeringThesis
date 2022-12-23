package project.model;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The type Model.
 */
public class Model {

    private final Map<String, String> dictionary = new HashMap<>();

    private String URL;

    private String txtPath;

    private String[] reqs;

    private String XHTML;

    private final List<String> errors = new ArrayList<>();

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


    /**
     * Read from file string [ ].
     *
     * @param path the path
     * @return the string [ ]
     */
    public String[] readFromFile(String path) {
        String[] ret = null;
        try {
            ret = readFromInputStream(new FileInputStream(path)).split("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }


    /**
     * Read from xhtml file string.
     *
     * @param path the path
     * @return the string
     */
    public String readFromXHTMLFile(String path) {
        String ret = null;
        try {
            ret = readFromInputStream(new FileInputStream(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    /**
     * Print string array.
     *
     * @param array the array
     */
    public void printStringArray(String[] array) {
        for (String s : array) {
            System.out.println(s);
        }
    }

    /**
     * Get xhtml.
     *
     * @param address the URL address of the site
     */
    public void GetXHTML (String address) {
        try {
            // Create a URL object from the user-entered URL string
            URL url = new URL(address);

            // Open a stream to the URL and read the XHTML into a StringBuilder
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder xhtml = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                xhtml.append(line);
            }
            XHTML = xhtml.toString();
            /*PrintWriter writer = new PrintWriter("xhtml.txt", "UTF-8");
            writer.println(xhtml);
            writer.close();*/

           /* Pattern pattern = Pattern.compile("<\\s*([\\w:.-]+)");
            Matcher matcher = pattern.matcher(xhtml);

            Map<String, Integer> elementCounts = new HashMap<>();

            while (matcher.find()) {
                String elementName = matcher.group(1);
                int count = elementCounts.getOrDefault(elementName, 0);
                elementCounts.put(elementName, count + 1);
            }

            for (Map.Entry<String, Integer> entry : elementCounts.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }*/
        } catch (Exception e) {
            System.out.println("Error: Invalid URL or problem reading XHTML");
        }
    }

    /**
     * Read dictionary.
     */
    public void readDictionary()
    {
        try (BufferedReader reader = new BufferedReader(new FileReader("dictionary.txt"))) {
            String line;
            // Read each line of the file
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" - ");
                String key = parts[0];
                String value = parts[1];
                dictionary.put(key, value);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //System.out.println(dictionary);
    }

    /**
     * Gets dictionary.
     *
     * @return the dictionary
     */
    public Map<String, String> getDictionary() {
        return dictionary;
    }

    /*
      Sets dictionary.

      @param dictionary the dictionary
    public void setDictionary(Map<String, String> dictionary) {
        this.dictionary = dictionary;
    }*/


    public void removeS()
    {
        for(int i=0; i< reqs.length; i++)
        {
            for(int j=0; j<reqs[i].length() ; j++)
            {
                if(reqs[i].charAt(j)=='s'&&(j+1==reqs[i].length() ||reqs[i].charAt(j+1)==' '))
                {
                    StringBuilder newWord = new StringBuilder(reqs[i]);
                    newWord.deleteCharAt(j);
                    reqs[i]=newWord.toString();
                }
            }
        }
    }


    public void swapWords()
    {
        for (Map.Entry<String, String> set : dictionary.entrySet())
        {
            for (int i=0; i< reqs.length; i++) {
                if (reqs[i].contains(set.getKey())) {
                    int startIndex = reqs[i].indexOf(set.getKey());
                    int endIndex = startIndex + set.getKey().length();
                    String newString = reqs[i].substring(0, startIndex) + set.getValue() + reqs[i].substring(endIndex);
                    reqs[i] = newString;
                }
            }
        }
    }

    public void counter(String search, int number)
    {
        String toSearch = "<"+search+">";
        int index = XHTML.indexOf(toSearch);

        int count = 0;

        // Iterate over the string and count the number of occurrences of the modified word
        while (index != -1) {
            count++;
            index = XHTML.indexOf(toSearch, index + toSearch.length());
        }

        if(count != number)
        {
            errors.add("Wrong number of occurrences of "+search+", supposed to be "+number+", is "+count+"\n");
        }

    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getTxtPath() {
        return txtPath;
    }

    public void setTxtPath(String txtPath) {
        this.txtPath = txtPath;
    }

    public String[] getReqs() {
        return reqs;
    }

    public void setReqs(String[] reqs) {
        this.reqs = reqs;
    }
}
