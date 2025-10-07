package by.pda.demoapp.android.viewModel;

import androidx.lifecycle.LiveData;
import android.util.Log;
import androidx.lifecycle.MutableLiveData;

import by.pda.demoapp.android.database.AppDao;
import by.pda.demoapp.android.database.AppExecutors;
import by.pda.demoapp.android.model.ProductModel;
import by.pda.demoapp.android.utils.base.BaseViewModel;

public class ProductDetailViewModel extends BaseViewModel {
    private final AppDao appDao;
    private final AppExecutors appExecutors;
    private final String id;
    private final MutableLiveData<ProductModel> _product = new MutableLiveData<>();

    public ProductDetailViewModel(AppDao appDao, AppExecutors appExecutors, String id) {
        this.appDao = appDao;
        this.appExecutors = appExecutors;
        this.id = id;
        getProduct();
    }

    public LiveData<ProductModel> getProductLiveData() {
        return _product;
    }

    public void getProduct() {
        appExecutors.diskIO().execute(() -> {
            try {
                _product.postValue(appDao.getProduct(Integer.parseInt(id)));
            } catch (NumberFormatException e) {
                Log.e("ProductDetailViewModel", "Invalid product ID format: " + id, e);
            }
        });
    }



}
