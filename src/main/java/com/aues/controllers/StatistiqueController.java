package com.aues.controllers;

import com.aues.services.StatistiqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/** Endpoints du tableau de bord (chiffres clés de l'AUES). */
@RestController
@RequestMapping("/statistiques")
public class StatistiqueController {

    @Autowired
    private StatistiqueService statistiqueService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> statistiques() {
        return ResponseEntity.ok(statistiqueService.statistiques());
    }
}
