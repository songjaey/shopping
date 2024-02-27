package com.weapon.shop.service;

import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.util.UUID;

@Service
@Log
public class FileService {
    public String uploadFile(String uploadPath, String orginalFileName,
                             byte[] fileData ) throws Exception{
        UUID uuid = UUID.randomUUID();
        String ext = orginalFileName.substring(orginalFileName.lastIndexOf("."));
        String savefName = uuid.toString()+ext;
        String fileUploadUrl = uploadPath+"/"+savefName;
        FileOutputStream fos = new FileOutputStream(fileUploadUrl);
        fos.write(fileData);
        fos.close();
        return savefName;
    }
    public void deleteFile(String filePath) throws Exception{

    }
}
