package com.aues.Verify;

import com.aues.DTO.PayementDto;
import io.micrometer.common.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class PayementVerify {

    public  static List<String> verify(PayementDto payementDto){
        List<String> error = new ArrayList<>();

        if (payementDto==null){
            error.add("Veuillez renseigner les champs du payement");
        } else {
            if (StringUtils.isEmpty(String.valueOf(payementDto.getMontant())))
                error.add("Veuillez donner le montant");
        }
        return error;
    }
}
