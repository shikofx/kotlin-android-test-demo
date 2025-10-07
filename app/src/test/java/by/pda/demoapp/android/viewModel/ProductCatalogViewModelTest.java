package by.pda.demoapp.android.viewModel;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import by.pda.demoapp.android.database.AppDao;
import by.pda.demoapp.android.database.AppExecutors;
import by.pda.demoapp.android.model.ProductModel;
import by.pda.demoapp.android.utils.InstantExecutorExtension;
import by.pda.demoapp.android.utils.SingletonClass;
import by.pda.demoapp.android.view.activities.MainActivity;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;

import static org.mockito.Mockito.when;

@Epic("Business Logic (ViewModels)")
@Feature("Product Catalog")
@Owner("D.Parkheychuk")
@DisplayName("ProductCatalogViewModel Unit Tests")
@ExtendWith({MockitoExtension.class, InstantExecutorExtension.class})
class ProductCatalogViewModelTest {

    private ProductCatalogViewModel viewModel;

    @Mock
    private AppDao mockAppDao;

    @Mock
    private SingletonClass mockSingletonClass;

    @BeforeEach
    void setUp() {
        // For unit tests, we use a special executor that runs tasks immediately on the same thread.
        AppExecutors testExecutors = new AppExecutors(Runnable::run, Runnable::run, Runnable::run);

        // Create the ViewModel with mocked dependencies
        // We will mock the static getInstance() method inside each test where it's needed.
        viewModel = new ProductCatalogViewModel(mockAppDao, testExecutors, mockSingletonClass);
    }

    private static List<ProductModel> createTestProductList() {
        List<ProductModel> productList = new ArrayList<>();

        ProductModel p1 = new ProductModel();
        p1.setTitle("Sauce Labs Backpack");
        p1.setPrice(29.99);
        p1.setImageVal(1001);
        productList.add(p1);

        ProductModel p2 = new ProductModel();
        p2.setTitle("Sauce Labs Bike Light");
        p2.setPrice(9.99);
        p2.setImageVal(1002);
        productList.add(p2);

        ProductModel p3 = new ProductModel();
        p3.setTitle("Sauce Labs Onesie");
        p3.setPrice(7.99);
        p3.setImageVal(1003);
        productList.add(p3);

        return productList;
    }

    @Nested
    @DisplayName("getAllProducts tests")
    @Story("Loading product list")
    class GetAllProductsTest {

        @Test
        @DisplayName("should load products and post them to LiveData when visual changes are off")
        @Description("Verify the happy path: ViewModel requests data from DAO, visual changes flag is off, and data is correctly posted to LiveData.")
        @Severity(SeverityLevel.CRITICAL)
        void getAllProducts_whenChangesOff_loadsAndPostsList() {
            // Arrange
            final List<ProductModel> testProducts = createTestProductList();
            Allure.step("Step 1: Setup mocks", () -> {
                when(mockAppDao.getPersonsSortByAscName()).thenReturn(testProducts);
                when(mockSingletonClass.getHasVisualChanges()).thenReturn(false);
            });

            // Act
            Allure.step("Step 2: Call getAllProducts method", () -> viewModel.getAllProducts(MainActivity.NAME_ASC));

            // Assert
            Allure.step("Step 3: Verify LiveData content", () -> {
                List<ProductModel> postedValue = viewModel.allProducts.getValue();
                assertThat(postedValue).isNotNull();
                assertThat(postedValue).isSameInstanceAs(testProducts);
                assertThat(postedValue).hasSize(3);
            });
        }

        @Test
        @DisplayName("should load products and apply visual changes when flag is on")
        @Description("Verify that if the visual changes flag is on, the ViewModel applies these changes to the product list before posting to LiveData.")
        @Severity(SeverityLevel.NORMAL)
        void getAllProducts_whenChangesOn_loadsAndAppliesChanges() {
            // Arrange
            final List<ProductModel> testProducts = createTestProductList();
            final double originalPrice = testProducts.get(0).getPrice();
            Allure.step("Step 1: Setup mocks with visual changes flag enabled", () -> {
                when(mockAppDao.getPersonsSortByAscName()).thenReturn(testProducts);
                when(mockSingletonClass.getHasVisualChanges()).thenReturn(true);
            });

            // Act
            Allure.step("Step 2: Call getAllProducts method", () -> viewModel.getAllProducts(MainActivity.NAME_ASC));

            // Assert
            Allure.step("Step 3: Verify that changes were applied to the list", () -> {
                List<ProductModel> postedValue = viewModel.allProducts.getValue();
                assertThat(postedValue).isNotNull();
                assertThat(postedValue).hasSize(3);
                // Check that the price has been changed by generateVisualChanges
                assertThat(postedValue.get(0).getPrice()).isNotEqualTo(originalPrice);
            });
        }
    }

