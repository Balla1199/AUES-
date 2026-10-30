package com.aues.controllers;

import com.aues.services.StatistiqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/** Statistiques du tableau de bord (recettes, impayés, consommation…). */
@RestController
public class StatistiqueController {

    private final StatistiqueService statistiqueService;

    @Autowired
    StatistiqueController(StatistiqueService statistiqueService) {
        this.statistiqueService = statistiqueService;
    }

    @GetMapping("/statistiques")
    public Map<String, Object> statistiques() {
        return statistiqueService.statistiques();
    }
}
