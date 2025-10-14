package by.pda.demoapp.android.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import by.pda.demoapp.android.database.AppDatabase;
import by.pda.demoapp.android.database.AppExecutors;

public class SplashViewModelFactory implements   ViewModelProvider.Factory {
    private final Application mApplication;

    public SplashViewModelFactory(Application application) {
        mApplication = application;
    }


    @Override
    @NonNull
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(SplashViewModel.class)) {
            AppDatabase database = AppDatabase.getInstance(mApplication);
            AppExecutors executors = AppExecutors.getInstance();
            return (T) new SplashViewModel(database.personDao(), executors);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }


}
