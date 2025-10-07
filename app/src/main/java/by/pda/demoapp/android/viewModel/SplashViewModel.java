package by.pda.demoapp.android.viewModel;

import android.app.Application;
import android.os.Handler;
import android.view.View;

import androidx.lifecycle.MutableLiveData;

import by.pda.demoapp.android.database.AppDao;
import by.pda.demoapp.android.database.AppDatabase;
import by.pda.demoapp.android.database.AppExecutors;
import by.pda.demoapp.android.model.ProductModel;
import by.pda.demoapp.android.utils.DatabaseRepository;
import by.pda.demoapp.android.utils.base.BaseViewModel;

import java.util.List;

public class SplashViewModel extends BaseViewModel {
    public MutableLiveData<Integer> pb;
    private final AppDao appDao;
    private final AppExecutors appExecutors;
    public MutableLiveData<List<ProductModel>> allProducts = new MutableLiveData<>();

    public SplashViewModel(AppDao appDao, AppExecutors appExecutors) {
        this.pb = new MutableLiveData<>();
        pb.setValue(View.VISIBLE);
        this.appDao = appDao;
        this.appExecutors = appExecutors;
        getAllProducts();
    }

    public void addDelays() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pb.setValue(View.GONE);
            }
        }, 1000);
    }

    public void getAllProducts() {
        appExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                allProducts.postValue(appDao.getAllProducts());
            }
        });
    }

    public void insertProducts(List<ProductModel> list) {
        appExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                appDao.insertProduct(list);
                pb.postValue(View.GONE);
            }
        });
    }
}
