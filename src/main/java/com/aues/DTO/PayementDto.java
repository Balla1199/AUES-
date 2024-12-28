package com.aues.DTO;

import com.aues.entites.ModePayement;
import com.aues.entites.Payement;
import com.aues.entites.StatutPay;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class PayementDto {

    private Integer id;
    private String date;
    private Integer montant;
    private ModePayement mode;
    private StatutPay statutPay;
    private Integer idFact;

    public static PayementDto Collects(Payement payement){
        LocalDateTime localDateTime = LocalDateTime.ofInstant(payement.getCreateDate(), ZoneId.systemDefault()).withNano(0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formatedDate = localDateTime.format(formatter);
        if (payement!=null){
            PayementDto payementDto = new PayementDto();
            payementDto.setId(payement.getId());
            payementDto.setDate(formatedDate);
            payementDto.setMode(payement.getMode());
            payementDto.setMontant(payement.getMontant());
            payementDto.setStatutPay(payement.getStatutPay());
            payementDto.setIdFact(payement.getFacture().getId());

            return payementDto;
        } return null;
    }

    public static Payement Register(PayementDto dto){

        Payement payement = new Payement();
        if (dto!=null){
            payement.setId(dto.getId());
            payement.setMode(dto.getMode());
            payement.setMontant(dto.getMontant());
            payement.setStatutPay(dto.getStatutPay());
            return payement;
        } return null;
    }

    public Integer getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public Integer getMontant() {
        return montant;
    }

    public ModePayement getMode() {
        return mode;
    }

    public Integer getIdFact() {
        return idFact;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setMontant(Integer montant) {
        this.montant = montant;
    }

    public void setMode(ModePayement mode) {
        this.mode = mode;
    }

    public void setIdFact(Integer idFact) {
        this.idFact = idFact;
    }

    public StatutPay getStatutPay() {
        return statutPay;
    }

    public void setStatutPay(StatutPay statutPay) {
        this.statutPay = statutPay;
    }
}
