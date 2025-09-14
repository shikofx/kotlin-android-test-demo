package by.pda.demoapp.android.viewModel;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import by.pda.demoapp.android.database.AppDatabase;
import by.pda.demoapp.android.database.AppExecutors;
import by.pda.demoapp.android.model.ProductModel;
import by.pda.demoapp.android.utils.DatabaseRepository;
import by.pda.demoapp.android.utils.base.BaseViewModel;

public class ProductDetailViewModel extends BaseViewModel {
    private AppDatabase mDb;
    private DatabaseRepository repository;
    String id;
    public MutableLiveData<ProductModel> product = new MutableLiveData<>();

    public ProductDetailViewModel(Application app,String id) {
        mDb = AppDatabase.getInstance(app);
        this.id = id;
        getProduct();
    }

    public void getProduct() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {

                product.postValue(mDb.personDao().getProduct(Integer.parseInt(id)));


            }
        });
    }



}
