package com.douglas.truckflow.firebaseconfigurations;

import androidx.annotation.NonNull;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.signature.ObjectKey;
import com.douglas.truckflow.firebaseconfigurations.FirebaseStorageDataFetcher;
import com.google.firebase.storage.StorageReference;
import java.io.InputStream;

public class FirebaseStorageModelLoader implements ModelLoader<StorageReference, InputStream> {

    public <Model, Data> FirebaseStorageModelLoader(ModelLoader<Model, Data> build) {

    }

    @Override
    public LoadData<InputStream> buildLoadData(@NonNull StorageReference reference, int width, int height, @NonNull Options options) {
        // Return a new LoadData instance with the appropriate data fetcher
        return new LoadData<>(new ObjectKey(reference), new FirebaseStorageDataFetcher(reference));
    }

    @Override
    public boolean handles(@NonNull StorageReference reference) {
        // Indicate that this ModelLoader can handle the given StorageReference
        return true;
    }
}

