package dataAccess;

import common.Validate;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Properties;
import model.CopyFile;

/**
 *
 * @author ASUS
 */
public class FileDAO {

    private static FileDAO instance = null;
    Validate validate = new Validate();

    public static FileDAO Instance() {
        if (instance == null) {
            synchronized (FileDAO.class) {
                if (instance == null) {
                    instance = new FileDAO();
                }
            }
        }
        return instance;
    }
    
    public void dataCopy(CopyFile data) {
        readFileConfig(data);
        copyFile(data);
    }

    public boolean checkFileConfig(String copyFolder, String path) {
        File f = new File(copyFolder);
        if (!f.isDirectory()) {
            System.out.println("Folder not exist");
            return false;
        }
        f = new File(path);
        if (!f.isDirectory()) {
            if (f.mkdir()) {
                return true;
            } else {
                System.out.println("Fail to create path");
                return false;
            }
        }
        return true;
    }

    public void createFileConfig(CopyFile data) {
        System.out.println("File Config not found!");
        System.out.println("---- Input Config File ----");
        data.setCopyFolder(validate.checkString("Copy Folder"));
        data.setDataType(validate.checkString("Data Type"));
        data.setPath(validate.checkString("Path"));
        if (!checkFileConfig(data.getCopyFolder(), data.getPath())) {
            System.out.println("System Shutdown...");
            System.exit(0);
        }
        Properties prop = new Properties();
        prop.setProperty("COPY_FOLDER", data.getCopyFolder());
        prop.setProperty("DATA_TYPE", data.getDataType());
        prop.setProperty("PATH", data.getPath());
        try {
            OutputStream output = new FileOutputStream("src\\config.properties");
            prop.store(output, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readFileConfig(CopyFile data) {
        try (InputStream input = new FileInputStream(("src\\config.properties"))) {
            Properties prop = new Properties();
            prop.load(input);
            data.setCopyFolder(prop.getProperty("COPY_FOLDER"));
            data.setDataType(prop.getProperty("DATA_TYPE"));
            data.setPath(prop.getProperty("PATH"));
            if (!checkFileConfig(data.getCopyFolder(), data.getPath())) {
                System.out.println("System Shutdown...");
                System.exit(0);
            }
        } catch (IOException | NullPointerException e) {
            createFileConfig(data);
        }
    }

    public boolean checkPath(String[] match, String name) {
        for (String str : match) {
            if(str.isBlank()) continue;
            if (name.endsWith(str)) {
                return true;
            }
        }
        return false;
    }

    public void copyFile(CopyFile data) {
        try{
            File test = new File(data.getCopyFolder());
        }
        catch(NullPointerException e){
            createFileConfig(data);
        }
        File f = new File(data.getCopyFolder());
        File[] files = f.listFiles();
        String[] str = data.getDataType().split("[^(\\.a-zA-Z)]|[\\..+\\..+]");
        System.out.println("Copy is running...");
        System.out.println("----------- File Name -----------");
        for (File file : files) {
            if (file.isFile() && checkPath(str, file.getName())) {
                try {
                    File destination = new File(data.getPath(), file.getName());
                    System.out.println("File name: " + file.getName());
                    Files.copy(file.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("Copy is finished...");
    }
}
