package by.pda.demoapp.android.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import by.pda.demoapp.android.database.AppDatabase;
import by.pda.demoapp.android.database.AppExecutors;
import by.pda.demoapp.android.utils.SingletonClass;

public class ProductCatalogViewModelFactory implements   ViewModelProvider.Factory {

    private final Application mApplication;

    public ProductCatalogViewModelFactory(Application application) {
        mApplication = application;

    }


    @Override
    @NonNull
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ProductCatalogViewModel.class)) {
            AppDatabase database = AppDatabase.getInstance(mApplication);
            AppExecutors executors = AppExecutors.getInstance();
            SingletonClass singleton = SingletonClass.getInstance();
            return (T) new ProductCatalogViewModel(database.personDao(), executors, singleton);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }


}
