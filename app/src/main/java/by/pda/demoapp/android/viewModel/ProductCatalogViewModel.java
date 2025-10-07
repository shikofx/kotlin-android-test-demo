package by.pda.demoapp.android.viewModel;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import by.pda.demoapp.android.database.AppDao;
import by.pda.demoapp.android.database.AppExecutors;
import by.pda.demoapp.android.model.ProductModel;
import by.pda.demoapp.android.utils.SingletonClass;
import by.pda.demoapp.android.utils.base.BaseViewModel;
import by.pda.demoapp.android.view.activities.MainActivity;

public class ProductCatalogViewModel extends BaseViewModel {
    private final AppDao appDao;
    private final AppExecutors appExecutors;
    private final SingletonClass singletonClass;
    public MutableLiveData<List<ProductModel>> allProducts = new MutableLiveData<>();

    public ProductCatalogViewModel(AppDao appDao, AppExecutors appExecutors, SingletonClass singletonClass) {
        this.appDao = appDao;
        this.appExecutors = appExecutors;
        this.singletonClass = singletonClass;
    }

    public void getAllProducts(int type) {
        appExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
//                allProducts.postValue(mDb.personDao().getAllProducts());

                List<ProductModel> productList = new ArrayList<>();
                if (MainActivity.selectedSort == MainActivity.NAME_ASC) {
                    productList = appDao.getPersonsSortByAscName();
                } else if (MainActivity.selectedSort == MainActivity.NAME_DESC) {
                    productList = appDao.getPersonsSortByDescName();
                } else if (MainActivity.selectedSort == MainActivity.PRICE_ASC) {
                    productList = appDao.getPersonsSortByAscPrice();
                } else if (MainActivity.selectedSort == MainActivity.PRICE_DESC) {
                    productList = appDao.getPersonsSortByDescPrice();
                }

                // Alter prices if needed
                if (singletonClass.getHasVisualChanges()) {
                    productList = generateVisualChanges(productList);
                }
                allProducts.postValue(productList);
            }
        });
    }

    public List<ProductModel> generateVisualChanges(List<ProductModel> productList) {
        Random random = new Random();

        // Replaces prices by Random ones
        for (int i = 0; i < productList.size(); i++) {
            double randomPrice = 1 + (100 - 1) * random.nextDouble();
            randomPrice = (double) Math.round(randomPrice * 100) / 100;
            productList.get(i).setPrice(randomPrice);
        }

        // Replace 2 first item by Onesie image.
        if(!productList.isEmpty()) {
            ProductModel onesie = findProductByName(productList, "Sauce Labs Onesie");
            productList.get(0).setImage(onesie.getImage());
            productList.get(0).setImageVal(onesie.getImageVal());
            if(productList.size() > 1) {
                productList.get(1).setImage(onesie.getImage());
                productList.get(1).setImageVal(onesie.getImageVal());
            }
        }
        return productList;
    }

    public ProductModel findProductByName(List<ProductModel> productList, String name) {
        for (ProductModel product: productList) {
            if (product.getTitle().equals(name)) {
                return product;
            }
        }
        return null;
    }
}
