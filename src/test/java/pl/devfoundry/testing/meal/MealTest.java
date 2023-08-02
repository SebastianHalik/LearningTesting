package pl.devfoundry.testing.meal;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.devfoundry.testing.extensions.IAExceptionIgnoreExtension;
import pl.devfoundry.testing.meal.Meal;
import pl.devfoundry.testing.order.Order;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

class MealTest {
  @Spy
  private Meal mealSpy;

  @Test
  void shouldReturnDiscountedPrice() {
    //given
    Meal meal = new Meal(35);

    //when
    int discountedPrice = meal.getDiscountedPrice(7);

    //then
    assertEquals(28, discountedPrice);
    // w assertJ: assertThat(discountedPrice).isEqualTo(28);
  }

  @Test
  void referencesToSameObjectShouldBeEqual() {
    Meal meal1 = new Meal(10);
    Meal meal = meal1;
    assertSame(meal, meal1);
    //AssertJ : assertThat(meal1).isSameAs(meal);
  }

  @Test
  void referencesToDifferentObjectShouldNotBeEqual() {
    Meal meal1 = new Meal(10);
    Meal meal = new Meal(20);
    assertNotSame(meal, meal1);
    assertThat(meal1, not(sameInstance(meal)));
    //AssertJ : assertThat(meal1).isNotSameAs(meal);
  }

  @Tag("Pizza")
  @Test
  void twoMealsShouldBeEqualWhenPriceAndNameAreSame() {
    //given
    Meal meal1 = new Meal(10, "Pizza");
    Meal meal2 = new Meal(10, "Pizza");

    //then
    assertEquals(meal1, meal2);
    //AssertJ : assertThat(meal1).isEqualTo(meal);
  }

  @Tag("Pizza")
  @Test
  void exceptionShouldBeThrownIfDiscountHigherThanPrice() {
    //given
    Meal meal = new Meal(5, "Pizza");

    //when
    //then
    assertThrows(IllegalArgumentException.class, () -> meal.getDiscountedPrice(666));
  }

  @ParameterizedTest
  @ValueSource(ints = {5, 10, 15, 19})
  void mealPricesShouldBeLowerThan20(int price) {
    assertThat(price, lessThan(20));
  }

  @ParameterizedTest
  @MethodSource("createMealsWithNameAndPrice")
  void burgersShouldHaveCorrectNameAndPrice(String name, int price) {
    assertThat(name, containsString("burger"));
    assertThat(price, greaterThanOrEqualTo(10));
  }

  @ParameterizedTest
  @MethodSource("createCakeNames")
  void cakeNamesShouldEndWithCake(String name) {
    assertThat(name, notNullValue());
    assertThat(name, endsWith("cake"));
  }

  private static Stream<Arguments> createMealsWithNameAndPrice() {
    return Stream.of(
        Arguments.of("Hamburger", 10),
        Arguments.of("Cheeseburger", 14)
    );
  }

  private static Stream<String> createCakeNames() {
    List<String> cakeNames = Arrays.asList("Cheesecake", "Fruitcake", "Cupcake");
    return cakeNames.stream();
  }

  @ExtendWith(IAExceptionIgnoreExtension.class)
  @ParameterizedTest
  @ValueSource(ints = {1, 3, 5, 7})
  void mealPricesShouldBeLowerThan10(int price) {
    if (price > 5) {
      throw new IllegalArgumentException();
    }
    assertThat(price, lessThan(20));
  }

  @TestFactory
  Collection<DynamicTest> dynamicTestCollection() {
    return Arrays.asList(
        DynamicTest.dynamicTest("Dynamic test 1", () -> assertThat(5, lessThan(6))),
        DynamicTest.dynamicTest("Dynamic test 2", () -> assertEquals(4, 2 * 2))
    );
  }

  private int calculatePrice(int price, int quantity) {
    return price * quantity;
  }

  @Tag("Pizza")
  @TestFactory
  Collection<DynamicTest> calculateMealPrices() {
    Order order = new Order();
    order.addMealToOrder(new Meal(10, 2, "Hamburger"));
    order.addMealToOrder(new Meal(12, 4, "Pizza"));
    order.addMealToOrder(new Meal(13, 5, "Pepsi"));
    Collection<DynamicTest> dynamicTests = new ArrayList<>();

    for (Meal meal : order.getMeals()) {
      int price = meal.getPrice();
      int quantity = meal.getQuantity();

      Executable executable = () -> {
        assertThat(calculatePrice(price, quantity), lessThan(99));
      };

      String name = "Test for" + meal.getName();
      DynamicTest dynamicTest = DynamicTest.dynamicTest(name, executable);
      dynamicTests.add(dynamicTest);
    }
    return dynamicTests;
  }

  @Test
  void testMealSumPrice() {
    //given
    Meal meal = mock(Meal.class);

    given(meal.getPrice()).willReturn(15);
    given(meal.getQuantity()).willReturn(3);
    given(meal.sumPrice()).willCallRealMethod();

    //when
    int result = meal.sumPrice();

    assertThat(result, equalTo(15 * 3));
  }

  @Test
  @ExtendWith(MockitoExtension.class)
  void testMealSumPriceWithSpy() {
    //given
    Meal meal = new Meal(15, 3, "Burito"); //Jesli chce korzystac z konstruktora argumentowego
    //Jesli nie to wtedy linijka na gorze out, a na dole spy(Meal.class)
    //Opcja B to uzycie pola klasy
    //Spy = obiekt hybrydowy ktory miesza mocka z realnym obiektem
    Meal mealSpy = spy(meal);

    given(mealSpy.getPrice()).willReturn(15);
    given(mealSpy.getQuantity()).willReturn(3);

    //when
    int result = mealSpy.sumPrice();
    then(mealSpy).should().getPrice();
    assertThat(result, equalTo(15 * 3));
  }
}