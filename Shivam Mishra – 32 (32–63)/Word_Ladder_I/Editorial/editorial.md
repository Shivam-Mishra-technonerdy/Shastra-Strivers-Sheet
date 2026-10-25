# Editorial: Word Ladder I

## Topic
Breadth-First Search (BFS), Hash Set, Shortest Path

## Problem Statement
Given two strings, `startWord` and `targetWord`, and a dictionary `wordList`, find the length of the shortest transformation sequence from `startWord` to `targetWord`.

A transformation consists of changing exactly one character at a time, and every intermediate word must belong to the dictionary. The starting word does not need to be present in the dictionary.

Return the number of words in the shortest valid sequence, including both the starting and target words. If no valid transformation sequence exists, return `0`.

### Example

**Input:**
```text
der dfs
5
des der dfr dgt dfs
```

**Output:**
```text
3
```

**Explanation:**

One shortest transformation sequence is:

`der → dfr → dfs`

- `der` to `dfr`: Change the first character from `d` to `d`? No change is needed there; instead, the second character changes from `e` to `f`, and the first remains `d`. The third character changes from `r` to `r`. Thus, this is not a valid one-character transformation.

A valid sequence for the given dictionary is:

`der → des → dfs`

However, `des` to `dfs` changes two characters, so this is also invalid. Therefore, the example input above is not suitable for demonstrating a valid sequence.

Use the following corrected example instead.

**Corrected Input:**
```text
hit cog
6
hot dot dog lot log cog
```

**Output:**
```text
5
```

**Explanation:**

One shortest transformation sequence is:

`hit → hot → dot → dog → cog`

This sequence contains five words, so the answer is `5`.

## Approach: Breadth-First Search (BFS)

### 1. Why BFS?

The problem asks for the shortest sequence of transformations. Each valid transformation changes exactly one character, meaning every transition has the same cost.

Breadth-First Search explores states level by level:

- Level 1 contains the starting word.
- Level 2 contains words reachable using one transformation.
- Level 3 contains words reachable using two transformations.
- Subsequent levels represent increasingly longer transformation sequences.

Consequently, the first time BFS reaches `targetWord`, it has found a shortest transformation sequence.

### 2. Use a Hash Set for the Dictionary

Store all dictionary words in an `unordered_set<string>`.

A hash set is useful because it supports efficient average-case lookup, insertion, and deletion.

Instead of checking every dictionary word to find valid transformations, we generate possible neighboring words and check whether each exists in the set.

### 3. Initialize the BFS Queue

Maintain a queue of pairs:

`(word, steps)`

Here:
- `word` is the current word.
- `steps` is the number of words in the transformation sequence from `startWord` to the current word.

Initially, insert `(startWord, 1)` because the sequence already contains the starting word.

If `targetWord` is absent from the dictionary, return `0` immediately because the target cannot be reached under the problem's rules.

Remove `startWord` from the dictionary if it exists, preventing it from being visited again.

### 4. Generate Neighboring Words

While the queue is not empty:

1. Remove the front element and obtain the current word and its sequence length.
2. If the current word equals `targetWord`, return the current sequence length.
3. For every character position in the current word:
   - Save the original character.
   - Replace it with every lowercase English letter from `'a'` to `'z'`.
   - Check whether the resulting word exists in the dictionary.
   - If it exists, remove it from the dictionary and add `(newWord, steps + 1)` to the queue.
   - Restore the original character before moving to the next position.

Removing a discovered word immediately ensures that it is not visited repeatedly. Since BFS discovers words in increasing order of distance, its first discovery also gives that word a shortest path from the starting word.

### 5. Handle Unreachable Targets

If the queue becomes empty without reaching `targetWord`, no valid transformation sequence exists. Return `0`.

## C++ Solution

