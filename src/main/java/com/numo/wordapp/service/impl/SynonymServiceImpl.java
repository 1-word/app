package com.numo.wordapp.service.impl;

import com.numo.wordapp.dto.SynonymDto;
import com.numo.wordapp.model.Synonym;
import com.numo.wordapp.repository.SynonymRepository;
import com.numo.wordapp.service.SynonymService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SynonymServiceImpl implements SynonymService {
    @Autowired
    private SynonymRepository synonymRepository;

    @Override
    public String updateBySynonym(List<SynonymDto.Request> synonymDto){
        for (SynonymDto.Request synonym : synonymDto){
            Optional<Synonym> synonymUpdate = synonymRepository.findById(synonym.getSynonym_id());
            synonymUpdate.ifPresent(updateSynonym->{
                updateSynonym.setSynonym(synonym.getSynonym());
                updateSynonym.setMemo(synonym.getMemo());
                synonymRepository.save(updateSynonym);
            });

        }
        System.out.println("업데이트 완료");
        return "업데이트 완료";
    }
}
