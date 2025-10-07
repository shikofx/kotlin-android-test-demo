package by.pda.demoapp.android.viewModel;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import by.pda.demoapp.android.database.AppDao;
import by.pda.demoapp.android.database.AppExecutors;
import by.pda.demoapp.android.model.ProductModel;
import by.pda.demoapp.android.utils.InstantExecutorExtension;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;

@Epic("Business Logic (ViewModels)")
@Feature("Product Detail Screen")
@Owner("D.Parkheychuk")
@DisplayName("ProductDetailViewModel Unit Tests")
@ExtendWith({MockitoExtension.class, InstantExecutorExtension.class})
class ProductDetailViewModelTest {

    @Mock
    private AppDao mockAppDao;

    @Captor
    private ArgumentCaptor<Integer> idCaptor;

    private AppExecutors testExecutors;

    @BeforeEach
    void setUp() {
        // Use a synchronous executor for test predictability
        testExecutors = new AppExecutors(Runnable::run, Runnable::run, Runnable::run);
    }

    @Nested
    @DisplayName("Product Loading Tests")
    @Story("Fetching a single product by ID")
    class ProductLoading {

        @Test
        @DisplayName("should fetch product with correct ID on init")
        @Severity(SeverityLevel.CRITICAL)
        void onInit_shouldFetchProductWithCorrectId() {
            // Arrange
            String productId = "123";
            ProductModel testProduct = new ProductModel();
            testProduct.setId(Integer.parseInt(productId));
            testProduct.setTitle("Test Product");

            when(mockAppDao.getProduct(Integer.parseInt(productId))).thenReturn(testProduct);

            // Act: ViewModel is created here, and the constructor calls getProduct()
            ProductDetailViewModel viewModel = new ProductDetailViewModel(mockAppDao, testExecutors, productId);

            // Assert
            // Verify that the DAO method was called and capture the argument
            verify(mockAppDao).getProduct(idCaptor.capture());
            assertThat(idCaptor.getValue()).isEqualTo(123);

            // Verify that the LiveData was updated with the correct product
            assertThat(viewModel.product.getValue()).isSameInstanceAs(testProduct);
        }
    }
}