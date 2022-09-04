package com.numo.wordapp.controller;

import com.numo.wordapp.dto.SynonymDto;
import com.numo.wordapp.service.SynonymService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SynonymController {
    SynonymService synonymService;

    public SynonymController(SynonymService synonymService){
        this.synonymService = synonymService;
    }

    @PutMapping(value = "/update")
    public String setUpdateWord(@RequestBody List<SynonymDto.Request> dto){
        String data = synonymService.updateBySynonym(dto);
        return data;
    }
}
