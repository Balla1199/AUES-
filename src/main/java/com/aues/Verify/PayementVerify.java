package com.aues.Verify;

import com.aues.DTO.PayementDto;
import io.micrometer.common.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PayementVerify {

    public static List<String> verify(PayementDto payementDto) {
        List<String> errors = new ArrayList<>();

        if (payementDto == null) {
            errors.add("Veuillez renseigner les champs du paiement");
        } else {
            if (payementDto.getMontant() == null
                    || payementDto.getMontant().compareTo(BigDecimal.ZERO) <= 0) {
                errors.add("Veuillez donner un montant strictement positif");
            }
            if (payementDto.getIdFact() == null) {
                errors.add("Veuillez indiquer la facture concernée (idFact)");
            }
            if (StringUtils.isEmpty(String.valueOf(payementDto.getMode()))
                    || payementDto.getMode() == null) {
                errors.add("Veuillez indiquer le mode de paiement");
            }
        }
        return errors;
    }
}
