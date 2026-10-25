# Word Ladder I

## Problem Statement

Given two distinct words, `startWord` and `targetWord`, and a list of `N` unique words called `wordList`, where each word has the same length `M`, find the length of the shortest transformation sequence from `startWord` to `targetWord`.

Keep the following conditions in mind:

- A word can only consist of lowercase English letters.
- Only one letter can be changed in each transformation.
- Each transformed word must exist in `wordList`, including `targetWord`.
- `startWord` may or may not be present in `wordList`.

**Note:** If there is no possible way to transform `startWord` into `targetWord`, return `0`.

## Examples

### Example 1

**Input:**
```text
wordList = ["des", "der", "dfr", "dgt", "dfs"]
startWord = "der"
targetWord = "dfs"
```

**Output:**
```text
3
```

**Explanation:**

The shortest transformation sequence from `der` to `dfs` is:

`der` → `dfr` → `dfs`

- Change `e` to `f` to transform `der` into `dfr`.
- Change `r` to `s` to transform `dfr` into `dfs`.

All words in the sequence are present in `wordList`. The sequence contains three words, so the answer is `3`.

### Example 2

**Input:**
```text
wordList = ["geek", "gefk"]
startWord = "gedk"
targetWord = "geek"
```

**Output:**
```text
2
```

**Explanation:**

The shortest transformation sequence is:

`gedk` → `geek`

Only one character changes: `d` is replaced by `e`. Since `geek` is present in `wordList`, the transformation is valid. The sequence contains two words, so the answer is `2`.

## Constraints

- `1 <= N <= 5000`
- `1 <= M <= 10`
- All words consist of lowercase English letters.
- All words in `wordList` are unique and have the same length.
- `startWord` and `targetWord` are distinct.

## Expected Approach

Use **Breadth-First Search (BFS)** to explore all valid transformations level by level. Since each transformation changes exactly one character, BFS finds the shortest sequence.

Return the number of words in the first sequence that reaches `targetWord`. If the target cannot be reached, return `0`.

## Complexity Analysis

Let `N` be the number of words in `wordList` and `M` be the length of each word.

- **Expected time complexity:** `O(N × M × 26)` using a hash set for dictionary lookups.
- **Auxiliary space complexity:** `O(N × M)` for the hash set and BFS queue.
