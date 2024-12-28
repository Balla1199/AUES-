package com.aues.controllers;

import com.aues.DTO.FactureDto;
import com.aues.controllers.Interface.FactureCtrlInterface;
import com.aues.services.FactureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FactureController implements FactureCtrlInterface {

    private FactureService factureService;

    @Autowired
    FactureController(FactureService factureService1){
        this.factureService=factureService1;
    }

    @Override
    public ResponseEntity<FactureDto> Savefacture(FactureDto factureDto) {
        return ResponseEntity.ok(factureService.save(factureDto));
    }

    @Override
    public FactureDto findFact(Integer id) {
        return factureService.findByid(id);
    }

    @Override
    public List<FactureDto> findAllfacture() {
        return factureService.findAll();
    }

    @Override
    public void deleteFacture(Integer id) {
        factureService.delete(id);
    }
}
