package by.pda.demoapp.android.viewModel;

import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import by.pda.demoapp.android.database.AppDao;
import by.pda.demoapp.android.database.AppExecutors;
import by.pda.demoapp.android.model.ProductModel;
import by.pda.demoapp.android.utils.base.BaseViewModel;

public class SplashViewModel extends BaseViewModel {
    private final MutableLiveData<Integer> _pb = new MutableLiveData<>();
    private final AppDao appDao;
    private final AppExecutors appExecutors;
    private final LiveData<List<ProductModel>> allProducts;

    public SplashViewModel(AppDao appDao, AppExecutors appExecutors) {
        _pb.setValue(View.VISIBLE);
        this.appDao = appDao;
        this.appExecutors = appExecutors;
        this.allProducts = appDao.getAllProducts();
    }

    public LiveData<Integer> getProgressBarState() {
        return _pb;
    }

    public LiveData<List<ProductModel>> getAllProducts() {
        return allProducts;
    }

    public void insertProducts(List<ProductModel> list) {
        appExecutors.diskIO().execute(() -> {
            appDao.insertProduct(list);
            _pb.postValue(View.GONE);
        });
    }
}
