package pl.devfoundry.testing.order;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;

class OrderBackupExecutionOrderTest {
  @Test
  void callingBackupWithoutCreatingAFileFirstShouldThrownException() {
    //given
    OrderBackup orderBackup = new OrderBackup();

    //then
    assertThrows(IOException.class, () -> orderBackup.backUpOrder(new Order()));
  }
}
