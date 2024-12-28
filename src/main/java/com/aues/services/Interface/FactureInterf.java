package com.aues.services.Interface;

import com.aues.DTO.FactureDto;

import java.util.List;

public interface FactureInterf {


    FactureDto save(FactureDto factureDto);
    FactureDto findByid(Integer id);
    List<FactureDto> findAll();
    void delete(Integer id);
}
