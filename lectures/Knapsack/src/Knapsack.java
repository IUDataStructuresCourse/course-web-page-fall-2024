import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;

class Result {
    Result(String i, int cal, Result r) {
        item = i;
        calories = cal;
        rest = r;
    }

    String item;
    int calories;
    Result rest;
}

public class Knapsack {

    public static void print_items(HashSet<String> items) {
        for (String item : items)
            System.out.println(item);
        System.out.println("");
    }

    static Result knapsack_aux1(int availableWeight,
                                HashSet<String> remainingItems,
                                HashMap<String, Integer> weight,
                                HashMap<String, Integer> calories) {
        Result best_result = new Result("None", 0, null);
        for (String item : remainingItems) {
            if (weight.get(item) <= availableWeight) {
                HashSet<String> newRemainingItems = (HashSet<String>) remainingItems.clone();
                newRemainingItems.remove(item);
                Result rest = knapsack_aux1(availableWeight - weight.get(item),
                        newRemainingItems, weight, calories);
                int new_calories = rest.calories + calories.get(item);
                if (new_calories > best_result.calories) {
                    best_result = new Result(item, new_calories, rest);
                }
            }
        }
        return best_result;
    }

    public static HashSet<String>
    knapsack1(int availableWeight,
              HashSet<String> items,
              HashMap<String, Integer> weight,
              HashMap<String, Integer> calories) {
        Result r = knapsack_aux1(availableWeight, items, weight, calories);
        HashSet<String> choices = new HashSet<>();
        while (r != null) {
            if (r.item != "None")
                choices.add(r.item);
            r = r.rest;
        }
        return choices;
    }

    static Result knapsack_aux2(int availableWeight,
                                int numItemsRemaining,
                                ArrayList<String> items,
                                HashMap<String, Integer> weight,
                                HashMap<String, Integer> calories) {
        if (numItemsRemaining == 0)
            return new Result("None", 0, null);
        String item = items.get(numItemsRemaining - 1);
        // Don't purchase the current item
        Result rest1 = knapsack_aux2(availableWeight,
                numItemsRemaining - 1, items, weight, calories);
        Result best_result = rest1;
        // Purchase the current item
        if (weight.get(item) <= availableWeight) {
            Result rest2 = knapsack_aux2(availableWeight - weight.get(item),
                    numItemsRemaining - 1, items, weight, calories);
            int new_calories = rest2.calories + calories.get(item);
            if (new_calories > best_result.calories) {
                best_result = new Result(item, new_calories, rest2);
            }
        }
        return best_result;
    }

    public static HashSet<String>
    knapsack2(int availableWeight,
              HashSet<String> items,
              HashMap<String, Integer> weight,
              HashMap<String, Integer> calories) {
        ArrayList<String> items_array = new ArrayList<>(items);
        Result r = knapsack_aux2(availableWeight, items.size(), items_array, weight, calories);
        HashSet<String> choices = new HashSet<>();
        while (r != null) {
            if (r.item != "None")
                choices.add(r.item);
            r = r.rest;
        }
        return choices;
    }

    static Result knapsack_aux2_memo(int availableWeight, int numItemsRemaining,
                                     ArrayList<String> items,
                                     HashMap<String, Integer> weight,
                                     HashMap<String, Integer> calories, Result[][] R) {
        if (R[availableWeight][numItemsRemaining] != null)
            return R[availableWeight][numItemsRemaining];
        if (numItemsRemaining == 0) {
            Result r = new Result("None", 0, null);
            R[availableWeight][numItemsRemaining] = r;
            return r;
        }
        String item = items.get(numItemsRemaining - 1);
        // Don't purchase the numItemsRemaining
        Result rest1 = knapsack_aux2_memo(availableWeight,
                numItemsRemaining - 1, items, weight, calories, R);
        Result best_result = rest1;
        // Purchase the numItemsRemaining
        if (weight.get(item) <= availableWeight) {
            Result rest2 = knapsack_aux2_memo(availableWeight - weight.get(item),
                    numItemsRemaining - 1, items, weight, calories, R);
            int new_calories = rest2.calories + calories.get(item);
            if (new_calories > best_result.calories) {
                best_result = new Result(item, new_calories, rest2);
            }
        }
        R[availableWeight][numItemsRemaining] = best_result;
        return best_result;
    }

    public static HashSet<String>
    knapsack3(int availableWeight,
              HashSet<String> items,
              HashMap<String, Integer> weight,
              HashMap<String, Integer> calories) {
        ArrayList<String> items_array = new ArrayList<>(items);
        Result[][] R = new Result[availableWeight + 1][];
        for (int i = 0; i != availableWeight + 1; ++i) {
            R[i] = new Result[items.size() + 1];
        }
        Result r = knapsack_aux2_memo(availableWeight, items.size(), items_array, weight, calories, R);
        HashSet<String> choices = new HashSet<>();
        while (r != null) {
            if (r.item != "None")
                choices.add(r.item);
            r = r.rest;
        }
        return choices;
    }

    // Dynamic Programming Version
    static void knapsack_one(int availableWeight,
                             int numItemsRemaining,
                             ArrayList<String> items,
                             HashMap<String, Integer> weight,
                             HashMap<String, Integer> calories, Result[][] R) {
        String item = items.get(numItemsRemaining - 1);
        // Don't purchase the numItemsRemaining
        Result rest1 = R[availableWeight][numItemsRemaining - 1];
        Result best_result = rest1;
        // Purchase the numItemsRemaining
        if (weight.get(item) <= availableWeight) {
            Result rest2 = R[availableWeight - weight.get(item)][numItemsRemaining - 1];
            int new_calories = rest2.calories + calories.get(item);
            if (new_calories > best_result.calories) {
                best_result = new Result(item, new_calories, rest2);
            }
        }
        R[availableWeight][numItemsRemaining] = best_result;
    }

    public static HashSet<String>
    knapsack4(int availableWeight,
              HashSet<String> items,
              HashMap<String, Integer> weight,
              HashMap<String, Integer> calories) {
        ArrayList<String> items_array = new ArrayList<>(items);
        Result[][] R = new Result[availableWeight + 1][];
        for (int i = 0; i != availableWeight + 1; ++i) {
            R[i] = new Result[items.size() + 1];
        }
        for (int i = 0; i != availableWeight + 1; ++i) {
            R[i][0] = new Result("None", 0, null);
        }
        for (int numItemsRemaining = 1; numItemsRemaining != items.size() + 1; ++numItemsRemaining) {
            for (int availWeight = 0; availWeight != availableWeight + 1; ++availWeight) {
                knapsack_one(availWeight, numItemsRemaining, items_array, weight, calories, R);
            }
        }
        Result r = R[availableWeight][items.size()];
        HashSet<String> choices = new HashSet<>();
        while (r != null) {
            if (r.item != "None")
                choices.add(r.item);
            r = r.rest;
        }
        return choices;
    }
}
