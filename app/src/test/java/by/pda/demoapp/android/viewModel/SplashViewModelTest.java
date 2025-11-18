package by.pda.demoapp.android.viewModel;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static com.google.common.truth.Truth.assertThat;

import android.view.View;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import by.pda.demoapp.android.database.AppDao;
import by.pda.demoapp.android.database.AppExecutors;
import by.pda.demoapp.android.model.ProductModel;
import by.pda.demoapp.core.extensions.InstantExecutorExtension;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;

@Epic("Business Logic (ViewModels)")
@Feature("Splash Screen")
@Owner("D.Parkheychuk")
@Tag("unitTests")
@DisplayName("SplashViewModel Unit Tests")
@ExtendWith({MockitoExtension.class, InstantExecutorExtension.class})
class SplashViewModelTest {

    @Mock
    private AppDao mockAppDao;

    private AppExecutors testExecutors;
    private SplashViewModel viewModel;

    @BeforeEach
    void setUp() {
        // Use a synchronous executor for test predictability
        testExecutors = new AppExecutors(Runnable::run, Runnable::run, Runnable::run);
    }

    @Nested
    @DisplayName("Initialization Tests")
    @Story("Initialization and data loading checks")
    class Initialization {

        @Test
        @DisplayName("should fetch all products from DAO on init")
        @Severity(SeverityLevel.CRITICAL)
        void onInit_shouldFetchAllProducts() {
            // Given
            final List<ProductModel> testProducts = new ArrayList<>();
            // Create a LiveData object to hold the test data
            final MutableLiveData<List<ProductModel>> liveData = new MutableLiveData<>();
            liveData.setValue(testProducts);

            // Mock the DAO to return the LiveData object
            when(mockAppDao.getAllProducts()).thenReturn(liveData);

            // When: ViewModel is created here, and the constructor calls getAllProducts()
            viewModel = new SplashViewModel(mockAppDao, testExecutors);

            // Then
            verify(mockAppDao, times(1)).getAllProducts(); // Verify that the method was called
            // Verify that the LiveData inside the ViewModel holds the correct data
            assertThat(viewModel.getAllProducts().getValue()).isSameInstanceAs(testProducts);
        }
    }

    @Nested
    @DisplayName("Data Insertion Tests")
    @Story("Inserting products into the database")
    class DataInsertion {
        @Test
        @DisplayName("should call insertProducts in DAO and hide progress bar")
        @Severity(SeverityLevel.NORMAL)
        void insertProducts_shouldCallDaoAndHideProgressBar() {
            // Given
            viewModel = new SplashViewModel(mockAppDao, testExecutors);
            List<ProductModel> productsToInsert = new ArrayList<>();

            // When
            viewModel.insertProducts(productsToInsert);

            // Then
            verify(mockAppDao, times(1)).insertProducts(productsToInsert); // Verify that DAO was called with the correct list
            assertThat(viewModel.getProgressBarState().getValue()).isEqualTo(View.GONE); // Verify that the progress bar is hidden
        }
    }
}