package com.aues.Verify;

import com.aues.DTO.FactureDto;
import io.micrometer.common.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class Factureverify {

    public static List<String> verify(FactureDto factureDto){

        List<String> error = new ArrayList<>();

        if (factureDto==null){
            error.add("Veuillez renseignez les informations de la facture");
        }else {
            if (StringUtils.isEmpty(String.valueOf(factureDto.getMontantTotal())))
                error.add("Veuillez donnez le montant");
        }

        return error;
    }
}
