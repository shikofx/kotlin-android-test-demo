package by.pda.demoapp.android.viewModel;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import by.pda.demoapp.android.database.AppDatabase;
import by.pda.demoapp.android.database.AppExecutors;

public class ProductDetailViewModelFactory implements   ViewModelProvider.Factory {

    private Application mApplication;
    private String mParam;


    public ProductDetailViewModelFactory(Application application,String mParam) {
        mApplication = application;
        this.mParam = mParam;

    }


    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        AppDatabase database = AppDatabase.getInstance(mApplication);
        AppExecutors executors = AppExecutors.getInstance();
        return (T) new ProductDetailViewModel(database.personDao(), executors, mParam);
    }


}
