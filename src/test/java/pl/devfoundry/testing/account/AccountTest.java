package pl.devfoundry.testing.account;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pl.devfoundry.testing.account.Account;
import pl.devfoundry.testing.account.Address;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assumptions.assumingThat;
// Aby sprawdzic pokrycie testami w konsoli wpisz mvn test. Następnie odpal w przegladarce target.site.jacoco -> intex.html
@Tag("Account")
class AccountTest {
  @Test
  void newAccountShouldNotBeActiveAfterCreation() {
    //given
    Account newAccount = new Account();

    //then
    assertFalse(newAccount.isActive());
    assertThat(newAccount.isActive(), equalTo(false));
  }

  @Test
  void newAccountShouldBeActivateAfterActivation() {
    Account newAccount = new Account();
    assertFalse(newAccount.isActive());
    newAccount.activate();
    assertTrue(newAccount.isActive());
  }

  @Test
  void newCreatedAccountShouldNotHaveDefaultDeliveryAddress() {
    //given
    Account account = new Account();

    //when
    Address address = account.getDefaultDeliveryAddress();

    //then

    assertNull(address);
    assertThat(address, nullValue());
  }

  @Test
  void defaultDeliveryAddressShouldNotBeNullAfterDeliverySet() {
    //given
    Address address = new Address("Krakowska", "15");
    Account account = new Account();
    account.setDefaultDeliveryAddress(address);

    //when
    Address defaultAddress = account.getDefaultDeliveryAddress();

    //then
    assertNotNull(defaultAddress);
    assertThat(defaultAddress, is(notNullValue()));
  }

  @Test
  void newAccountWithNoNullAddressShouldBeActivated() {
    //given
    Address address = new Address("Puławka", "12");

    //when
    Account account = new Account(address);

    //then
    assumingThat(address != null, () -> {
      assertTrue(account.isActive());
    });
  }

  @Test
  void invalidEmailShouldThrownException() {
    //given
    Account account = new Account();

    //when
    //then
    assertThrows(IllegalArgumentException.class, () -> account.setEmail("wrongEmail"));
  }

  @Test
  void validEmailShouldBeSet() {
    //given
    Account account = new Account();
    String email = "test@testowo.pl";

    //when
    account.setEmail(email);
    //then
    assertThat(account.getEmail(), is(email));
  }
}
