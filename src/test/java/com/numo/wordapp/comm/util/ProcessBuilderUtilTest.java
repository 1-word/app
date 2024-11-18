package com.numo.wordapp.comm.util;

import org.junit.jupiter.api.Test;

class ProcessBuilderUtilTest {
    @Test
    void gttsStart() {
        String path = "/Users/hyun/Projects/wordApp";
        String text = "weight";
        String type = "en";

        new ProcessBuilderUtil(path, text, type).run();
    }

}