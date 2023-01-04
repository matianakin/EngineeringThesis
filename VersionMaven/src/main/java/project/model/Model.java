package project.model;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Model.
 */
public class Model {

    private final Map<String, String> dictionary = new HashMap<>();

    private String txtPath;

    private String address;

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


    public void GetXHTML () {
        try {
            /*
            URL url = new URL(address);


            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder xhtml = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                xhtml.append(line);
            }*/

            System.setProperty("webdriver.chrome.driver", "C:\\chromedriver_win32\\chromedriver.exe");

            ChromeOptions options = new ChromeOptions();
            options.setHeadless(true);


            WebDriver driver = new ChromeDriver(options);

            driver.get(address);

            Thread.sleep(1000);

            /*WebClient webClient = new WebClient();

            webClient.getOptions().setThrowExceptionOnScriptError(false);
            webClient.getOptions().setJavaScriptEnabled(true);

            HtmlPage page = webClient.getPage(address);
            webClient.waitForBackgroundJavaScript(1000000);
            var xhtml = page.asXml();*/

            XHTML = driver.getPageSource();
            PrintWriter writer = new PrintWriter("xhtml.txt", StandardCharsets.UTF_8);
            writer.println(XHTML);
            writer.close();
            driver.quit();
            /*Pattern pattern = Pattern.compile("<\\s*([\\w:.-]+)");
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
        } catch (InterruptedException e) {
            System.out.println("Error: Interrupted while waiting for page to load");
        } catch (IOException e) {
            System.out.println("Error: Problem writing to file");
        }
    }

    /**
     * Read dictionary.
     */
    public void readDictionary()
    {
        try (BufferedReader reader = new BufferedReader(new FileReader("dictionary.txt"))) {
            String line;
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
        String toSearch1 = "<"+search+" ";
        String toSearch2 = "<"+search+">";
        int index = XHTML.indexOf(toSearch1);

        int count = 0;

        while (index != -1) {
            count++;
            index = XHTML.indexOf(toSearch1, index + toSearch1.length());
        }

        index = XHTML.indexOf(toSearch2);

        while (index != -1) {
            count++;
            index = XHTML.indexOf(toSearch2, index + toSearch2.length());
        }

        if(count != number)
        {
            errors.add("Wrong number of occurrences of "+search+", supposed to be "+number+", is "+count+"\n");
        }

    }


    public void iterateReqs()
    {
        for (String req : reqs) {
            if (req.charAt(0) >= 48 && req.charAt(0) <= 57) {
                int number = req.charAt(0) - 48;
                int j = 1;
                while (req.charAt(j) >= 48 && req.charAt(j) <= 57) {
                    j++;
                    number *= 10;
                    number += req.charAt(j) - 48;
                }

                String toAnalyze = req.substring(j + 1);

                counter(toAnalyze, number);
            }
        }
    }

    public void buttonSimulator()
    {
        try {

            /*URL url = new URL(address);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");

            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            String data = "button=btnDypl";

            connection.setDoOutput(true);

            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.writeBytes(data);
            out.flush();
            out.close();

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
                response.append("\n");
            }
            in.close();*/

            WebDriver driver = new ChromeDriver();

            driver.get(address);

            WebElement button = driver.findElement(By.id("btnDypl"));
            button.click();

            /*(new WebDriverWait(driver, Duration.ofMillis(500))).until(new ExpectedCondition<Boolean>() {
                public Boolean apply(WebDriver d) {
                    return d.getCurrentUrl().contains("some_string_that_appears_after_clicking_the_button");
                }
            });*/

            Thread.sleep(1000);


            // Get the page source
            String response = driver.getPageSource();

            //System.out.println(response.toString());

            PrintWriter writer = new PrintWriter("xhtmlAfterButton.txt", StandardCharsets.UTF_8);
            writer.println(response);
            writer.close();
            driver.quit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public  List<String> compare(String file1, String file2)  {
        List<String> differences = new ArrayList<>();
        try {
            BufferedReader reader1 = new BufferedReader(new FileReader(file1));
            BufferedReader reader2 = new BufferedReader(new FileReader(file2));

            String line1 = reader1.readLine();
            String line2 = reader2.readLine();

            while (line1 != null || line2 != null) {
                if (line1 == null || !line1.equals(line2)) {
                    differences.add("< " + line1);
                    differences.add("> " + line2);
                }
                line1 = reader1.readLine();
                line2 = reader2.readLine();
            }

            reader1.close();
            reader2.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return differences;
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

    public String getXHTML() {
        return XHTML;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setXHTML(String XHTML) {
        this.XHTML = XHTML;
    }


    public void setAddress(String address) {
        this.address = address;
    }
}
