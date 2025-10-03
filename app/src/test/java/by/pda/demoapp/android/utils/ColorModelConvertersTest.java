package by.pda.demoapp.android.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.google.gson.JsonSyntaxException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import by.pda.demoapp.android.model.ColorModel;
import by.pda.demoapp.android.model.ColorModelConverters;

class ColorModelConvertersTest {

    @Nested
    @DisplayName("someObjectListToString tests (Serialization)")
    class SomeObjectListToStringTest {

        @ParameterizedTest(name = "should correctly serialize {0}")
        @MethodSource("listToStringDataSource")
        void someObjectListToString_whenGivenList_returnsCorrectJson(String description, List<ColorModel> colors, String expectedJson) {
            // Act
            String actualJson = ColorModelConverters.someObjectListToString(colors);

            // Assert
            assertThat(actualJson).isEqualTo(expectedJson);
        }

        private static Stream<Arguments> listToStringDataSource() {
            // CMCS-1: Standard list
            List<ColorModel> standardList = new ArrayList<>();
            standardList.add(new ColorModel(1, 10));
            standardList.add(new ColorModel(2, 20));

            // CMCS-2: Empty list
            List<ColorModel> emptyList = new ArrayList<>();

            return Stream.of(
                    Arguments.of("a standard list", standardList, "[{\"colorImg\":1,\"colorValue\":10},{\"colorImg\":2,\"colorValue\":20}]"),
                    Arguments.of("an empty list", emptyList, "[]"),
                    Arguments.of("a null list", null, "null") // CMCS-3: Null list
            );
        }
    }

    @Nested
    @DisplayName("stringToSomeObjectList tests (Deserialization)")
    class StringToSomeObjectListTest {

        @Test
        @DisplayName("should deserialize a standard JSON string")
        void stringToSomeObjectList_whenStandardJson_returnsCorrectList() {
            // Arrange
            String json = "[{\"colorImg\":1,\"colorValue\":10}]";

            // Act
            List<ColorModel> result = ColorModelConverters.stringToSomeObjectList(json);

            // Assert
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getColorImg()).isEqualTo(1);
            assertThat(result.get(0).getColorValue()).isEqualTo(10);
        }

        @Test
        @DisplayName("should return an empty list for empty or null JSON")
        void stringToSomeObjectList_whenJsonIsEmptyOrNull_returnsEmptyList() {
            assertThat(ColorModelConverters.stringToSomeObjectList("[]")).isEmpty();
            assertThat(ColorModelConverters.stringToSomeObjectList(null)).isEmpty();
        }

        @Test
        @DisplayName("should throw JsonSyntaxException for invalid JSON")
        void stringToSomeObjectList_whenJsonIsInvalid_throwsException() {
            assertThatThrownBy(() -> ColorModelConverters.stringToSomeObjectList("not a json"))
                    .isInstanceOf(JsonSyntaxException.class);
        }
    }
}