import java.io.*;
import java.nio.file.*;
import java.util.*;

public class gen {

    static class TestCase {
        String startWord;
        String targetWord;
        List<String> wordList;

        TestCase(String startWord, String targetWord,
                 List<String> wordList) {
            this.startWord = startWord;
            this.targetWord = targetWord;
            this.wordList = wordList;
        }
    }

    static int wordLadderLength(String startWord,
                                String targetWord,
                                List<String> wordList) {

        Set<String> dictionary = new HashSet<>(wordList);

        if (!dictionary.contains(targetWord)) {
            return 0;
        }

        Queue<String> queue = new LinkedList<>();
        Map<String, Integer> distance = new HashMap<>();

        queue.offer(startWord);
        distance.put(startWord, 1);
        dictionary.remove(startWord);

        while (!queue.isEmpty()) {
            String word = queue.poll();
            int steps = distance.get(word);

            if (word.equals(targetWord)) {
                return steps;
            }

            char[] chars = word.toCharArray();

            for (int i = 0; i < chars.length; i++) {
                char original = chars[i];

                for (char ch = 'a'; ch <= 'z'; ch++) {
                    if (ch == original) {
                        continue;
                    }

                    chars[i] = ch;
                    String next = new String(chars);

                    if (dictionary.remove(next)) {
                        distance.put(next, steps + 1);
                        queue.offer(next);
                    }
                }

                chars[i] = original;
            }
        }

        return 0;
    }

    static String changeOneCharacter(String word,
                                     int position,
                                     char replacement) {
        char[] chars = word.toCharArray();
        chars[position] = replacement;
        return new String(chars);
    }

    static TestCase generateTestCase(int testNumber,
                                     Random random) {

        int length = 3 + random.nextInt(3);
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < length; i++) {
            builder.append((char) ('a' + random.nextInt(6)));
        }

        String startWord = builder.toString();

        // Generate a valid transformation chain.
        List<String> chain = new ArrayList<>();
        Set<String> used = new HashSet<>();

        chain.add(startWord);
        used.add(startWord);

        String current = startWord;

        int chainLength = 1 + random.nextInt(5);

        for (int step = 0; step < chainLength; step++) {
            char[] chars = current.toCharArray();
            boolean generated = false;

            for (int attempt = 0; attempt < 100; attempt++) {
                int position = random.nextInt(length);
                char replacement =
                    (char) ('a' + random.nextInt(6));

                if (replacement == chars[position]) {
                    continue;
                }

                char old = chars[position];
                chars[position] = replacement;
                String candidate = new String(chars);

                if (!used.contains(candidate)) {
                    current = candidate;
                    chain.add(current);
                    used.add(current);
                    generated = true;
                    break;
                }

                chars[position] = old;
            }

            if (!generated) {
                break;
            }
        }

        String targetWord = chain.get(chain.size() - 1);
        List<String> wordList = new ArrayList<>();

        // Include chain words, optionally excluding the target.
        boolean includeTarget = testNumber % 4 != 0;

        for (int i = 1; i < chain.size(); i++) {
            if (i == chain.size() - 1 && !includeTarget) {
                continue;
            }

            wordList.add(chain.get(i));
        }

        // Occasionally include the start word in the dictionary.
        if (testNumber % 3 == 0) {
            wordList.add(startWord);
        }

        // Add valid-length decoy words without duplicates.
        int extraWords = random.nextInt(8);

        while (extraWords > 0) {
            StringBuilder decoy = new StringBuilder();

            for (int i = 0; i < length; i++) {
                decoy.append(
                    (char) ('a' + random.nextInt(6))
                );
            }

            String word = decoy.toString();

            if (used.add(word)) {
                wordList.add(word);
                extraWords--;
            }
        }

        // Ensure at least one dictionary word exists.
        if (wordList.isEmpty()) {
            wordList.add(targetWord);
        }

        Collections.shuffle(wordList, random);

        return new TestCase(startWord, targetWord, wordList);
    }

    static void writeTestCase(TestCase testCase,
                              int testNumber,
                              Path inputDir,
                              Path outputDir) throws IOException {

        StringBuilder input = new StringBuilder();

        input.append(testCase.startWord)
             .append(" ")
             .append(testCase.targetWord)
             .append("\n");

        input.append(testCase.wordList.size()).append("\n");

        for (int i = 0; i < testCase.wordList.size(); i++) {
            if (i > 0) {
                input.append(" ");
            }

            input.append(testCase.wordList.get(i));
        }

        input.append("\n");

        int answer = wordLadderLength(
            testCase.startWord,
            testCase.targetWord,
            testCase.wordList
        );

        Files.writeString(
            inputDir.resolve(testNumber + ".txt"),
            input.toString()
        );

        Files.writeString(
            outputDir.resolve(testNumber + ".txt"),
            answer + "\n"
        );
    }

    public static void main(String[] args) throws IOException {

        String baseDir = "Testcases";

        if (!Files.exists(Paths.get(baseDir))
                && Files.exists(Paths.get("../Testcases"))) {
            baseDir = "../Testcases";
        }

        Path inputDir = Paths.get(baseDir, "input");
        Path outputDir = Paths.get(baseDir, "output");

        Files.createDirectories(inputDir);
        Files.createDirectories(outputDir);

        Random random = new Random(20261025);

        for (int i = 1; i <= 101; i++) {
            TestCase testCase = generateTestCase(i, random);

            writeTestCase(
                testCase,
                i,
                inputDir,
                outputDir
            );
        }

        System.out.println(
            "Successfully generated 101 test cases."
        );
        System.out.println(
            "Input directory: " + inputDir.toAbsolutePath()
        );
        System.out.println(
            "Output directory: " + outputDir.toAbsolutePath()
        );
    }
}