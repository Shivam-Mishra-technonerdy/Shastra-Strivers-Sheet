#include <bits/stdc++.h>
using namespace std;

class Solution {
public:
    int wordLadderLength(string startWord, string targetWord,
        vector<string>& wordList) {
        unordered_set<string> dictionary(
            wordList.begin(), wordList.end()
        );

        if (dictionary.find(targetWord) == dictionary.end()) {
            return 0;
        }

        queue<pair<string, int>> q;
        q.push({startWord, 1});
        dictionary.erase(startWord);

        while (!q.empty()) {
            string word = q.front().first;
            int steps = q.front().second;
            q.pop();

            if (word == targetWord) {
                return steps;
            }

            for (int i = 0; i < (int)word.size(); i++) {
                char original = word[i];

                for (char ch = 'a'; ch <= 'z'; ch++) {
                    word[i] = ch;

                    auto it = dictionary.find(word);

                    if (it != dictionary.end()) {
                        dictionary.erase(it);
                        q.push({word, steps + 1});
                    }
                }

                word[i] = original;
            }
        }

        return 0;
    }
};

int main() {
    string startWord, targetWord;
    cin >> startWord >> targetWord;

    int n;
    cin >> n;

    vector<string> wordList(n);

    for (int i = 0; i < n; i++) {
        cin >> wordList[i];
    }

    Solution sol;
    cout << sol.wordLadderLength(startWord, targetWord, wordList)
         << '\n';

    return 0;
}