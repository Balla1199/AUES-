package com.aues.controllers;

import com.aues.DTO.PayementDto;
import com.aues.controllers.Interface.PayementCtrlInterface;
import com.aues.services.PayementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PayementController implements PayementCtrlInterface {
    @Autowired
    private PayementService service;


    @Override
    public ResponseEntity<PayementDto> SavePaiemnt(PayementDto payementDto) {
        return ResponseEntity.ok(service.save(payementDto));
    }

    @Override
    public PayementDto findPaiemnt(Integer id) {
        return service.findByid(id);
    }

    @Override
    public PayementDto AnnulerPaiement(Integer id) {
        return service.AnnulerByid(id);
    }

    @Override
    public List<PayementDto> findAllPaiemnt() {
        return service.findAll();
    }

}
