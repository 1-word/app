package com.numo.wordapp.service;

import com.numo.wordapp.dto.SynonymDto;

import java.util.List;

public interface SynonymService {
    String updateBySynonym(List<SynonymDto.Request> synonymDto);
}
