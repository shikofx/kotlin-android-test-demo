package by.pda.demoapp.android.database;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import by.pda.demoapp.android.model.ProductModel;
import by.pda.demoapp.android.model.User;

import java.util.List;

@Dao
public interface AppDao {
    @Query("SELECT * FROM person ORDER BY ID")
    List<User> loadAllPersons();

    @Insert
    void insert(User user);

    @Update
    void update(User user);

    @Delete
    void delete(User user);

    @Query("SELECT * FROM person WHERE id = :id")
    User loadPersonById(int id);

    @Query("SELECT * FROM Product ORDER BY ID")
    LiveData<List<ProductModel>> getAllProducts();

    @Query("SELECT * FROM Product ORDER BY price ASC")
    LiveData<List<ProductModel>> getProductsSortByAscPrice();

    @Query("SELECT * FROM Product ORDER BY price DESC")
    LiveData<List<ProductModel>> getProductsSortByDescPrice();

    @Query("SELECT * FROM Product ORDER BY title ASC")
    LiveData<List<ProductModel>> getProductsSortByAscName();

    @Query("SELECT * FROM Product ORDER BY title DESC")
    LiveData<List<ProductModel>> getProductsSortByDescName();


    @Insert
    void insertProducts(ProductModel product);

    @Insert
    void insertProducts(List<ProductModel> products);

    @Query("SELECT * FROM Product WHERE id = :id")
    ProductModel getProduct(int id);

}
