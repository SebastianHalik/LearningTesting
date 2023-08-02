package pl.devfoundry.testing.order;

import pl.devfoundry.testing.meal.Meal;

import java.util.ArrayList;
import java.util.List;

public class Order {
  private List<Meal> meals = new ArrayList<>();
  private OrderStatus orderStatus;

  void cancel() {
    this.meals.clear();
  }

  int totalPrice() {
    int sum = this.meals.stream().mapToInt(Meal::getPrice).sum();
    if (sum < 0) {
      throw new IllegalStateException("Price limit exceeded");
    }
    return sum;
  }

  public List<Meal> getMeals() {
    return meals;
  }

  public OrderStatus getOrderStatus() {
    return orderStatus;
  }

  public void changeOrderStatus(OrderStatus orderStatus) {
    this.orderStatus = orderStatus;
  }

  public void setMeals(List<Meal> meals) {
    this.meals = meals;
  }

  public void addMealToOrder(Meal meal) {
    this.meals.add(meal);
  }

  public void removeMealFromOrder(Meal meal) {
    this.meals.remove(meal);
  }

  @Override
  public String toString() {
    return "Order{" +
        "meals=" + meals +
        '}';
  }
}
