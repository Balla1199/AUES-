package com.aues.controllers.Interface;

import com.aues.DTO.PayementDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;

import java.util.List;

@Api("/payement")
public interface PayementCtrlInterface {

    @PostMapping(value = "/payement/save")
    ResponseEntity<PayementDto> SavePaiemnt(@RequestBody PayementDto payementDto);

    @GetMapping(value = "/payement/search/{idp}")
    PayementDto findPaiemnt(@PathVariable("idp")Integer id);

    @GetMapping(value = "/payement/annuler/{ida}")
    PayementDto AnnulerPaiement(@PathVariable("ida")Integer id);

    @GetMapping(value = "/payment/searchs")
    List<PayementDto> findAllPaiemnt();


}
