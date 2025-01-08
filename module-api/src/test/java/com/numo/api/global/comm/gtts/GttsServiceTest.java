package com.numo.api.global.comm.gtts;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GttsServiceTest {

    @Autowired
    GttsService gttsService;

    @Test
    void saveAudio() {

        Gtts gtts = new Gtts("color", "en", "/Users/hyun/Projects/wordApp/tmp/color.mp3");
        Gtts gtts1 = new Gtts("색, 색상", "ko", "/Users/hyun/Projects/wordApp/tmp/color_mean.mp3");

        gttsService.saveAudio(gtts);

        gttsService.saveAudio(List.of(gtts, gtts1));
    }

}