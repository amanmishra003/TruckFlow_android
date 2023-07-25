package com.example.truckflow.firebaseconfigurations;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import com.example.truckflow.firebaseconfigurations.FirebaseStorageModelLoader;
import com.google.firebase.storage.StorageReference;
import java.io.InputStream;

public class FirebaseStorageModelLoaderFactory implements ModelLoaderFactory<StorageReference, InputStream> {

    @NonNull
    @Override
    public ModelLoader<StorageReference, InputStream> build(@NonNull MultiModelLoaderFactory multiFactory) {
        return new FirebaseStorageModelLoader(multiFactory.build(InputStream.class, InputStream.class));
    }

    @Override
    public void teardown() {
        // No special cleanup required.
    }
}
