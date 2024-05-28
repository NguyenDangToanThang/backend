package com.shopelec.backend.service.firebase;

import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class FirebaseStorageService {

    private final Storage storage;

    public FirebaseStorageService() {
        this.storage = StorageOptions.getDefaultInstance().getService();
    }

    public String uploadFile(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        BlobInfo blobInfo = BlobInfo.newBuilder("shopelec-d93e6.appspot.com", fileName).build();
        byte[] content = file.getBytes();
        storage.create(blobInfo, content);
        return fileName;
    }
}
