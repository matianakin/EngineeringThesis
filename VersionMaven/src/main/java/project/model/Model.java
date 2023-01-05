package project.model;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;


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



    public String[] readFromFile(String path) {
        String[] ret = null;
        try {
            ret = readFromInputStream(new FileInputStream(path)).split("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }


    public String readFromXHTMLFile(String path) {
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

    public void buttonSimulator(String id)
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

            WebElement button = driver.findElement(By.id(id));
            button.click();

            /*(new WebDriverWait(driver, Duration.ofMillis(500))).until(new ExpectedCondition<Boolean>() {
                public Boolean apply(WebDriver d) {
                    return d.getCurrentUrl().contains("some_string_that_appears_after_clicking_the_button");
                }
            });*/

            Thread.sleep(1000);

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

    public void inputFieldSimulator(String inputName, String inputValue) {
        try {
            WebDriver driver = new ChromeDriver();

            driver.get(address);

            WebElement input = driver.findElement(By.name(inputName));
            input.sendKeys(inputValue);

            String response = driver.getPageSource();

            PrintWriter writer = new PrintWriter("xhtmlAfterInputField.txt", StandardCharsets.UTF_8);
            writer.println(response);
            writer.close();

            driver.quit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void formSimulator(String formId, String inputName, String inputValue) {
        try {
            WebDriver driver = new ChromeDriver();

            driver.get(address);

            WebElement form = driver.findElement(By.id(formId));
            WebElement input = form.findElement(By.name(inputName));
            input.sendKeys(inputValue);

            form.submit();

            String response = driver.getPageSource();

            PrintWriter writer = new PrintWriter("xhtmlAfterFormSubmit.txt", StandardCharsets.UTF_8);
            writer.println(response);
            writer.close();

            driver.quit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String[] readLinesFromFile(String filePath) {
        ArrayList<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading the file: " + e.getMessage());
        }
        return lines.toArray(new String[0]);
    }

    public  void compare(String[] text1, String[] text2)  {
        int lengthDif = Math.abs(text1.length- text2.length);
        ArrayList<String> diffs = new ArrayList<>();
        int shortLength =0;
        var longText = text1;
        var shortText = text2;
        if(text1.length> text2.length)
        {
            shortLength = text2.length;
        }
        else
        {
            shortLength = text1.length;
            longText=text2;
            shortText=text1;
        }

        BufferedWriter writer = null;

        try {
             writer = new BufferedWriter(new FileWriter("diff.txt"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for(int i=0; i<shortLength; i++)
        {
            if(!longText[i].equalsIgnoreCase(shortText[i]))
            {
                String line = text2[i];
                try {
                    writer.write(line);
                    writer.write("\n");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                if(i+1<shortText.length&&longText[i+1]!=null&&i+1+lengthDif< longText.length&&longText[i+1+lengthDif].equalsIgnoreCase(shortText[i+1]))
                {
                    if(Arrays.equals(text2, longText))
                    {
                        for(int j=1; j<lengthDif; j++)
                        {
                            try {
                                writer.write(text2[i+j]);
                                writer.write("\n");
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                    break;
                }
            }
        }
        try {
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
