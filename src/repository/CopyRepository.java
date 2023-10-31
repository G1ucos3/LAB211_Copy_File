package repository;

import dataAccess.FileDAO;
import model.CopyFile;

/**
 *
 * @author ASUS
 */
public class CopyRepository implements ICopy {

    @Override
    public void dataCopy(CopyFile data) {
        FileDAO.Instance().dataCopy(data);
    }
}
