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
    private final MutableLiveData<List<ProductModel>> _allProducts = new MutableLiveData<>();

    public SplashViewModel(AppDao appDao, AppExecutors appExecutors) {
        _pb.setValue(View.VISIBLE);
        this.appDao = appDao;
        this.appExecutors = appExecutors;
        getAllProducts();
    }

    public LiveData<Integer> getProgressBarState() {
        return _pb;
    }

    public LiveData<List<ProductModel>> getAllProductsLiveData() {
        return _allProducts;
    }

    public void getAllProducts() {
        appExecutors.diskIO().execute(() -> _allProducts.postValue(appDao.getAllProducts()));
    }

    public void insertProducts(List<ProductModel> list) {
        appExecutors.diskIO().execute(() -> {
            appDao.insertProduct(list);
            _pb.postValue(View.GONE);
        });
    }
}
