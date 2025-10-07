package by.pda.demoapp.android.viewModel;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import by.pda.demoapp.android.database.AppDatabase;
import by.pda.demoapp.android.database.AppExecutors;
import by.pda.demoapp.android.utils.SingletonClass;

public class ProductCatalogViewModelFactory implements   ViewModelProvider.Factory {

    private Application mApplication;
    private String mParam;


    public ProductCatalogViewModelFactory(Application application) {
        mApplication = application;

    }


    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        AppDatabase database = AppDatabase.getInstance(mApplication);
        AppExecutors executors = AppExecutors.getInstance();
        SingletonClass singleton = SingletonClass.getInstance();

        return (T) new ProductCatalogViewModel(database.personDao(), executors, singleton);
    }


}
