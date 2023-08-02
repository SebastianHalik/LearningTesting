package pl.devfoundry.testing.cart;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.devfoundry.testing.meal.Meal;
import pl.devfoundry.testing.order.Order;

import java.time.Duration;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test cases for Cart")
class CartTest {

  @Test
  @Disabled //Wylacza test
  @DisplayName("Cart is able to process 1000 orders in 100 ml")
  void simulateLargeOrder() {
    //given
    Cart cart = new Cart();

    //when
    //then
    assertTimeout(Duration.ofMillis(100), this::simulateLargeOrder);
  }

  @Test
  void cartShouldNotBeEmptyAfterAddingOrderToCart() {
    //given
    Order order = new Order();
    Cart cart = new Cart();

    //when
    cart.addOrderToCart(order);

    //then
    assertThat(cart.getOrders(), anyOf(
        notNullValue(),
        hasSize(1),
        is(not(empty())),
        is(not(emptyCollectionOf(Order.class)))
    ));
    //W przypadku błędów wyrzuci tylko pierwszy
    assertThat(cart.getOrders(), allOf(
        notNullValue(),
        hasSize(1),
        is(not(empty())),
        is(not(emptyCollectionOf(Order.class)))
    ));
    //W przypadku błędów wyrzuci wszystkie
    assertAll(
        () -> assertThat(cart.getOrders(), notNullValue()),
        () -> assertThat(cart.getOrders(), hasSize(1)),
        () -> assertThat(cart.getOrders(), is(not(empty()))),
        () -> assertThat(cart.getOrders(), is(not(emptyCollectionOf(Order.class)))),
        () -> {
          List<Meal> mealList = cart.getOrders().get(0).getMeals();
          assertThat(mealList, empty());
        }
    );
  }
}