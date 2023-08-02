package pl.devfoundry.testing.meal;

import java.util.List;

public class MealRepository {

  private List<Meal> meals;

  public void add(Meal meal) {
    meals.add(meal);
  }

  public List<Meal> getAllMeals() {
    return meals;
  }

  public void delete(Meal meal) {
    meals.remove(meal);
  }

  public List<Meal> findByName(String name, boolean exactMatch) {
    if (exactMatch) {
      return meals.stream().filter(meal -> meal.getName().equals(name)).toList();
    } else {
      return meals.stream().filter(meal -> meal.getName().startsWith(name)).toList();
    }
  }

  public List<Meal> findByPrice(int price) {
    return meals.stream().filter(meal -> meal.getPrice() == price).toList();
  }
}
