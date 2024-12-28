package com.aues.services.Interface;

import com.aues.DTO.PayementDto;

import java.util.List;

public interface PayementInterf {

    PayementDto save(PayementDto payementDto);
    PayementDto findByid(Integer id);
    PayementDto AnnulerByid(Integer id);
    List<PayementDto> findAll();
    void delete(Integer id);
}