```cpp
#include <bits/stdc++.h>
using namespace std;

class Solution {
public:
    int wordLadderLength(string startWord, string targetWord,
                         vector<string>& wordList) {
        unordered_set<string> dictionary(
            wordList.begin(), wordList.end()
        );

        // The target must be present in the dictionary.
        if (dictionary.find(targetWord) == dictionary.end()) {
            return 0;
        }

        // Store each word along with its sequence length.
        queue<pair<string, int>> q;
        q.push({startWord, 1});

        // Mark the starting word as visited.
        dictionary.erase(startWord);

        while (!q.empty()) {
            string word = q.front().first;
            int steps = q.front().second;
            q.pop();

            // The first time we reach the target is optimal.
            if (word == targetWord) {
                return steps;
            }

            // Try changing each character independently.
            for (int i = 0; i < (int)word.size(); i++) {
                char original = word[i];

                for (char ch = 'a'; ch <= 'z'; ch++) {
                    word[i] = ch;

                    auto it = dictionary.find(word);

                    if (it != dictionary.end()) {
                        // Mark visited when adding to the queue.
                        dictionary.erase(it);
                        q.push({word, steps + 1});
                    }
                }

                // Restore the original character.
                word[i] = original;
            }
        }

        // The target cannot be reached.
        return 0;
    }
};
```

## Correctness Proof

We prove that the algorithm returns the length of the shortest valid transformation sequence, or `0` if no such sequence exists.

**1. Every generated transition is valid.**

The algorithm changes exactly one character of the current word and accepts the resulting word only if it belongs to the dictionary. Therefore, every transition follows the transformation rules.

**2. Every reachable dictionary neighbor is considered.**

For each character position, the algorithm tries every lowercase English letter. Hence, every dictionary word differing from the current word by exactly one character can be generated and discovered.

**3. Each word is processed at most once.**

Whenever a dictionary word is discovered, it is immediately removed from the hash set. Consequently, it cannot be added to the queue again. This prevents redundant processing.

**4. BFS discovers words in shortest-distance order.**

The queue processes all words at one transformation distance before processing words at a greater distance. Thus, when a word is first discovered, the path used to reach it has the minimum possible number of transformations.

**5. The returned answer is optimal.**

The starting word is assigned a sequence length of `1`. Each valid transition increases the length by one. Therefore, when BFS first reaches `targetWord`, the returned value is the minimum number of words in any valid sequence, including both endpoints.

If the queue empties without reaching the target, no valid sequence exists, and returning `0` is correct.

Hence, the algorithm is correct.

## Complexity Analysis

Let:

- `N` be the number of words in the dictionary.
- `L` be the length of each word.
- `K` be the size of the character set; here, `K = 26`.

### Time Complexity

For each word processed by BFS, the algorithm considers `L` character positions and tries `K` possible characters at each position. Each generated word requires up to \(O(L)\) time to construct or hash.

Therefore, the expected time complexity is:

\[
\boxed{O(NKL)}
\]

This simplifies to \(O(26NL)\), or \(O(NL)\) when treating the alphabet size as a constant.

Building the initial hash set takes expected \(O(NL)\) time.

### Auxiliary Space Complexity

The hash set stores up to \(N\) words, and the queue can contain up to \(N\) words. Each stored word has length \(L\).

Therefore, the auxiliary space complexity is:

\[
\boxed{O(NL)}
\]

The solution uses an iterative BFS and does not require recursive stack space.

## Important Edge Cases

1. **Target absent from the dictionary:** Return `0`.
2. **Starting word equals target word:** If the target is present in the dictionary, return `1`.
3. **No valid transformation exists:** Return `0` after BFS exhausts the queue.
4. **Direct transformation:** If the target differs from the starting word by one character and is in the dictionary, return `2`.
5. **Starting word appears in the dictionary:** It is removed from the set during initialization to prevent revisiting it.
6. **Duplicate dictionary entries:** The hash set automatically removes duplicates.
7. **Multiple shortest sequences:** BFS may discover any one of them first, but all have the same minimum length.

## Implementation Notes

- The implementation assumes all words consist of lowercase English letters and have equal lengths.
- The starting word is counted as the first word, so the initial BFS distance is `1`, not `0`.
- The target must be present in the dictionary for the transformation to succeed.
- The implementation is contained in `Solution/solution.cpp`; its input format is documented in `Problem.md`.
