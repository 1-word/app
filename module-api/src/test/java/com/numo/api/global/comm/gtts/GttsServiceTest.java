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

        Gtts gtts = new Gtts("color", "en", "/usr/src/app/data/sound/color.mp3");
        Gtts gtts1 = new Gtts("색, 색상", "ko", "/usr/src/app/data/sound/color_mean.mp3");

        gttsService.saveAudio(gtts);

        gttsService.saveAudio(List.of(gtts, gtts1));
    }

}