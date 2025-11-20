package by.pda.demoapp.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import by.pda.demoapp.android.database.AppDao
import by.pda.demoapp.android.database.AppDatabase
import by.pda.demoapp.android.model.ProductModel
import by.pda.demoapp.core.annotations.DatabaseTest
import by.pda.demoapp.core.utils.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import io.qameta.allure.Feature
import io.qameta.allure.kotlin.Allure.step
import io.qameta.allure.Story
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DatabaseTest
@Feature("AppDao Tests")
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
        step("Given a product is prepared") {
            val product = ProductModel().apply {
                id = 1
                title = "product"
                price = 2.0
            }

            step("When the product is inserted") {
                appDao.insertProducts(product)
            }

            step("Then the product can be read from the database") {
                val products = appDao.allProducts.getOrAwaitValue()
                val resultProduct = products?.first()

                assertThat(products).isNotEmpty()
                assertThat(products).hasSize(1)
                assertThat(resultProduct).isEqualTo(product)
            }
        }
    }

    @Test
    @Story("Query Operations - Sorting")
    @DisplayName("Should return products sorted by title ascending")
    fun getProducts_shouldReturnSortedByTitleAsc() {
        // Given (DAO-3)
        step("Given a list of unsorted products is inserted") {
            val productC = ProductModel().apply { id = 1; title = "C"; price = 1.0 }
            val productA = ProductModel().apply { id = 2; title = "A"; price = 1.0 }
            val productB = ProductModel().apply { id = 3; title = "B"; price = 1.0 }
            appDao.insertProducts(mutableListOf(productA, productB, productC))
        }

        step("When products are queried with ascending name sort") {
            val products = appDao.getProductsSortByAscName().getOrAwaitValue()

            step("Then the products are returned in ascending order by title") {
                assertThat(products).hasSize(3)
                assertThat(products.map { it.title }).containsExactly("A", "B", "C").inOrder()
            }
        }
    }

    @Test
    @Story("Query Operations - Sorting")
    @DisplayName("Should return products sorted by title descending")
    fun getProducts_shouldReturnSortedByTitleDesc() {
        // Given (DAO-4)
        step("Given a list of unsorted products is inserted") {
            val productC = ProductModel().apply { id = 1; title = "C"; price = 1.0 }
            val productA = ProductModel().apply { id = 2; title = "A"; price = 1.0 }
            val productB = ProductModel().apply { id = 3; title = "B"; price = 1.0 }
            appDao.insertProducts(mutableListOf(productA, productB, productC))
        }

        step("When products are queried with descending name sort") {
            val products = appDao.getProductsSortByDescName().getOrAwaitValue()

            step("Then the products are returned in descending order by title") {
                assertThat(products).hasSize(3)
                assertThat(products.map { it.title }).containsExactly("C", "B", "A").inOrder()
            }
        }
    }

    @Test
    @Story("Edge Cases - Empty State")
    @DisplayName("Should return empty list from empty database")
    fun readFromEmptyDatabase_shouldReturnEmptyList() {
        // Given (DAO-5)
        step("Given the database is empty") {
            // No action needed
        }

        step("When all products are queried") {
            val products = appDao.allProducts.getOrAwaitValue()
            assertThat(products).isNotNull()
            assertThat(products).isEmpty()
        }
    }

    @Test
    @Story("CRUD Operations - Read by ID")
    @DisplayName("Should return correct product by its ID")
    fun getProductById_shouldReturnCorrectProduct() {
        // Given (DAO-6)
        step("Given multiple products are inserted") {
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

            step("When a product is queried by a specific ID") {
                val resultProduct = appDao.getProduct(42)

                step("Then the correct product is returned") {
                    assertThat(resultProduct).isNotNull()
                    assertThat(resultProduct).isEqualTo(productToInsert)
                }
            }
        }
    }
}