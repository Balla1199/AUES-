package com.aues.controllers.Interface;

import com.aues.DTO.FactureDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;

import java.util.List;

@Api("/payement")
public interface FactureCtrlInterface {

    @PostMapping(value = "/facture/save")
    ResponseEntity<FactureDto> Savefacture(@RequestBody FactureDto factureDto);

    @GetMapping(value = "/facture/search/{idfact}")
    FactureDto findFact (@PathVariable("idfact") Integer id);

    @GetMapping(value = "/facture/")
    List<FactureDto> findAllfacture();

    @DeleteMapping(value = "/facture/deletes/{ide}")
    void deleteFacture(@PathVariable("ide") Integer id);
}
