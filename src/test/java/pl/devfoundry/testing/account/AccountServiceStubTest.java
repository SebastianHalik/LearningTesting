package pl.devfoundry.testing.account;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

class AccountServiceStubTest {
  //Stuby są okej dla małych kodów i interfejsów. Lepsze są właśnie mocki, bo jeśli AccREpo by dostało nową metode to trzeba aktualizowac testy
  @Test
  void getAllActiveAccounts() {
    //given
    AccountRepository accountRepository = new AccountRepositoryStub();
    AccountService accountService = new AccountService(accountRepository);

    //when
    List<Account> accountList = accountService.getAllActiveAccounts();

    //then
    assertThat(accountList, hasSize(2));
  }
}