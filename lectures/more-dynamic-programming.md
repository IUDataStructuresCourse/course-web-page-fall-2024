# DNA Alignment Continued

## Extracting the Alignment from the Table

Last time we applied dynamic programming to
find the best alignment of the DNA strings

    GAA
    GGA

Here's the resulting table, which contains the solution for how to
best align the two strings and every pair of prefixes of them.


          Y =            G     G     A
          j =      0  |  1  |  2  |  3
              --------------------------
        X i = 0 |  0  |I:-1 |I:-2 |I:-3
          G   1 |D:-1 |M:2  |M:1  |I:0
          A   2 |D:-2 |D:1  |M:0  |M:3
          A   3 |D:-3 |D:0  |I:-1 |D:2

The best score for all of GGA and GAA is in the bottom-right corner:

    best score = 2

To obtain the best alignment, start at the bottom-right corner
(row 3, column 3) and consult the choice that was made.

The choice was D (delete), so that corresponds to aligning
the last letter in X with a gap:

         Partial Solution      Remaining Strings
    X    A                     GA
    Y    _                     GGA

To get the rest of the solution, we move up one cell because the
choice was D (delete).

The choice in row 2, column 3 was M (match/mismatch), so that
corresponds to aligning the last letter of each of the remaining
strings (A and A).

         Partial Solution      Remaining Strings
    X    AA                    G
    Y    A_                    GG

To get the rest of then solution, we move diagonally up and left
because the choice was M (match/mismatch).

The choice in row 1, column 2 was M (match/mismatch).

         Partial Solution      Remaining Strings
    X    GAA
    Y    GA_                   G

Again we move diagonally up and left.

The choice in row 0, column 1 is I (insert), so we align
the letter G from Y with a gap.

         Partial Solution      Remaining Strings
    X    _GAA                  
    Y    GGA_                  

Because we did an insert, we move to the left one cell.

So the best alignment of the strings X and Y is:

    X    _GAA
    Y    GGA_
	     -1 2 2 -1 = 2

Summary:
D: move up
I: move left
M: move up and left (diagonal)


## Time Complexity

Let m be the length of the first string and n the length of the second.

The time complexity is  O(mn).

The space complexity is  O(mn).

# The 0-1 Knapsack Problem

Let us return to the 0-1 knapsack problem we discussed last week and
see how to solve it using dynamic programming.

Suppose you are preparing to hike the Appalachian Trail. Select items
from a grocery store that will fit in your backpack while maximizing
the number of calories.  You have room for 10 ounces of food in your
backpack and you are not allowed to split items.

                    Weight (oz)    Calories
    Chicken Curry   2              300
    Apple           2               40
    Oats            4              350
    Dry Salami      6              400 

Recall the three steps in developing a dynamic programming solution.
1. Create a recursive function that selects the best solution from
   all possible solutions.
2. Memoize the recursive function to save time on overlapping
   subproblems.
3. Reorganize the recursive function into a loop that iterates
   bottom-up through the problem tree.

## Recursive Solution

Create a Result class to represent the choice of one item.

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

Other data structures:

* set of items left to choose from
* map from item to weight
* map from item to calories

Recursive function:

    static Result knapsack_aux1(int availableWeight,
                                HashSet<String> remainingItems,
                                HashMap<String, Integer> weight,
                                HashMap<String, Integer> calories) {
        ...
    }

Iterate through all possible choices of the next item to purchase:

    static Result knapsack_aux1(int availableWeight,
                                HashSet<String> remainingItems,
                                HashMap<String, Integer> weight,
                                HashMap<String, Integer> calories) {
        Result best_result = new Result("None", 0, null);
        for (String item : remainingItems) {
            ...
        }
        return best_result;
    }

We only want to consider items that weight less than the remainingWeight:

        for (String item : remainingItems) {
            if (weight.get(item) <= availableWeight) {
                ...
            }
        }

We use recursion to obtian the best set of choices for the rest of the items.

            if (weight.get(item) <= availableWeight) {
                HashSet<String> newRemainingItems = (HashSet<String>) remainingItems.clone();
                newRemainingItems.remove(item);
                Result rest = knapsack_aux1(availableWeight - weight.get(item),
                                            newRemainingItems, weight, calories);
                ...
            }
            
Then we check whether the current choice of `item` is the best yet.

                int new_calories = rest.calories + calories.get(item);
                if (new_calories > best_result.calories) {
                    best_result = new Result(item, new_calories, rest);
                }

Putting this together, we have 

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
    
We can then read off from the chain of `Result` objects to obtain the
set of choices.

    public static HashSet<String>
    knapsack1(int availableWeight, HashSet<String> items,
              HashMap<String, Integer> weight, HashMap<String, Integer> calories) {
        Result r = knapsack_aux1(availableWeight, items, weight, calories);
        HashSet<String> choices = new HashSet<>();
        while (r != null) {
            if (r.item != "None")
                choices.add(r.item);
            r = r.rest;
        }
        return choices;
    }

## Alternate Enumeration

With memoization in mind, let's find an alternative way to enumerate
the possibilities that doesn't require a HashSet for the
`remainingItems`.  We can instead keep track of how many items are
left to process, and for each item, decide whether to purchase it or
not.

    static Result knapsack_aux2(int availableWeight, int numItemsRemaining,
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


## Memoization

With the inputs expressed as integers, we can use a 2-D array for
memoization.

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
        Result r = knapsack_aux2_memo(availableWeight, items.size(), 
		                              items_array, weight, calories, R);
        HashSet<String> choices = new HashSet<>();
        while (r != null) {
            if (r.item != "None")
                choices.add(r.item);
            r = r.rest;
        }
        return choices;
    }

## Dynamic Programming Solution to 0-1 Knapsack

We'll put the code for the recursive case in the following function.

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

We add an loop for the base cases, for zero items across all the
weights, and then doubly-nested loop for the recursive cases.

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

