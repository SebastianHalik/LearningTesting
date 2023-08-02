package pl.devfoundry.testing.cart;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import pl.devfoundry.testing.order.Order;
import pl.devfoundry.testing.order.OrderStatus;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.*;

@MockitoSettings(strictness = Strictness.STRICT_STUBS)
@ExtendWith(MockitoExtension.class)
class CartServiceTest {
  @InjectMocks
  private CartService cartService;
  @Mock
  private CartHandler cartHandler;
  @Captor
  private ArgumentCaptor<Cart> argumentCaptor;

  @Test
  void processCartShouldSendToPrepare() {
    //given
    Order order = new Order();
    Cart cart = new Cart();
    cart.addOrderToCart(order);

    given(cartHandler.canHandleCart(cart)).willReturn(true);

    //when
    Cart resultCart = cartService.processCart(cart);

    //then
    //Weryfikujemy czy została wywołana metoda sendToPrepare na cartHandler, a została powyżej w proccessCart
    verify(cartHandler).sendToPrepare(cart);
    verify(cartHandler, times(1)).sendToPrepare(cart);
    verify(cartHandler, atLeastOnce()).sendToPrepare(cart);

    //Inna wersja sprawdza kolejnosc dodatkowo
    InOrder inOrder = inOrder(cartHandler);
    inOrder.verify(cartHandler).canHandleCart(cart);
    inOrder.verify(cartHandler).sendToPrepare(cart);
  }

  @Test
  void processCartShouldNotSendToPrepare() {
    //given
    Order order = new Order();
    Cart cart = new Cart();
    cart.addOrderToCart(order);

    given(cartHandler.canHandleCart(cart)).willReturn(false);

    //when
    Cart resultCart = cartService.processCart(cart);

    //then
    verify(cartHandler, never()).sendToPrepare(cart);
  }

  @Test
  void processCartShouldNotSendToPrepareWithArgumentMatchers() {
    //given
    Order order = new Order();
    Cart cart = new Cart();
    cart.addOrderToCart(order);

    given(cartHandler.canHandleCart(any(Cart.class))).willReturn(false);

    //when
    Cart resultCart = cartService.processCart(cart);

    //then
    verify(cartHandler, never()).sendToPrepare(any(Cart.class));

  }

  @Test
  void canHandleCartReturnMultipleValues() {
    //given
    Order order = new Order();
    Cart cart = new Cart();
    cart.addOrderToCart(order);

    given(cartHandler.canHandleCart(any(Cart.class))).willReturn(true, false, false, true);

    //then
    assertThat(cartHandler.canHandleCart(cart), equalTo(true));
    assertThat(cartHandler.canHandleCart(cart), equalTo(false));
    assertThat(cartHandler.canHandleCart(cart), equalTo(false));
    assertThat(cartHandler.canHandleCart(cart), equalTo(true));
  }

  @Test
  void processCartShouldSendToPrepareWithLambdas() {
    //given
    Order order = new Order();
    Cart cart = new Cart();
    cart.addOrderToCart(order);

    given(cartHandler.canHandleCart(argThat(c -> !c.getOrders().isEmpty()))).willReturn(true);

    //when
    Cart resultCart = cartService.processCart(cart);

    //then
    then(cartHandler).should().sendToPrepare(cart);
    assertThat(resultCart.getOrders(), hasSize(1));
    assertThat(resultCart.getOrders().get(0).getOrderStatus(), equalTo(OrderStatus.PREPARING));
  }

  @Test
  void canHandleCartShouldThrowException() {
    //given
    Order order = new Order();
    Cart cart = new Cart();
    cart.addOrderToCart(order);

    given(cartHandler.canHandleCart(cart)).willThrow(IllegalStateException.class);

    //when
    //then
    assertThrows(IllegalStateException.class, () -> cartService.processCart(cart));
  }

  @Test
  void processCartShouldSendToPrepareWithArgumentCapture() {
    //given
    Order order = new Order();
    Cart cart = new Cart();
    cart.addOrderToCart(order);

    //1 captor = 1 argument (czyli 2 arg w metodzie to 2 captory)

    given(cartHandler.canHandleCart(cart)).willReturn(true);

    //when
    Cart resultCart = cartService.processCart(cart);

    //then
    //verify(cartHandler).sendToPrepare(argumentCaptor.capture()); to samo co nizej
    then(cartHandler).should().sendToPrepare(argumentCaptor.capture());

    assertThat(argumentCaptor.getValue().getOrders().size(), equalTo(1));

    assertThat(resultCart.getOrders(), hasSize(1));
    assertThat(resultCart.getOrders().get(0).getOrderStatus(), equalTo(OrderStatus.PREPARING));
  }

  @Test
  void shouldDoNothingWhenProcessCart() {
    //given
    Order order = new Order();
    Cart cart = new Cart();
    cart.addOrderToCart(order);

    given(cartHandler.canHandleCart(cart)).willReturn(true);

    willDoNothing().given(cartHandler).sendToPrepare(cart);

    //when
    Cart resultCart = cartService.processCart(cart);

    //then

    assertThat(resultCart.getOrders(), hasSize(1));
    assertThat(resultCart.getOrders().get(0).getOrderStatus(), equalTo(OrderStatus.PREPARING));
  }

  @Test
  void shouldAnswerWhenProcessCart() {
    //given
    Order order = new Order();
    Cart cart = new Cart();
    cart.addOrderToCart(order);

    doAnswer(invocationOnMock -> {
      Cart argumentCart = invocationOnMock.getArgument(0);
      argumentCart.clearCart();
      return true;
    }).when(cartHandler).canHandleCart(cart);
    //when
    Cart resultCart = cartService.processCart(cart);

    //then
    assertThat(resultCart.getOrders().size(), equalTo(0));
  }

  @Test
  void deliveryShouldBeFree() {
    //given
    Cart cart = new Cart();
    cart.addOrderToCart(new Order());
    cart.addOrderToCart(new Order());
    cart.addOrderToCart(new Order());

    given(cartHandler.isDeliveryFree(cart)).willCallRealMethod();

    //when
    boolean isDeliveryFree = cartHandler.isDeliveryFree(cart);

    //then
    assertTrue(isDeliveryFree);
  }
}