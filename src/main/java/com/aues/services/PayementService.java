package com.aues.services;

import com.aues.DTO.PayementDto;
import com.aues.Verify.PayementVerify;
import com.aues.entites.*;
import com.aues.repositories.FactureRepository;
import com.aues.repositories.PayementRepository;
import com.aues.services.Interface.PayementInterf;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PayementService implements PayementInterf {

    private FactureRepository factureRepository;
    private PayementRepository payementRepository;

    public PayementService (PayementRepository payementRepository1, FactureRepository factureRepository1)
    { this.payementRepository=payementRepository1;
        this.factureRepository=factureRepository1;
    }


    @Override
    public PayementDto save(PayementDto payementDto) {
        List<String> errors = PayementVerify.verify(payementDto);
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException("Erreur de validation : " + String.join(", ", errors));
        }
        Long sumTotal = 0L;
        Long reste = 0L;
        Optional<Facture> optionalFacture = factureRepository.findById(payementDto.getIdFact());
        if (optionalFacture.isEmpty()) {
            throw new RuntimeException("Facture non trouvée pour l'ID spécifié.");
        }

        Facture facture = optionalFacture.get();
        sumTotal = facture.getMontantTotal();

        Payement payement = PayementDto.Register(payementDto);
        payement.setFacture(facture);
        payement.setStatutPay(StatutPay.effectuer);
        Payement savePayement = payementRepository.save(payement);

        if (payementDto.getMode() == ModePayement.physique) {
            if (payementDto.getMontant() >= sumTotal) {
                // Paiement complet
                facture.setMontantTotal(0L);
                facture.setStatut(StatutFact.payer);
            } else {
                // Paiement partiel
                reste = sumTotal - payementDto.getMontant();
                facture.setMontantTotal(reste);
                facture.setStatut(StatutFact.nopayer);
            }

            facture.setPayementlist(savePayement.getFacture().getPayementlist());
            factureRepository.save(facture);
        }

        return PayementDto.Collects(savePayement);

    }

    @Override
    public PayementDto findByid(Integer id) {
        if (id==null){
            throw new RuntimeException("Paiement non trouvé pour l'ID spécifié.");
        }
        Optional<Payement> payement = payementRepository.findById(id);
        return Optional.of(PayementDto.Collects(payement.get())).orElseThrow(() ->
                new RuntimeException("Ce paye payement n'est pas dans la base"));
    }

    @Override
    public PayementDto AnnulerByid(Integer id) {
        if (id==null){
            throw new RuntimeException("Paiement non trouvé pour l'ID spécifié.");
        }
        if (!payementRepository.findById(id).isPresent()){ return null;}
        Optional<Payement> payement = payementRepository.findById(id);
        Facture facture = payement.get().getFacture();
        facture.setMontantTotal(facture.getMontantTotal()+payement.get().getMontant());
        facture.setStatut(StatutFact.nopayer);

        Payement payementAnnuler = new Payement();
        payementAnnuler.setMontant(payement.get().getMontant());
        payementAnnuler.setFacture(facture);
        payementAnnuler.setMode(ModePayement.physique);
        payementAnnuler.setStatutPay(StatutPay.annuler);

        factureRepository.save(facture);
        return PayementDto.Collects(payementRepository.save(payementAnnuler));
    }

    @Override
    public List<PayementDto> findAll() {
        return payementRepository.findAll().stream()
                .map(PayementDto::Collects)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        if (id==null){
            throw new RuntimeException("Paiement non trouvé pour l'ID spécifié.");
        }
        payementRepository.deleteById(id);
    }
}
