package com.douglas.truckflow.firebaseconfigurations;

import androidx.annotation.NonNull;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.data.DataFetcher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FirebaseStorageDataFetcher implements DataFetcher<InputStream> {

    private final StorageReference storageReference;
    private InputStream inputStream;

    FirebaseStorageDataFetcher(StorageReference storageReference) {
        this.storageReference = storageReference;
    }

    @Override
    public void loadData(@NonNull Priority priority, @NonNull DataCallback<? super InputStream> callback) {
        try {
            // Create a temporary file to store the downloaded image
            File tempFile = File.createTempFile("image", null);
            storageReference.getFile(tempFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            try {
                                // Create an InputStream from the downloaded file
                                inputStream = new FileInputStream(tempFile);
                                // Notify the callback with the InputStream
                                callback.onDataReady(inputStream);
                            } catch (IOException e) {
                                e.printStackTrace();
                                // Notify the callback of the error
                                callback.onLoadFailed(e);
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                            // Notify the callback of the error
                            callback.onLoadFailed(e);
                        }
                    })
                    .addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull com.google.android.gms.tasks.Task<FileDownloadTask.TaskSnapshot> task) {
                            // Delete the temporary file when the task is complete
                            tempFile.delete();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
            // Notify the callback of the error
            callback.onLoadFailed(e);
        }
    }

    @Override
    public void cleanup() {
        // Close the InputStream if necessary
        try {
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void cancel() {
        // Cancel the loading task if necessary
    }

    @NonNull
    @Override
    public Class<InputStream> getDataClass() {
        return InputStream.class;
    }

    @NonNull
    @Override
    public DataSource getDataSource() {
        return DataSource.REMOTE;
    }
}
