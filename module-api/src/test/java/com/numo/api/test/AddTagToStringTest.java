package com.numo.api.test;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddTagToStringTest {
    @Test
    void test1() {
        List<String> words = List.of("running", "park", "early", "crisp", "morning", "trees", "running-h");

        String sentence = "running She was running-h through the park early in the morning, running enjoying the crisp air and the sound of birds chirping, as the first rays of sunlight began to break through the trees. running";

        String s = addTag(words, sentence);
        System.out.println(s);
    }

    String addTag(List<String> words, String sentence) {
        String result = sentence;

        for (String word : words) {
            String regex = "(?<!-)\\b" + Pattern.quote(word) + "\\b(?!-)";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(result);

            result = matcher.replaceAll("<strong>" + word + "</strong>");
        }

        return result;
    }
}