    @Nested
    @DisplayName("findProductByName tests")
    @Story("Search product by name")
    class FindProductByNameTest {

        @ParameterizedTest(name = "[{index}] {0}")
        @MethodSource("findProductByNameDataSource")
        @DisplayName("should return correct result for various inputs")
        @Severity(SeverityLevel.NORMAL)
        void findProductByName_variousScenarios_returnsCorrectResult(String description, List<ProductModel> list, String name, ProductModel expected) {//FPN-1
            // Act
            ProductModel actual = viewModel.findProductByName(list, name);

            // Assert
            // For objects, it's better to compare them directly if `equals` is properly implemented.
            // If not, we can compare key fields. Here, we assume we can compare objects.
            assertThat(actual).isEqualTo(expected);
        }

        private static Stream<Arguments> findProductByNameDataSource() {
            List<ProductModel> productList = createTestProductList();
            ProductModel expectedProduct = productList.get(1);

            return Stream.of(
                    // FPN-1
                    Arguments.of("Happy Path (product exists)", productList, "Sauce Labs Bike Light", expectedProduct),
                    // FPN-2
                    Arguments.of("Negative (product does not exist)", productList, "Non-existent Product", null),
                    // FPN-3
                    Arguments.of("Boundary (empty list)", Collections.emptyList(), "any name", null),
                    // FPN-5
                    Arguments.of("Negative (null name)", productList, null, null)
            );
        }

        // FPN-4
        @Test
        @DisplayName("should throw NullPointerException for null list")
        @Severity(SeverityLevel.MINOR)
        void findProductByName_whenListIsNull_throwsNPE() {
            assertThrows(NullPointerException.class, () -> {
                // Act
                viewModel.findProductByName(null, "any name");
            });
        }
    }

    @Nested
    @DisplayName("generateVisualChanges tests")
    @Story("Applying visual changes")
    class GenerateVisualChangesTest {

        // GVC-1
        @Test
        @DisplayName("should apply visual changes to a standard list")
        @Severity(SeverityLevel.NORMAL)
        void generateVisualChanges_whenStandardList_appliesChanges() {
            // Arrange - Create a deep copy for comparison
            List<ProductModel> productList = createTestProductList();
            final double originalPrice1 = productList.get(0).getPrice();
            final double originalPrice2 = productList.get(1).getPrice();
            int onesieImageVal = viewModel.findProductByName(productList, "Sauce Labs Onesie").getImageVal();

            // Act
            List<ProductModel> changedList = viewModel.generateVisualChanges(productList);

            // Assert
            assertThat(changedList).isSameInstanceAs(productList); // The method modifies the list in-place
            assertThat(changedList.get(0).getPrice()).isNotEqualTo(originalPrice1);
            assertThat(changedList.get(1).getPrice()).isNotEqualTo(originalPrice2);

            assertThat(changedList.get(0).getImageVal()).isEqualTo(onesieImageVal);
            assertThat(changedList.get(1).getImageVal()).isEqualTo(onesieImageVal);
        }

        // GVC-2
        @Test
        @DisplayName("should only change price for list with less than 2 items")
        @Severity(SeverityLevel.MINOR)
        void generateVisualChanges_whenListIsSmall_changesOnlyPrice() {
            // Arrange
            List<ProductModel> singleItemList = new ArrayList<>();
            ProductModel p1 = new ProductModel();
            p1.setTitle("Sauce Labs Onesie");
            p1.setPrice(29.99);
            p1.setImageVal(1001);
            singleItemList.add(p1);

            double originalPrice = p1.getPrice();
            int originalImageVal = p1.getImageVal();

            // Act
            List<ProductModel> changedList = viewModel.generateVisualChanges(singleItemList);

            // Assert
            assertThat(changedList).hasSize(1);
            assertThat(changedList.get(0).getPrice()).isNotEqualTo(originalPrice);
            assertThat(changedList.get(0).getImageVal()).isEqualTo(originalImageVal); // Image should not change
        }

        // GVC-3
        @Test
        @DisplayName("should return an empty list without errors")
        @Severity(SeverityLevel.MINOR)
        void generateVisualChanges_whenListIsEmpty_returnsEmptyList() {
            assertThat(viewModel.generateVisualChanges(new ArrayList<>())).isEmpty();
        }

        // GVC-4
        @Test
        @DisplayName("should throw NullPointerException for null list")
        @Severity(SeverityLevel.MINOR)
        void generateVisualChanges_whenListIsNull_throwsNPE() {
            assertThrows(NullPointerException.class, () -> {
                viewModel.generateVisualChanges(null);
            });
        }
    }
}
