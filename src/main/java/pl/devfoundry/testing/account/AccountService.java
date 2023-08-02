package pl.devfoundry.testing.account;

import java.util.List;

public class AccountService {
  private final AccountRepository accountRepository;

  public AccountService(AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  public List<Account> getAllActiveAccounts() {
    return accountRepository.getAllAccounts().stream()
        .filter(Account::isActive)
        .toList();
  }

  public List<String> findByName(String name) {
    return accountRepository.getByName(name);
  }
}
