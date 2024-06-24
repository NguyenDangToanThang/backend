package com.shopelec.backend.service.firebase;

import com.google.cloud.storage.Acl;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FirebaseStorageService {

    final private Storage storage;

    public String uploadFile(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        String filePath = "image/" + fileName;
        BlobInfo blobInfo = BlobInfo.newBuilder("shopelec-d93e6.appspot.com", filePath)
                .setContentType(file.getContentType())
                .build();
        byte[] content = file.getBytes();
        Blob blob = storage.create(blobInfo, content);

        storage.createAcl(blob.getBlobId(), Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER));

        return filePath;
    }

    public String getFileUrl(String fileName) {
        return String.format("https://storage.googleapis.com/%s/%s", "shopelec-d93e6.appspot.com", fileName);
    }
}
