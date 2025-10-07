package by.pda.demoapp.android.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.Random;

import by.pda.demoapp.android.database.AppDao;
import by.pda.demoapp.android.database.AppExecutors;
import by.pda.demoapp.android.model.ProductModel;
import by.pda.demoapp.android.utils.SingletonClass;
import by.pda.demoapp.android.utils.base.BaseViewModel;
import by.pda.demoapp.android.view.activities.MainActivity;

public class ProductCatalogViewModel extends BaseViewModel {
    private static final int MAX_RANDOM_PRICE = 100;
    private static final String ONESIE_PRODUCT_NAME = "Sauce Labs Onesie";
    private final AppDao appDao;
    private final AppExecutors appExecutors;
    private final SingletonClass singletonClass;
    private final MutableLiveData<List<ProductModel>> _allProducts = new MutableLiveData<>();

    public ProductCatalogViewModel(AppDao appDao, AppExecutors appExecutors, SingletonClass singletonClass) {
        this.appDao = appDao;
        this.appExecutors = appExecutors;
        this.singletonClass = singletonClass;
    }

    public LiveData<List<ProductModel>> getAllProductsLiveData() {
        return _allProducts;
    }

    public void getAllProducts(int type) {
        appExecutors.diskIO().execute(() -> {
            List<ProductModel> productList;
            switch (type) {
                case MainActivity.NAME_DESC:
                    productList = appDao.getPersonsSortByDescName();
                    break;
                case MainActivity.PRICE_ASC:
                    productList = appDao.getPersonsSortByAscPrice();
                    break;
                case MainActivity.PRICE_DESC:
                    productList = appDao.getPersonsSortByDescPrice();
                    break;
                case MainActivity.NAME_ASC:
                default:
                    productList = appDao.getPersonsSortByAscName();
                    break;
            }

            // Alter prices if needed
            if (singletonClass.getHasVisualChanges()) {
                productList = generateVisualChanges(productList);
            }
            _allProducts.postValue(productList);
        });
    }

    public List<ProductModel> generateVisualChanges(List<ProductModel> productList) {
        final Random random = new Random();

        // Replaces prices by Random ones
        for (int i = 0; i < productList.size(); i++) {
            double randomPrice = 1 + (MAX_RANDOM_PRICE - 1) * random.nextDouble();
            randomPrice = (double) Math.round(randomPrice * 100) / 100;
            productList.get(i).setPrice(randomPrice);
        }

        // Replace 2 first item by Onesie image.
        if(!productList.isEmpty()) {
            ProductModel onesie = findProductByName(productList, ONESIE_PRODUCT_NAME);
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
