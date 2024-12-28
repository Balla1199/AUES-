package com.aues.services;

import com.aues.DTO.FactureDto;
import com.aues.Verify.Factureverify;
import com.aues.entites.Facture;
import com.aues.repositories.FactureRepository;
import com.aues.repositories.PayementRepository;
import com.aues.services.Interface.FactureInterf;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FactureService implements FactureInterf {

    private FactureRepository factureRepository;
    private PayementRepository payementRepository;

    public FactureService(FactureRepository factureRepository1, PayementRepository payementRepository1){
        this.factureRepository=factureRepository1;
        this.payementRepository=payementRepository1;
    }


    @Override
    public FactureDto save(FactureDto factureDto) {
        List<String> errors = Factureverify.verify(factureDto);
        if (!errors.isEmpty()){
            throw new IllegalArgumentException("Erreur de validation : " + String.join(", ", errors));
        }

        Facture facturesave = factureRepository.save(FactureDto.Register(factureDto));

        return FactureDto.Collects(facturesave);
    }

    @Override
    public FactureDto findByid(Integer id) {
        if(id==null){ return null;}
        Optional<Facture> facture = factureRepository.findById(id);
        return Optional.ofNullable(FactureDto.Collects(facture.get())).orElseThrow(() ->
                new RuntimeException("Cette facture n'est pas dans la base."));
    }

    @Override
    public List<FactureDto> findAll() {
        return factureRepository.findAll().stream()
                .map(FactureDto::Collects)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        if (id!=null){
            factureRepository.deleteById(id);
        }

    }
}
