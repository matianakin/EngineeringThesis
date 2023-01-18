package project.model;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;


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
            /*for (int i = 0; i < ret.length; i++) {
                ret[i] = ret[i].toLowerCase();
            }*/
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
     */
    public void getXHTML() {
        try {
            /*
            URL url = new URL(address);


            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder xhtml = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                xhtml.append(line);
            }*/

            //System.setProperty("webdriver.chrome.driver", "C:\\chromedriver_win32\\chromedriver.exe");

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


    /**
     * Remove s.
     */
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

    /**
     * Swap words.
     */
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

    /**
     * Counter.
     *
     * @param search the search
     * @param number the number
     */
    private void counter(String search, int number)
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
            errors.add("\n"+"Wrong number of occurrences of "+search+", supposed to be "+number+", is "+count);
        }
    }

    private boolean classChanged(String id, String[] added, String[] deleted)
    {
        String classBeg="";
        String classEnd="";
        for(String d: deleted)
        {
            if(d.contains(id))
            {
                classBeg=d;
                break;
            }
        }
        for(String a: added)
        {
            if(a.contains(id))
            {
                classEnd=a;
                break;
            }
        }
        String[] wordsBeg = classBeg.split("\"");
        String[] wordsEnd = classEnd.split("\"");

        String class1="";
        String class2="";

        for(int i=0; i<wordsBeg.length; i++)
        {
            if(wordsBeg[i].contains("class"))
            {
                class1=wordsBeg[i+1];
                break;
            }
        }
        for(int i=0; i<wordsEnd.length; i++)
        {
            if(wordsEnd[i].contains("class"))
            {
                class2=wordsEnd[i+1];
                break;
            }
        }

        return !class1.equalsIgnoreCase(class2);
    }

    private boolean valueChanged(String id, String[] added, String[] deleted)
    {
        String valueBeg="";
        String valueEnd="";
        for(String d: deleted)
        {
            if(d.contains(id))
            {
                valueBeg=d;
                break;
            }
        }
        for(String a: added)
        {
            if(a.contains(id))
            {
                valueEnd=a;
                break;
            }
        }
        String[] wordsBeg = valueBeg.split("\"");
        String[] wordsEnd = valueEnd.split("\"");

        String value1="";
        String value2="";

        for(int i=0; i<wordsBeg.length; i++)
        {
            if(wordsBeg[i].contains("value"))
            {
                value1=wordsBeg[i+1];
                break;
            }
        }
        for(int i=0; i<wordsEnd.length; i++)
        {
            if(wordsEnd[i].contains("value"))
            {
                value2=wordsEnd[i+1];
                break;
            }
        }

        return !value1.equalsIgnoreCase(value2);
    }

    private boolean styleChanged(String id, String[] added, String[] deleted)
    {
        String styleBeg="";
        String styleEnd="";
        for(String d: deleted)
        {
            if(d.contains(id))
            {
                styleBeg=d;
                break;
            }
        }
        for(String a: added)
        {
            if(a.contains(id))
            {
                styleEnd=a;
                break;
            }
        }
        String[] wordsBeg = styleBeg.split("\"");
        String[] wordsEnd = styleEnd.split("\"");

        String style1="";
        String style2="";

        for(int i=0; i<wordsBeg.length; i++)
        {
            if(wordsBeg[i].contains("style"))
            {
                style1=wordsBeg[i+1];
                break;
            }
        }
        for(int i=0; i<wordsEnd.length; i++)
        {
            if(wordsEnd[i].contains("style"))
            {
                style2=wordsEnd[i+1];
                break;
            }
        }

        return !style1.equalsIgnoreCase(style2);
    }

    private boolean isShown(String id, String[] added, String[] deleted)
    {
        for (String d:deleted)
        {
            var newD = d.replaceAll("\\s", "");
            if(newD.contains(id) && newD.contains("hidden") && !newD.contains("hidden=false"))
            {
                for (String s:added)
                {
                    var newS = s.replaceAll("\\s", "");
                    if(newS.contains(id))
                    {
                        return (!newS.contains("hidden") ||  newS.contains("hidden=false"));
                    }
                }
                return false;
            }
        }
        return false;
    }

    private boolean isHidden(String id, String[] added, String[] deleted)
    {
        for (String s:added)
        {
            var newS = s.replaceAll("\\s", "");
            if(newS.contains(id) && newS.contains("hidden") && !newS.contains("hidden=false"))
            {
                for(String d:deleted)
                {
                    var newD = d.replaceAll("\\s", "");
                    if(newD.contains(id))
                    {
                        return (!newD.contains("hidden") ||  newD.contains("hidden=false"));
                    }
                }
                return false;
            }
        }
        return false;
    }

    private boolean isEnabled(String id, String[] added, String[] deleted)
    {
        for (String d:deleted)
        {
            var newD = d.replaceAll("\\s", "");
            if(newD.contains(id) && newD.contains("disabled") && !newD.contains("disabled=false"))
            {
                for (String s:added)
                {
                    var newS = s.replaceAll("\\s", "");
                    if(newS.contains(id))
                    {
                        return (!newS.contains("disabled") ||  newS.contains("disabled=false"));
                    }
                }
                return false;
            }
        }
        return false;
    }

    private boolean isDisabled(String id, String[] added, String[] deleted)
    {
        for (String s:added)
        {
            var newS = s.replaceAll("\\s", "");
            if(newS.contains(id) && newS.contains("disabled") && !newS.contains("disabled=false"))
            {
                for(String d:deleted)
                {
                    var newD = d.replaceAll("\\s", "");
                    if(newD.contains(id))
                    {
                        return (!newD.contains("disabled") ||  newD.contains("disabled=false"));
                    }
                }
                return false;
            }
        }
        return false;
    }

    private boolean isDeleted(String element, String[] added, String[] deleted)
    {
        for (String d: deleted) {
            if (d.contains(element)) {
                String [] words = d.split("\\s+");
                String id;
                for (String word : words) {
                    if (word.contains("id")) {
                        id = word;
                        for (String s : added) {
                            if (s.contains(element) && s.contains(id)) {
                                return false;
                            }
                        }
                        return true;
                    }
                }
                return true;
            }
        }
        return false;
    }

    private boolean isDeleted(String element, String id, String[] added, String[] deleted)
    {
        for (String d : deleted) {
            if (d.contains(element) && d.contains(id))
            {
                for (String s: added)
                {
                    if(s.contains(element) && s.contains(id))
                    {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    private boolean isAdded(String element, String[] added, String[] deleted)
    {
        for (String s : added) {
            if (s.contains(element)) {
                String [] words = s.split("\\s+");
                String id;
                for (String word : words) {
                    if (word.contains("id")) {
                        id = word;
                        for (String d : deleted) {
                            if (d.contains(element) && d.contains(id)) {
                                return false;
                            }
                        }
                        return true;
                    }
                }
                return true;
            }
        }
        return false;
    }

    private boolean isAdded(String element, String id, String[] added, String[] deleted)
    {
        for (String s : added) {
            if (s.contains(element) && s.contains(id))
            {
                for (String d: deleted)
                {
                    if(d.contains(element) && d.contains(id))
                    {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    private void afterThen(int i, String[] words, String[] added, String[] deleted)
    {
       switch (words [i+2])
       {
           case "appear":
           {
               if(!isAdded(words[i+1], added, deleted))
               {
                   errors.add("\n The condition "+ Arrays.toString(words) + " is not satisfied");
               }
               break;
           }
           case "disappear":
           {
               if(!isDeleted(words[i+1], added, deleted))
               {
                   errors.add("\n The condition "+ Arrays.toString(words) + " is not satisfied");
               }
               break;
           }
           case "change":
           {
               if(words[i+3].equalsIgnoreCase("style")) {
                   if (!styleChanged(words[i + 1], added, deleted)) {
                       errors.add("\n The condition " + Arrays.toString(words) + " is not satisfied");
                   }
               }
               else if (words[i+3].equalsIgnoreCase("value")){
                   if (!valueChanged(words[i + 1], added, deleted)) {
                       errors.add("\n The condition " + Arrays.toString(words) + " is not satisfied");
                   }
               }
               else if (words[i+3].equalsIgnoreCase("clas")){
                   if (!classChanged(words[i + 1], added, deleted)) {
                       errors.add("\n The condition " + Arrays.toString(words) + " is not satisfied");
                   }
               }
               else
               {
                   errors.add("\n Conditional part after \"then\" not recognized");
               }
               break;
           }
           default:
               if(i+3< words.length) {
                   switch (words[i + 3]) {
                       case "appear": {
                           if (!isAdded(words[i + 1], words[i + 2], added, deleted)) {
                               errors.add("\n The condition " + Arrays.toString(words) + " is not satisfied");
                           }
                           break;
                       }
                       case "disappear": {
                           if (!isDeleted(words[i + 1], words[i + 2], added, deleted)) {
                               errors.add("\n The condition " + Arrays.toString(words) + " is not satisfied");
                           }
                           break;
                       }
                       case "disabled": {
                           if (!isDisabled(words[i + 1], added, deleted)) {
                               errors.add("\n The condition " + Arrays.toString(words) + " is not satisfied");
                           }
                           break;
                       }
                       case "enabled": {
                           if (!isEnabled(words[i + 1], added, deleted)) {
                               errors.add("\n The condition " + Arrays.toString(words) + " is not satisfied");
                           }
                           break;
                       }
                       case "hidden": {
                           if (!isHidden(words[i + 1], added, deleted)) {
                               errors.add("\n The condition " + Arrays.toString(words) + " is not satisfied");
                           }
                           break;
                       }
                       case "shown": {
                           if (!isShown(words[i + 1], added, deleted)) {
                               errors.add("\n The condition " + Arrays.toString(words) + " is not satisfied");
                           }
                           break;
                       }
                       default:
                           errors.add("\n Conditional part after \"then\" not recognized");
                   }
               }
               else
               {
                   errors.add("\n Conditional part after \"then\" not recognized");
               }
       }

    }
    
    private void analyzeCondition(String req)
    {
        String[] words = req.split("\\s+");

        int i=0;

        switch (words [1])
        {
            case "button":
            {
                buttonSimulator(words[2]);
                break;
            }
            case "radio":
            {
                radioButtonSimulator(words[2], words[4]);
                break;
            }
            case "checkbox":
            {
                checkboxSimulator(words[2]);
                break;
            }
            case "select":
            {
                dropdownSimulator(words[2], words[4]);
                break;
            }
            case "input":
            {
                if(words[3].equalsIgnoreCase("in") && words[4].equalsIgnoreCase("form"))
                {
                    formInputSimulator(words[2], words[5], words[7]);
                }
                else
                {
                    inputFieldSimulator(words[2], words[4]);
                }
                break;
            }
            default:
                errors.add("\n Unrecognized element in the conditional statement in requirements ");
                //throw new IllegalStateException("Unexpected value: " + words[1]);
                return;
        }
        compare(readLinesFromFile("xhtml.txt"), readLinesFromFile("xhtmlAfter.txt"));

        for(;i< words.length;i++)
        {
            if(words[i].equalsIgnoreCase("then")) {
                break;
            }
        }
        if(i== words.length)
        {
            errors.add("\n Missing \"then\" in the conditional statement in requirements ");
        }
        else
        {
            String[] add = readFromFile("diffAdd.txt");
            String[] sub = readFromFile("diffSub.txt");
            afterThen(i, words, add, sub);
        }
    }

    /**
     * Iterate reqs.
     */
    public void iterateReqs()
    {
        for (String req : reqs) {
            if(!req.isEmpty()) {
                if (req.charAt(0) >= 48 && req.charAt(0) <= 57) {
                    int number = req.charAt(0) - 48;
                    int j = 1;
                    while (req.charAt(j) >= 48 && req.charAt(j) <= 57) {
                        number *= 10;
                        number += req.charAt(j) - 48;
                        j++;
                    }

                    String toAnalyze = req.substring(j + 1);

                    counter(toAnalyze, number);
                } else if ((req.charAt(0) == 'i' || req.charAt(0) == 'I') && ((req.charAt(1) == 'f' || req.charAt(1) == 'F'))) {
                    analyzeCondition(req);
                }
            }
            else
            {
                errors.add("\n Requirement is empty");
            }
        }
    }

    /**
     * Button simulator.
     *
     * @param id the id
     */
    private void buttonSimulator(String id)
    {
        try {

            /*

            old method no1

            URL url = new URL(address);

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

            ChromeOptions options = new ChromeOptions();
            options.setHeadless(true);
            WebDriver driver = new ChromeDriver(options);

            driver.get(address);

            WebElement button = driver.findElement(By.id(id));
            button.click();

            /*


            old method no2

            (new WebDriverWait(driver, Duration.ofMillis(500))).until(new ExpectedCondition<Boolean>() {
                public Boolean apply(WebDriver d) {
                    return d.getCurrentUrl().contains("some_string_that_appears_after_clicking_the_button");
                }
            });*/

            Thread.sleep(1000);

            String response = driver.getPageSource();

            //System.out.println(response.toString());

            PrintWriter writer = new PrintWriter("xhtmlAfter.txt", StandardCharsets.UTF_8);
            writer.println(response);
            writer.close();
            driver.quit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Input field simulator.
     *
     * @param inputName  the input name
     * @param inputValue the input value
     */
    private void inputFieldSimulator(String inputName, String inputValue) {
        try {
            ChromeOptions options = new ChromeOptions();
            options.setHeadless(true);
            WebDriver driver = new ChromeDriver(options);

            driver.get(address);

            WebElement input = driver.findElement(By.id(inputName));
            input.sendKeys(inputValue);

            Thread.sleep(1000);

            String response = driver.getPageSource();

            PrintWriter writer = new PrintWriter("xhtmlAfter.txt", StandardCharsets.UTF_8);
            writer.println(response);
            writer.close();

            driver.quit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Radio button simulator.
     *
     * @param radioButtonName  the radio button name
     * @param radioButtonId the radio button value
     */
    private void radioButtonSimulator(String radioButtonName, String radioButtonId) {
        try {
            ChromeOptions options = new ChromeOptions();
            options.setHeadless(true);
            WebDriver driver = new ChromeDriver(options);

            driver.get(address);

            WebElement radioButton = driver.findElement(By.id(radioButtonId));

            if (!radioButton.isSelected()) {
                radioButton.click();
            }

            Thread.sleep(1000);

            String response = driver.getPageSource();
            PrintWriter writer = new PrintWriter("xhtmlAfter.txt", StandardCharsets.UTF_8);
            writer.println(response);
            writer.close();
            driver.quit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Checkbox simulator.
     *
     * @param checkboxId the checkbox id
     */
    private void checkboxSimulator(String checkboxId) {
        try {
            ChromeOptions options = new ChromeOptions();
            options.setHeadless(true);
            WebDriver driver = new ChromeDriver(options);
            driver.get(address);
            WebElement checkbox = driver.findElement(By.id(checkboxId));
            if (!checkbox.isSelected()) {
                checkbox.click();
            }

            Thread.sleep(1000);

            String response = driver.getPageSource();
            PrintWriter writer = new PrintWriter("xhtmlAfter.txt", StandardCharsets.UTF_8);
            writer.println(response);
            writer.close();
            driver.quit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Dropdown simulator.
     *
     * @param dropdownId  the dropdown id
     * @param optionValue the option value
     */
    private void dropdownSimulator(String dropdownId, String optionValue) {
        try {
            ChromeOptions options = new ChromeOptions();
            options.setHeadless(true);
            WebDriver driver = new ChromeDriver(options);

            driver.get(address);

            WebElement dropdown = driver.findElement(By.id(dropdownId));

            Select select = new Select(dropdown);
            select.selectByValue(optionValue);

            Thread.sleep(1000);

            String response = driver.getPageSource();

            PrintWriter writer = new PrintWriter("xhtmlAfter.txt", StandardCharsets.UTF_8);
            writer.println(response);
            writer.close();

            driver.quit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Form simulator.
     *
     * @param formId     the form id
     * @param inputName  the input name
     * @param inputValue the input value
     */
    private void formInputSimulator(String inputName, String formId, String inputValue) {
        try {
            ChromeOptions options = new ChromeOptions();
            options.setHeadless(true);
            WebDriver driver = new ChromeDriver(options);

            driver.get(address);

            WebElement form = driver.findElement(By.id(formId));
            WebElement input = form.findElement(By.name(inputName));
            input.sendKeys(inputValue);

            List<WebElement> buttons = form.findElements(By.tagName("button"));
            if (buttons.size() > 0) {
                buttons.get(0).click();
            } else {
                form.submit();
            }

            Thread.sleep(1000);

            String response = driver.getPageSource();

            PrintWriter writer = new PrintWriter("xhtmlAfter.txt", StandardCharsets.UTF_8);
            writer.println(response);
            writer.close();

            driver.quit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Read lines from file string [ ].
     *
     * @param filePath the file path
     * @return the string [ ]
     */
    private String[] readLinesFromFile(String filePath) {
        ArrayList<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading the file: " + e.getMessage());
        }
        return lines.toArray(new String[0]);
    }

    /**
     * Compare.
     *
     * @param text1 the text 1
     * @param text2 the text 2
     */
    private void compare(String[] text1, String[] text2)  {
        int lengthDif = Math.abs(text1.length- text2.length);
        int shortLength;
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

        BufferedWriter writer;
        BufferedWriter writer2;

        try {
             writer = new BufferedWriter(new FileWriter("diffAdd.txt"));
             writer2 = new BufferedWriter(new FileWriter("diffSub.txt"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for(int i=0; i<shortLength; i++)
        {
            if(!longText[i].equalsIgnoreCase(shortText[i]))
            {

                try {
                    writer.write(text2[i]);
                    writer.write("\n");
                    writer2.write(text1[i]);
                    writer2.write("\n");
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
                    else if(Arrays.equals(text1, longText))
                    {
                        for(int j=1; j<lengthDif; j++)
                        {
                            try {
                                writer2.write(text1[i+j]);
                                writer2.write("\n");
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
            writer2.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets txt path.
     *
     * @return the txt path
     */
    public String getTxtPath() {
        return txtPath;
    }

    /**
     * Sets txt path.
     *
     * @param txtPath the txt path
     */
    public void setTxtPath(String txtPath) {
        this.txtPath = txtPath;
    }

    /**
     * Get reqs string [ ].
     *
     * @return the string [ ]
     */
    public String[] getReqs() {
        return reqs;
    }

    /**
     * Sets reqs.
     *
     * @param reqs the reqs
     */
    public void setReqs(String[] reqs) {
        this.reqs = reqs;
    }


    /**
     * Gets errors.
     *
     * @return the errors
     */
    public List<String> getErrors() {
        return errors;
    }

    /**
     * Sets xhtml.
     *
     * @param XHTML the xhtml
     */
    public void setXHTML(String XHTML) {
        this.XHTML = XHTML;
    }


    /**
     * Sets address.
     *
     * @param address the address
     */
    public void setAddress(String address) {
        this.address = address;
    }
}
