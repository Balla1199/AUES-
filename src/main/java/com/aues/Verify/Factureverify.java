package com.aues.Verify;

import com.aues.DTO.FactureDto;
import io.micrometer.common.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class Factureverify {

    public static List<String> verify(FactureDto factureDto) {
        List<String> errors = new ArrayList<>();

        if (factureDto == null) {
            errors.add("Veuillez renseigner les informations de la facture");
        } else {
            if (factureDto.getMontant() == null
                    || factureDto.getMontant().compareTo(java.math.BigDecimal.ZERO) <= 0) {
                errors.add("Veuillez donner un montant strictement positif");
            }
        }
        return errors;
    }
}
