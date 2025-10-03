package by.pda.demoapp.android.viewModel;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import by.pda.demoapp.android.model.ProductModel;

class ProductCatalogViewModelTest {

    private ProductCatalogViewModel viewModel;

    @BeforeEach
    void setUp() {
        // The constructor requires an Application, but for these tests,
        // we can pass null as we are not testing database interactions.
        viewModel = new ProductCatalogViewModel(null);
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
    @DisplayName("findProductByName tests")
    class FindProductByNameTest {

        @ParameterizedTest(name = "[{index}] {0}")
        @MethodSource("findProductByNameDataSource")
        @DisplayName("should return correct result for various inputs")
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
        void findProductByName_whenListIsNull_throwsNPE() {
            // Assert
            assertThatThrownBy(() -> viewModel.findProductByName(null, "any name"))
                    .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("generateVisualChanges tests")
    class GenerateVisualChangesTest {

        // GVC-1
        @Test
        @DisplayName("should apply visual changes to a standard list")
        void generateVisualChanges_whenStandardList_appliesChanges() {
            // Arrange - Create a deep copy for comparison
            List<ProductModel> productList = createTestProductList();
            final double originalPrice1 = productList.get(0).getPrice();
            final double originalPrice2 = productList.get(1).getPrice();
            int onesieImageVal = viewModel.findProductByName(productList, "Sauce Labs Onesie").getImageVal();

            // Act
            List<ProductModel> changedList = viewModel.generateVisualChanges(productList);

            // Assert
            assertThat(changedList).isSameAs(productList); // The method modifies the list in-place
            assertThat(changedList.get(0).getPrice()).isNotEqualTo(originalPrice1);
            assertThat(changedList.get(1).getPrice()).isNotEqualTo(originalPrice2);

            assertThat(changedList.get(0).getImageVal()).isEqualTo(onesieImageVal);
            assertThat(changedList.get(1).getImageVal()).isEqualTo(onesieImageVal);
        }

        // GVC-2
        @Test
        @DisplayName("should only change price for list with less than 2 items")
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
        void generateVisualChanges_whenListIsEmpty_returnsEmptyList() {
            assertThat(viewModel.generateVisualChanges(new ArrayList<>())).isEmpty();
        }

        // GVC-4
        @Test
        @DisplayName("should throw NullPointerException for null list")
        void generateVisualChanges_whenListIsNull_throwsNPE() {
            assertThatThrownBy(() -> viewModel.generateVisualChanges(null))
                    .isInstanceOf(NullPointerException.class);
        }
    }
}
