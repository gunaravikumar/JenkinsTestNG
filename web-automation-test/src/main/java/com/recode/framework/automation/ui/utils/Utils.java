package com.recode.framework.automation.ui.utils;

import com.recode.framework.automation.ui.pageobjects.PageClass;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Utils {
    private static boolean readProperties = false;
    protected static Properties setupProperties = new Properties();

    /**
     * This method is used to read the properties file.
     */
    private static void readAutomationProperties() throws IllegalStateException{
        if (!readProperties) {
            String propFilePath = System.getProperty("automationpropfile", "Automation_Config.properties");
            System.out.println(propFilePath);  // Need to replace with Logger
            try
            {
                InputStream fileStream = PageClass.class.getClassLoader().getResourceAsStream(propFilePath);
                setupProperties.load(fileStream);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                throw new IllegalStateException("unable to load properties file:" + propFilePath);
            }
        }
        readProperties = true;
    }

     /**
     * This method is used to read the properties file
     * @return Properties
     */
    public static Properties getAutomationProperties() {
        readAutomationProperties();
        return setupProperties;
    }

    /**
     * This method is used to pause the execution of current thread for specified time in seconds
     * @param seconds
     */
    public static void sleep(int seconds) throws InterruptedException {
        Thread.sleep(Duration.ofSeconds(seconds).toMillis());
        Logger.instance.info("Sleeping for "+ seconds +" seconds.");
    }

    /**
     * This method is used to pause the execution of current thread for specified time in milliseconds with reason
     * @param seconds
     * @param reason
     */
    public static void sleepWithReason(int seconds, String reason) throws InterruptedException {
        Thread.sleep(Duration.ofSeconds(seconds).toMillis());
        Logger.instance.info("Sleeping for "+ seconds +" seconds and Reason is " +reason);
    }

    /**
     * This method is to kill the windows process
     * @param processName - Process Name to kill
     */
    public static void killProcess(String processName){
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        boolean isRunning = isProcessRunning(processName);
        Logger.instance.info("is " + processName + " running : " + isRunning);
        try {
            if (isRunning) {
                Runtime.getRuntime().exec("taskkill /F /IM " + processName);
                Logger.instance.info(processName + " killed successfully!");
            } else {
                Logger.instance.info("Not able to find the process : " + processName);
            }
        }
        catch (IOException e) {
            Logger.instance.error("Error occured in the method: "+name+" because of " + e.getMessage());
        }
    }

    /**
     * This method is to check the given windows process is running or not.
     * @param processName - Process name to check
     * @return True if its running. Else, False.
     */
    public static boolean isProcessRunning(String processName){
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        try{
            Process pro = Runtime.getRuntime().exec("tasklist");
            BufferedReader reader = new BufferedReader(new InputStreamReader(pro.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(processName)) {
                    return true;
                }
            }
        } catch (IOException e) {
            Logger.instance.error("Error occured in the method: "+name+" because of " + e.getMessage());
        }
        return false;
    }

    /**
     * This method is used to copy the file from sourcePath to destinationPath
     * @param sourcePath - (new File(sourceFilePathLocation))
     * @param destinationPath - (new File(destinationFilePathLocation))
     * */
    public static void copyFile(File sourcePath, File destinationPath) {
        try {
            if(sourcePath.exists()){
                File destinationLocation = new File(String.valueOf(destinationPath));
                if(!destinationLocation.exists()){
                    destinationLocation.createNewFile();
                }
                FileUtils.copyFile(sourcePath, destinationPath);
                Logger.instance.info("Copied a file from '" + sourcePath + "' to '" + destinationPath + "'");
            } else {
                Logger.instance.error(sourcePath.getAbsoluteFile() + " FilePath is not exists");
            }

        } catch (Exception exp) {
            Logger.instance.error("Error while executing 'copyFile' method : " + exp);
        }
    }

    /**
     * This method is used to deletes the file or directory denoted by filepath.
     * @param filePath - (new File(filePathLocation))
     * */
    public static boolean deleteFile(File filePath) {
        try {
            if(filePath.exists()){
                if (filePath.delete()) {
                    Logger.instance.info(filePath + " File Deleted Successfully.");
                    return true;
                } else {
                    Logger.instance.error(filePath + " Deletion failed!!!");
                }
            } else {
                Logger.instance.error(filePath + " FilePath is not exists");
            }
        } catch (Exception exp) {
            Logger.instance.error("Error while executing 'deleteFile' method : " + exp);
        }
        return false;
    }

    /**
     * This method is to search a particular word in file
     * @param searchKeyword
     * @param path
     *
     */
    public boolean searchWordInTextFile(String searchKeyword, String path)
    {
        try
        {
            String[] words = null;
            String lineText = null;
            File f1 = new File(path);
            FileReader fr = new FileReader(f1);
            BufferedReader br = new BufferedReader(fr);
            while ((lineText = br.readLine()) != null)
            {
                words = lineText.split(" ");
                for (String word : words)
                {
                    if (word.toLowerCase().equals(searchKeyword.toLowerCase()))
                    {
                        fr.close();
                        Logger.instance.info("The given word :'" + searchKeyword + "' is present in the file");
                        return true;
                    }
                }
            }

        }
        catch (Exception e){
            Logger.instance.error("Exception occurred due to following exception :" + e.getMessage());
        }
        return false;
    }
    /**
     * This method is to create a file directory
     * @param path
     */
    public void makeDirectory(String path){
        try{
            File file = new File(path);
            file.createNewFile();
            boolean flag= file.mkdir();
            Logger.instance.info("A new file Directory is created in the mentioned path");
        }
        catch(Exception e){
            Logger.instance.error("New file Directory is not created in the mentioned path"+e);
        }
    }

    /**
     * This method is to delete a file directory
     * @param file
     */
    public static boolean deleteDirectory(File file){
        try{
            File[] list = file.listFiles();
            if (list != null) {
                for (File temp : list) {
                    //recursive delete
                    deleteDirectory(temp);
                }
            }

            if (file.delete()) {
                Logger.instance.info("Deleted file/folder : " + file.getAbsolutePath());
                return true;
            } else {
                Logger.instance.error("Unable to delete file/folder : " + file.getAbsolutePath());
            }
        }
        catch(Exception e){
            Logger.instance.error("The file Directory mentioned in the path is not deleted" +e);
        }
        return false;
    }

    /**
     * This method is to start Windows service when the service is in stopped state.
     * @param serviceName - Name of the service.
     */
    public static void startWindowsService(String serviceName){
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        try{
            int attempt = 1;
            if(checkWindowsServiceStatus(serviceName) == 2) {
                String[] script = {"cmd.exe", "/c", "sc", "start", serviceName};
                Process process = new ProcessBuilder(script).start();
                process.waitFor();

                while (checkWindowsServiceStatus(serviceName) != 1){
                    sleepWithReason(15, "Attempt: "+attempt+" - Waiting for service start.");
                    if (attempt >= 6) {
                        Logger.instance.info(serviceName + " service is not started after "+attempt+" attempts");
                        break;
                    }
                    attempt++;
                }

                if (attempt <= 5) {
                    Logger.instance.info(serviceName + " service is started successfully");
                }
            }
        }
        catch (Exception e) {
            Logger.instance.error("Error occured in the method: "+name+" because of " + e.getMessage());
        }
    }

    /**
     * This method is to stop Windows service when the service is in running state.
     * @param serviceName - Name of the service.
     */
    public static void stopWindowsService(String serviceName){
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        try{
            int attempt = 1;
            if(checkWindowsServiceStatus(serviceName) == 1) {
                String[] script = {"cmd.exe", "/c", "sc", "stop", serviceName};
                Process process = new ProcessBuilder(script).start();
                process.waitFor();

                while (checkWindowsServiceStatus(serviceName) != 2){
                    sleepWithReason(15, "Attempt: "+attempt+" - Waiting for service stop.");
                    if (attempt >= 6) {
                        Logger.instance.info(serviceName + " service is not stopped after "+attempt+" attempts");
                        break;
                    }
                    attempt++;
                }
                if (attempt <= 5) {
                    Logger.instance.info(serviceName + " service is stopped successfully");
                }
            }
        }
        catch (Exception e) {
            Logger.instance.error("Error occured in the method: "+name+" because of " + e.getMessage());
        }
    }

    /**
     * This method is to restart the Windows service.
     * @param serviceName - Name of the service.
     */
    public static void restartWindowsService(String serviceName){
        stopWindowsService(serviceName);
        startWindowsService(serviceName);
    }

    /**
     * This method is to check the Windows service status and the service exist.
     * @param serviceName - Name of the service.
     * @return 1 for Running, 2 for Stopped, 3 for InProgress, 4 for Does not exist, 0 for Error.
     */
    public static int checkWindowsServiceStatus(String serviceName){
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        try {
            String outputLine;
            String[] findScript = {"cmd.exe", "/c", "sc", "query", serviceName};
            Process process = new ProcessBuilder(findScript).start();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            while ((outputLine = bufferedReader.readLine()) != null) {
                if (outputLine.contains("STATE")) {
                    if(outputLine.contains("RUNNING")){
                        Logger.instance.info(serviceName+ " - service is in running state.");
                        return 1;
                    }
                    else if (outputLine.contains("STOPPED")){
                        Logger.instance.info(serviceName+ " - service is in stopped state.");
                        return 2;
                    }
                    else {
                        Logger.instance.info(serviceName+ " - service is in progress of starting or stopping state.");
                        return 3;
                    }
                }
                else if(outputLine.contains("does not exist")){
                    Logger.instance.info(serviceName+ " - service does not exist as an installed service.");
                    return 4;
                }
            }
        }
        catch (Exception e) {
            Logger.instance.error("Error occured in the method: "+name+" because of " + e.getMessage());
        }
        return 0;
    }


    /**
     * This method is used to extract a file from the ZIP file.
     */
    public static void unZipFile(String sourceFilePath, String destinationPath) {
        try {
            int BUFFER = 2048;
            File file = new File(sourceFilePath);
            ZipFile zip = new ZipFile(file);
            new File(destinationPath).mkdir();
            Enumeration zipFileEntries = zip.entries();
            // Process each entry
            while (zipFileEntries.hasMoreElements()) {
                // grab a zip file entry
                ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();
                File destFile = new File(destinationPath, entry.getName());
                File destinationParent = destFile.getParentFile();
                // create the parent directory structure if needed
                destinationParent.mkdirs();
                if (!entry.isDirectory()) {
                    BufferedInputStream is = new BufferedInputStream(zip.getInputStream(entry));
                    int currentByte;
                    // establish buffer for writing file
                    byte data[] = new byte[BUFFER];
                    // write the current file to disk
                    FileOutputStream fos = new FileOutputStream(destFile);
                    BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER);
                    // read and write until last byte is encountered
                    while ((currentByte = is.read(data, 0, BUFFER)) != -1) {
                        dest.write(data, 0, currentByte);
                    }
                    dest.flush();
                    dest.close();
                    is.close();
                }
            }
        } catch (Exception exp) {
            Logger.instance.error("Error while executing 'unZipFile' method : " + exp);
        }
    }
    /**
     * This method is to trigger the batch file or any script with input parameters.
     * @param file - Absolute Path of the file.
     * @param args - Input Parameters. Have to pass with space for multi parameters.
     */
    public static void scriptStart(String file, String args){
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        try {
            File scriptFile = new File(file);

            List<String> script = new ArrayList<>();
            script.add("cmd.exe");
            script.add("/c");
            script.add(scriptFile.getName());

            if(args != null && !args.isEmpty()) {
                String[] arguments = args.split(" ");
                Collections.addAll(script, arguments);
            }

            ProcessBuilder processBuilder= new ProcessBuilder(script);
            processBuilder.directory(new File(scriptFile.getParent()));
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            process.waitFor();
            if (process.exitValue() == 0) {
                Logger.instance.info(scriptFile.getName()+" script is executed successfully without error");
            } else {
                Logger.instance.error(scriptFile.getName()+" script is not executed successfully");
            }

        } catch (Exception e) {
            Logger.instance.error("Error occured in the method: "+name+" because of " + e.getMessage());
        }
    }

    /**
     * This method is to compare two images with tolerance value.
     * @param image1         - Base Image
     * @param image2         - Compare Image
     * @param pixelTolerance - Value for tolerance
     * @return true if both images are equal otherwise false
     */
    public static Boolean compareImagesWithTolerance(String image1, String image2, int pixelTolerance) {
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        try {
            File fileA = new File(image1);
            File fileB = new File(image2);
            BufferedImage imgA = ImageIO.read(fileA);
            BufferedImage imgB = ImageIO.read(fileB);

            int flag = 0;

            if ((imgA.getWidth() != imgB.getWidth()) || (imgA.getHeight() != imgB.getHeight())) {
                Logger.instance.error("Unable to compare because the image dimensions are mismatched for " + fileA.getName() + " and " + fileB.getName());
                return false;
            }

            for (int y = 0; y < imgA.getHeight(); y++) {
                for (int x = 0; x < imgA.getWidth(); x++) {
                    if (imgA.getRGB(x, y) != imgB.getRGB(x, y)) {
                        flag++;
                        break;
                    }
                }
            }

            if (flag == 0) {
                Logger.instance.info("Pixel of both images " + fileA.getName() + " and " + fileB.getName() + " are equal");
                return true;
            } else {
                Logger.instance.info("Total Flag value is :" + flag);
                if (flag <= pixelTolerance) {
                    Logger.instance.info("Flag value (mismatch pixel count) '" + flag + "' less than or equal to set tolerance: " + pixelTolerance);
                    Logger.instance.info("Pixel of both images " + fileA.getName() + " and " + fileB.getName() + " are equal");
                    return true;
                } else {
                    Logger.instance.info("Flag value (mismatch pixel count) '" + flag + "' NOT less than or equal to set tolerance: " + pixelTolerance);
                    Logger.instance.error("Pixel of both images " + fileA.getName() + " and " + fileB.getName() + " are not equal");
                    return false;
                }
            }
        } catch (Exception e) {
            Logger.instance.error("Error occurred in the method: " + name + " because of " + e.getMessage());
        }
        return false;
    }

    /**
     * This method is to compare two images and create a difference image based on the base image.
     * @param image1 - Base Image
     * @param image2 - Compare Image
     * @param testCaseName - Testcase name to copy the diff Image and also for diff Image naming.
     * @return - true if both images are equal otherwise false
     */
    public static boolean compareImages(String image1, String image2, String testCaseName) {
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        try {
            String diffImagePath = Utils.getAutomationProperties().getProperty("DiffImageDir");

            File fileA = new File(image1);
            File fileB = new File(image2);
            BufferedImage imgA = ImageIO.read(fileA);
            BufferedImage imgB = ImageIO.read(fileB);

            int flag = 0;
            long difference = 0;
            int width1 = imgA.getWidth();
            int height1 = imgA.getHeight();

            if ((imgA.getWidth() != imgB.getWidth()) || (imgA.getHeight() != imgB.getHeight())) {
                Logger.instance.error("Unable to compare because the image dimensions are mismatched for " + fileA.getName() + " and " + fileB.getName());
                return false;
            }

            File diffFilePath = new File(System.getProperty("user.dir") + diffImagePath + File.separator + testCaseName);
            if (!diffFilePath.exists()) {
                diffFilePath.mkdirs();
                Logger.instance.info("The diff image directory path is created " + diffFilePath);
            }

            File diffFile = new File(System.getProperty("user.dir") + diffImagePath + File.separator + testCaseName+ File.separator + testCaseName+"_Diff"+".png");
            copyFile(fileA, diffFile);
            BufferedImage diffImg = ImageIO.read(diffFile);

            for (int y = 0; y < height1; y++) {
                for (int x = 0; x < width1; x++) {
                    int rgbA = imgA.getRGB(x, y);
                    int rgbB = imgB.getRGB(x, y);
                    Color colorA = new Color(rgbA, true);
                    Color colorB = new Color(rgbB, true);

                    difference += Math.abs(colorA.getRed() - colorB.getRed());
                    difference += Math.abs(colorA.getGreen() - colorB.getGreen());
                    difference += Math.abs(colorA.getBlue() - colorB.getBlue());

                    if (!((Math.abs(colorA.getRed() - colorB.getRed())) <= 100) || !((Math.abs(colorA.getGreen() - colorB.getGreen())) <= 100) || !((Math.abs(colorA.getBlue() - colorB.getBlue())) <= 100)) {
                        flag++;
                        diffImg.setRGB(x, y, Color.red.getRGB());
                    }
                }
            }

            double total_pixels = width1 * height1 * 3;
            double avg_different_pixels = difference / total_pixels;
            double percentage = (avg_different_pixels / 255) * 100;
            Logger.instance.info("Difference Percentage-->" + percentage);

            Logger.instance.info("Total Flag value : " + flag);
            if (flag == 0) {
                Logger.instance.info("Pixel of both images " + fileA.getName() + " and " + fileB.getName() + " are equal");
                return true;
            } else {
                ImageIO.write(diffImg, "PNG", diffFile);
                Logger.instance.info("The differences of both images are highlighted in red color. Diff file Location: " + diffFile);
                Logger.instance.error("Pixel of both images " + fileA.getName() + " and " + fileB.getName() + " are not equal");
                return false;
            }
        } catch (Exception e) {
            Logger.instance.error("Error occurred in the method: " + name + " because of " + e.getMessage());
        }
        return false;
    }

    /**
     * This method is to fetch the hostname using ip address
     * @param IPAddress - IP Address
     * @return String
     *
     */
    public String getHostName(String IPAddress){
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        String hostname = null;
        try {
            if (IPAddress != null && !IPAddress.isEmpty()) {
                InetAddress address = InetAddress.getByName(IPAddress);
                hostname = address.getHostName();
                Logger.instance.info("The host name for the particular ip is retrieved");
            } else
                Logger.instance.error("IP address is either null or empty");
        }
        catch (Exception e){
            Logger.instance.error("Error occurred in the method: " + name +" because of " + e.getMessage());
        }
        return hostname;
    }

    /**
     * This method is used to check the file is exists or not.
     * @param filePath
     * @param fileName
     * */
    public static boolean isFileExists(String filePath, String fileName) {
        try {
            File file = new File(filePath, fileName);
            if (file.exists()) {
                Logger.instance.info(fileName + " file is exists in the " + filePath);
                return true;
            } else {
                Logger.instance.error(fileName + " file is not exists in the " + filePath);
                return false;
            }
        } catch (Exception exp) {
            Logger.instance.error("Error while executing 'isFileExists' method : " + exp);
        }
        return false;
    }

    /**
     * This method is to capture the entire screen using Selenium Screenshot.
     * @param driver - WebDriver instance
     * @param testProtocol - Used to create child folder to keep the capture files.
     * @param testCaseName - Used to create child folder to keep the capture files.
     */
    public static String captureScreenshot(WebDriver driver, String testProtocol, String testCaseName) {
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        String imagePath = "";
        try {
            LocalDateTime date = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd_hh_mm_ss");
            String timeText = date.format(formatter);

            String screenShotDir = Utils.getAutomationProperties().getProperty("ScreenShotDir");
            File testcaseDir = new File(System.getProperty("user.dir") + screenShotDir + File.separator + testProtocol + File.separator + testCaseName);
            if (!testcaseDir.exists()) {
                testcaseDir.mkdirs();
            }
            testcaseDir.setWritable(true, false);
            testcaseDir.setReadable(true, false);
            testcaseDir.setExecutable(true, false);

            TakesScreenshot screenShot = ((TakesScreenshot) driver);
            File srcFile = screenShot.getScreenshotAs(OutputType.FILE);
            imagePath = testcaseDir + File.separator + timeText + "_" + testCaseName + ".png";
            File destFile = new File(imagePath);
            FileUtils.copyFile(srcFile, destFile);
            destFile.setWritable(true, false);
            destFile.setReadable(true, false);
        } catch (Exception exp) {
            Logger.instance.error("Error occurred in the method: " + name + " because of " + exp.getMessage());
        }
        return imagePath;
    }
    /**
     * This method is to get the current date & time with specific pattern
     * @throws Exception
     * @return It return the Currentdatetime value
     */
    public static String getCurrentdatetime() {

        LocalDateTime currentDate = LocalDateTime.now();
        DateTimeFormatter formatter =DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm:ss a");
        String dateTimeRightNow = currentDate.format(formatter);
        return dateTimeRightNow;
    }


    /**
     * This method is to find the difference between date and time
     * @throws Exception
     * @return It return date&time difference value
     * @param start_date
     * @param end_date
     *
     */
    public static String findDateDifference(String start_date, String end_date)
    {

        // SimpleDateFormat converts the
        // string format to date object
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a");

        // Try Block
        try {

            // parse method is used to parse
            // the text from a string to
            // produce the date
            Date d1 = (Date) sdf.parse(end_date);
            Date d2 = (Date) sdf.parse(start_date);


            // Calculate time difference
            // in milliseconds
            long difference_In_Time= d2.getTime() - d1.getTime();

            // Calculate time difference in
            // seconds, minutes, hours, years,
            // and days
            long difference_In_Seconds= (difference_In_Time/ 1000)% 60;

            long difference_In_Minutes= (difference_In_Time/ (1000 * 60))% 60;

            long difference_In_Hours= (difference_In_Time/ (1000 * 60 * 60))% 24;

            long difference_In_Days= (difference_In_Time/ (1000 * 60 * 60 * 24))% 365;

            // Print the date difference in
            // years, in days, in hours, in
            // minutes, and in seconds

            String resultDatedifference= difference_In_Days+ " days,"
                    + difference_In_Hours + " hours, "+ difference_In_Minutes+ " minutes, " +
                    + difference_In_Seconds + " seconds";
            return resultDatedifference;
        }
        // Catch the Exception
        catch (Exception e) {
            Logger.instance.error("Error occurred in the method while finding the difference between date's " + e.getMessage());
        }

        return "verify the start_date and end_date";

    }
}