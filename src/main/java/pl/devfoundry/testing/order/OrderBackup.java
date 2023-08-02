package pl.devfoundry.testing.order;

import pl.devfoundry.testing.order.Order;

import java.io.*;

public class OrderBackup {

  private Writer writer;

  public OrderBackup() {
  }

  void createFile() throws FileNotFoundException {
    File file = new File("orderBackup.txt");
    FileOutputStream fileOutputStream = new FileOutputStream(file);
    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
    writer = new BufferedWriter(outputStreamWriter);
  }

  void closeFile() throws IOException {
    writer.close();
  }

  void backUpOrder(Order order) throws IOException {
    if (writer == null) {
      throw new IOException("Backup file not created");
    }
    writer.append(order.toString());
  }

  public Writer getWriter() {
    return writer;
  }
}
