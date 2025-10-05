package by.pda.demoapp.android.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.offset;

import static by.pda.demoapp.android.utils.MethodsTest.GetTotalPriceTest.createCartItem;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import by.pda.demoapp.android.model.CartItemModel;
import by.pda.demoapp.android.model.ProductModel;

class MethodsTest {
    private Methods methods;

    @BeforeEach
    void setUp() {
        //Arrange
        methods = new Methods();
    }

    @Nested
    @DisplayName("isValidPassword tests")
    class IsValidPasswordTest {
        @ParameterizedTest
        @ValueSource(strings = {"123456", "1234567", "01234567890123456789"})
        @DisplayName("should return true for valid passwords")
        void isValidPassword_whenPasswordIsValid_returnsTrue(String password) {
            //Act
            boolean actual = methods.isValidPassword(password);

            //Assert
            assertThat(actual).isTrue();
        }

        @ParameterizedTest
        @ValueSource(strings = {"1", "12345"})
        @NullAndEmptySource
        @DisplayName("should return false for invalid passwords")
        void isValidPassword_whenPasswordIsInvalid_returnsFalse(String password) {
            //Act
            boolean actual = methods.isValidPassword(password);

            //Assert
            assertThat(actual).isFalse();
        }
    }

    @Nested
    @DisplayName("getTotalPrice tests")
    class GetTotalPriceTest {

        @ParameterizedTest(name = "totalPrice should be equal to {2}")
        @MethodSource("totalPriceDataSource")
        void getTotalPrice_whenCalledWithCart_returnsCorrectTotal(List<CartItemModel> cart, double expectedPrice) {
            // Act
            double actualPrice = methods.getTotalPrice(cart);

            // Assert
            assertThat(actualPrice).isCloseTo(expectedPrice, offset(0.01));
        }

        @Test
        @DisplayName("should throw NullPointerException when cart list is null")
        void getTotalPrice_whenCartIsNull_throwsNPE() {
            //TODO: Need to fix logic in order to avoid NPE
            assertThatThrownBy(() -> {
                // Act
                methods.getTotalPrice(null);
            })
            .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("should throw NullPointerException when a product model in cart is null")
        void getTotalPrice_whenProductModelIsNull_throwsNPE() {
            // Arrange
            //TODO: Need to fix logic in order to avoid NPE
            CartItemModel badItem = new CartItemModel();
            badItem.setProductModel(null);
            badItem.setNumberOfProduct(1);
            List<CartItemModel> cart = Collections.singletonList(badItem);

            // Assert
            assertThatThrownBy(() -> {
                // Act
                methods.getTotalPrice(cart);
            })
            .isInstanceOf(NullPointerException.class);
        }

        /**
         * This is the data provider method for the parameterized test.
         */
        private static Stream<Arguments> totalPriceDataSource() {
            // Scenario 1: Standard cart (GTP-1)
            List<CartItemModel> standardCart = new ArrayList<>();
            standardCart.add(createCartItem(10.0, 2)); // 20.0
            standardCart.add(createCartItem(25.5, 1)); // 25.5

            // Scenario 2: Empty cart (GTP-2)
            List<CartItemModel> emptyCart = Collections.emptyList();

            // Scenario 3: Single item cart (GTP-3)
            List<CartItemModel> singleItemCart = Collections.singletonList(createCartItem(15.99, 3));

            // Scenario 4: Item with zero price (GTP-4)
            List<CartItemModel> zeroPriceCart = new ArrayList<>();
            zeroPriceCart.add(createCartItem(10.0, 2));
            zeroPriceCart.add(createCartItem(0.0, 5));

            // Scenario 5: Item with zero quantity (GTP-5)
            List<CartItemModel> zeroQuantityCart = new ArrayList<>();
            zeroQuantityCart.add(createCartItem(10.0, 2));
            zeroQuantityCart.add(createCartItem(50.0, 0));

            return Stream.of(
                    Arguments.of(standardCart, 45.5),
                    Arguments.of(emptyCart, 0.0),
                    Arguments.of(singleItemCart, 47.97),
                    Arguments.of(zeroPriceCart, 20.0),
                    Arguments.of(zeroQuantityCart, 20.0)
            );
        }

        /**
         * Helper method to create a CartItemModel with a dummy ProductModel.
         */
        static CartItemModel createCartItem(double price, int quantity) {
            ProductModel product = new ProductModel();
            product.setPrice(price);

            CartItemModel cartItem = new CartItemModel();
            cartItem.setProductModel(product);
            cartItem.setNumberOfProduct(quantity);
            return cartItem;
        }
    }

    @Nested
    @DisplayName("isEqual tests")
    class IsEqualTest {

        @ParameterizedTest(name = "isEqual(''{0}'', ''{1}'') should be {2}")
        @CsvSource({
                "hello,    hello,   true",
                "'',       '',      true",
                "hello,    world,   false",
                "Hello,    hello,   false",
                "hello,    '',      false",
                "hello,    NULL,    false",
                "NULL,     hello,   false",
                "NULL,     NULL,    true" //mistake for example of failed test
        })
        void isEqual_variousStringPairs_returnsCorrectResult(String str1, String str2, boolean expected) {
            boolean actual = methods.isEqual(str1, str2);
            assertThat(actual).isEqualTo(expected);
        }
    }

    @Nested
    @DisplayName("getTotalNum tests")
    class GetTotalNumTest {

        // This will run after each test in this nested class
        @AfterEach
        void tearDown() {
            // Clean up the global state to ensure test independence
            SingletonClass.getInstance().cartItemList.clear();
        }

        @Test
        @DisplayName("should return 0 for an empty cart")
        void getTotalNum_whenCartIsEmpty_returnsZero() {
            // Arrange: The cart is already empty due to tearDown() or initial state

            // Act
            int total = methods.getTotalNum();

            // Assert
            assertThat(total).isZero();
        }

        @Test
        @DisplayName("should return the correct sum of item quantities")
        void getTotalNum_whenCartHasItems_returnsCorrectSum() {
            // Arrange: Add items to the global singleton cart
            SingletonClass.getInstance().cartItemList.add(createCartItem(10.0, 2));
            SingletonClass.getInstance().cartItemList.add(createCartItem(20.0, 3));

            // Act
            int total = methods.getTotalNum();

            // Assert
            assertThat(total).isEqualTo(5);
        }
    }
}