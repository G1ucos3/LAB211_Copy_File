package controller;

import model.CopyFile;
import repository.CopyRepository;

/**
 *
 * @author ASUS
 */
public class CopyController {
    CopyRepository cr;
    CopyFile data;
    
    public CopyController(){
        cr = new CopyRepository();
        data = new CopyFile();
    }
    
    public void run(){
        System.out.println("========== Copy Program ==========");
        cr.dataCopy(data);
    }
}
