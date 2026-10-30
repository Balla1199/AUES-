package com.aues.DTO;

import com.aues.entites.Payement;
import com.aues.entites.StatutPay;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class PayementDto {

    private Integer id;
    private String date;
    private BigDecimal montant;
    private String mode;          // nom de l'enum ModePayement
    private StatutPay statutPay;
    private Integer idFact;
    private String payeur;        // nom du payeur (facultatif)

    public static PayementDto Collects(Payement payement) {
        if (payement == null) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        PayementDto dto = new PayementDto();
        dto.setId(payement.getId());
        if (payement.getCreateDate() != null) {
            dto.setDate(LocalDateTime.ofInstant(payement.getCreateDate(), ZoneId.systemDefault())
                    .withNano(0).format(formatter));
        }
        dto.setMontant(payement.getMontant());
        dto.setMode(payement.getMode() != null ? payement.getMode().name() : null);
        dto.setStatutPay(payement.getStatutPay());
        dto.setIdFact(payement.getFacture() != null ? payement.getFacture().getId() : null);
        dto.setPayeur(payement.getPayeur());
        return dto;
    }

    public static Payement Register(PayementDto dto) {
        if (dto == null) {
            return null;
        }
        Payement payement = new Payement();
        payement.setId(dto.getId());
        payement.setMontant(dto.getMontant());
        return payement;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public Integer getIdFact() {
        return idFact;
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

    public String getPayeur() {
        return payeur;
    }

    public void setPayeur(String payeur) {
        this.payeur = payeur;
    }
}
