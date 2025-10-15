package by.pda.demoapp.android.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import by.pda.demoapp.android.database.AppDatabase;
import by.pda.demoapp.android.database.AppExecutors;

public class ProductDetailViewModelFactory implements   ViewModelProvider.Factory {

    private final Application mApplication;
    private final String mParam;


    public ProductDetailViewModelFactory(Application application,String mParam) {
        mApplication = application;
        this.mParam = mParam;

    }


    @Override
    @NonNull
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ProductDetailViewModel.class)) {
            AppDatabase database = AppDatabase.getInstance(mApplication);
            AppExecutors executors = AppExecutors.getInstance();
            return (T) new ProductDetailViewModel(database.appDao(), executors, mParam);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }


}
