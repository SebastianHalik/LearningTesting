package pl.devfoundry.testing.account;

public class Account {
  private boolean active;
  private String email;
  private Address defaultDeliveryAddress;

  public Account(Address defaultDeliveryAddress) {
    this.defaultDeliveryAddress = defaultDeliveryAddress;
    if (defaultDeliveryAddress != null) {
      activate();
    } else {
      this.active = false;
    }
  }

  public Account() {
    this.active = false;
  }

  public boolean isActive() {
    return active;
  }

  public void activate() {
    this.active = true;
  }

  public Address getDefaultDeliveryAddress() {
    return defaultDeliveryAddress;
  }

  public void setDefaultDeliveryAddress(Address defaultDeliveryAddress) {
    this.defaultDeliveryAddress = defaultDeliveryAddress;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    String emailRegex = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
    if (email.matches(emailRegex)) {
      this.email = email;
    } else {
      throw new IllegalArgumentException("Wrong Email Format");
    }
  }
}
