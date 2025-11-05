package by.pda.demoapp.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import by.pda.demoapp.android.database.AppDao
import by.pda.demoapp.android.database.AppDatabase
import by.pda.demoapp.android.model.ProductModel
import by.pda.demoapp.core.extensions.InstantExecutionExtension
import by.pda.demoapp.core.utils.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import io.qameta.allure.Epic
import io.qameta.allure.Feature
import io.qameta.allure.Owner
import io.qameta.allure.Story
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@Epic("Data Layer")
@Feature("Room Database (Instrumented)")
@Owner("D.Parkheychuk")
@DisplayName("DAO Instrumented Tests")
@ExtendWith(InstantExecutionExtension::class)
class AppDaoTest {
    private lateinit var database: AppDatabase
    private lateinit var appDao: AppDao

    @BeforeEach
    fun createDatabase() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        appDao = database.appDao()
    }

    @AfterEach
    fun closeDatabase() {
        database.close()
    }

    @Test
    @Story("CRUD Operations - Create & Read")
    @DisplayName("Insert and read product")
    fun insertAndReadProduct_shouldInsertProducts() {
        // Given
        val product = ProductModel().apply {
            id = 1
            title = "product"
            price = 2.0
        }

        // When
        appDao.insertProducts(product)

        // Then
        val products = appDao.allProducts.getOrAwaitValue()
        val resultProduct = products?.first()

        assertThat(products).isNotEmpty()
        assertThat(products).hasSize(1)
        assertThat(resultProduct).isEqualTo(product)
    }

    @Test
    @Story("Query Operations - Sorting")
    @DisplayName("Should return products sorted by title ascending")
    fun getProducts_shouldReturnSortedByTitleAsc() {
        // Given (DAO-3)
        val productC = ProductModel().apply { id = 1; title = "C"; price = 1.0 }
        val productA = ProductModel().apply { id = 2; title = "A"; price = 1.0 }
        val productB = ProductModel().apply { id = 3; title = "B"; price = 1.0 }
        appDao.insertProducts(mutableListOf(productA, productB, productC))

        // When
        // Предполагается, что в AppDao есть метод getProductsSortedByTitleAsc()
        val products = appDao.getProductsSortByAscName().getOrAwaitValue()

        // Then
        assertThat(products).hasSize(3)
        assertThat(products.map { it.title }).containsExactly("A", "B", "C").inOrder()
    }

    @Test
    @Story("Query Operations - Sorting")
    @DisplayName("Should return products sorted by title descending")
    fun getProducts_shouldReturnSortedByTitleDesc() {
        // Given (DAO-4)
        val productC = ProductModel().apply { id = 1; title = "C"; price = 1.0 }
        val productA = ProductModel().apply { id = 2; title = "A"; price = 1.0 }
        val productB = ProductModel().apply { id = 3; title = "B"; price = 1.0 }
        appDao.insertProducts(mutableListOf(productA, productB, productC))

        // When
        // Предполагается, что в AppDao есть метод getProductsSortedByTitleDesc()
        val products = appDao.getProductsSortByDescName().getOrAwaitValue()

        // Then
        assertThat(products).hasSize(3)
        assertThat(products.map { it.title }).containsExactly("C", "B", "A").inOrder()
    }

    @Test
    @Story("Edge Cases - Empty State")
    @DisplayName("Should return empty list from empty database")
    fun readFromEmptyDatabase_shouldReturnEmptyList() {
        // Given (DAO-5)
        // База данных пуста

        // When
        val products = appDao.allProducts.getOrAwaitValue()

        // Then
        assertThat(products).isNotNull()
        assertThat(products).isEmpty()
    }

    @Test
    @Story("CRUD Operations - Read by ID")
    @DisplayName("Should return correct product by its ID")
    fun getProductById_shouldReturnCorrectProduct() {
        // Given (DAO-6)
        val productToInsert = ProductModel().apply {
            id = 42
            title = "Specific Product"
            price = 99.9
        }
        val otherProduct = ProductModel().apply {
            id = 100
            title = "Another Product"
            price = 10.0
        }
        appDao.insertProducts(mutableListOf(productToInsert, otherProduct))

        // When
        // Предполагается, что в AppDao есть метод getProduct(id)
        val resultProduct = appDao.getProduct(42)

        // Then
        assertThat(resultProduct).isNotNull()
        assertThat(resultProduct).isEqualTo(productToInsert)
    }
}