package com.manager;

import com.azure.storage.blob.*;
import com.azure.storage.blob.models.BlobStorageException;

import jakarta.servlet.http.Part;

import java.io.InputStream;
import java.util.UUID;

public class BlobStorage {
	//upload image
    private final BlobContainerClient containerClient;

    public BlobStorage() {
        String connectionString = System.getenv("AZURE_STORAGEBLOB_CONNECTIONSTRING");
        String containerName = "product-images";
        this.containerClient = new BlobServiceClientBuilder()
                .connectionString(connectionString)
                .buildClient()
                .getBlobContainerClient(containerName);
    }

    public String uploadImage(InputStream inputStream, long size, String originalFileName) {
        String uniqueFileName = UUID.randomUUID() + "-" + originalFileName;

        try {
            BlobClient blobClient = containerClient.getBlobClient(uniqueFileName);
            blobClient.upload(inputStream, size, true);
            return blobClient.getBlobUrl();
        } catch (BlobStorageException e) {
            throw new RuntimeException("Blob upload failed: " + e.getMessage(), e);
        }
    }
    
    //update image
    private static final String CONNECTION_STRING = System.getenv("AZURE_STORAGEBLOB_CONNECTIONSTRING");

    public String uploadImage(Part imagePart, String containerName) throws Exception {
        BlobContainerClient containerClient = new BlobContainerClientBuilder()
                .connectionString(CONNECTION_STRING)
                .containerName(containerName)
                .buildClient();

        String imageName = "product_" + System.currentTimeMillis() + ".jpg";
        BlobClient blobClient = containerClient.getBlobClient(imageName);

        try (InputStream inputStream = imagePart.getInputStream()) {
            blobClient.upload(inputStream, imagePart.getSize(), true);
        }

        return blobClient.getBlobUrl();
    }
}
