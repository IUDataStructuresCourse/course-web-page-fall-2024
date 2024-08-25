import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class KnapsackTest {

    @Test
    void test_knapsack() {
        HashSet<String> items = new HashSet<>();
        HashMap<String,Integer> weight = new HashMap<>();
        HashMap<String,Integer> calories = new HashMap<>();

        items.add("Chicken");
        items.add("Apple");
        items.add("Oats");
        items.add("Salami");

        weight.put("Chicken", 2);
        weight.put("Apple", 2);
        weight.put("Oats", 4);
        weight.put("Salami", 6);
        weight.put("None", 0);

        calories.put("Chicken", 300);
        calories.put("Apple", 40);
        calories.put("Oats", 350);
        calories.put("Salami", 400);
        calories.put("None", 0);

        HashSet<String> expected = new HashSet<>();
        expected.add("Oats");
        expected.add("Salami");
        {
            HashSet<String> best_choice = Knapsack.knapsack1(10, items, weight, calories);
            assertTrue(expected.equals(best_choice));
        }
        {
            HashSet<String> best_choice = Knapsack.knapsack2(10, items, weight, calories);
            assertTrue(expected.equals(best_choice));
        }
        {
            HashSet<String> best_choice = Knapsack.knapsack3(10, items, weight, calories);
            assertTrue(expected.equals(best_choice));
        }
        {
            HashSet<String> best_choice = Knapsack.knapsack4(10, items, weight, calories);
            assertTrue(expected.equals(best_choice));
        }

    }

}