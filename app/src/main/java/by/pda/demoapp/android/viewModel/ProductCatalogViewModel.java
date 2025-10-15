package by.pda.demoapp.android.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

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
    private final SingletonClass singleton;

    private final MutableLiveData<Integer> sortType = new MutableLiveData<>();
    private final LiveData<List<ProductModel>> products;

    public ProductCatalogViewModel(AppDao appDao, AppExecutors appExecutors, SingletonClass singletonClass) {
        this.appDao = appDao;
        this.appExecutors = appExecutors;
        this.singleton = singletonClass;

        products = Transformations.switchMap(sortType, type -> {
            LiveData<List<ProductModel>> source = switch (type) {
                case MainActivity.NAME_DESC -> appDao.getProductsSortByDescName();
                case MainActivity.PRICE_ASC -> appDao.getProductsSortByAscPrice();
                case MainActivity.PRICE_DESC -> appDao.getProductsSortByDescPrice();
                default -> appDao.getProductsSortByAscName();
            };

            if (singleton.getHasVisualChanges()) {
                return Transformations.map(source, this::generateVisualChanges);
            }
            return source;
        });
    }

    public LiveData<List<ProductModel>> getProducts() {
        return products;
    }

    public void setSortType(int type) {
        sortType.setValue(type);
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
