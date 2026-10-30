# PR — Fusion des conceptions de facturation

**Branche :** `kariba-facturation-fusion` → `master`
**Supersède :** `kariba-facturation-v2` (son travail est intégré ici ; la branche pourra être supprimée après relecture)

## Titre suggéré

> Fusion des conceptions de facturation : Facture/Paiement (montant immuable + cumulatif), statistiques et vues web

## Description

Cette branche fusionne les deux conceptions du module facturation élaborées
par l'équipe (facture générée par branche `kariba` à la création du relevé,
paiements « physique/mobile ») et la proposition v2 (numéro de facture,
montant immuable + cumul des versements, règles de paiement tous modes),
sans toucher aux branches de personne (`Namza`, `Sidiki`, `kone`, `master`).

### Modèle retenu
- `Facture` : `numero` lisible (`FACT-{compteur}-{releve}`), `montant`
  **BigDecimal immuable** (= unités × tarif du relevé), `montantPaye`
  **cumulatif**, `statut` **recalculé** (Payee/Impayee), échéance +30 j.
- `Payement` : mode `Especes/MobileMoney/Virement/Cheque`
  (capturer l'ancien `physique` → Especes, `mobile` → MobileMoney),
  statut `Effectuer/Annuler` — **l'annulation garde la trace**.
- Melting-pot des apports de l'équipe conservés : `EntiteAbstrait` (audit
  JPA), `DTO/`, `Verify/`, interfaces de services/contrôleurs,
  `AdminInitializationService`, lien `Compteur→Utilisateur`.

### Règles métier (tous modes de paiement)
1. Une seule facture par relevé (`existsByReleveId`) ;
2. Montant strictement positif ;
3. Refus de paiement sur facture payée ;
4. Refus de tout paiement supérieur au reste à payer (pas de trop-payé) ;
5. Annulation : `StatutPay.Annule` conservé, `montantPaye` recalculé.

### Nouveautés transverses
- `GET /statistiques` : recettes encaissées, montant impayé, taux de
  recouvrement, unités consommées, agrégats par mois.
- Vues web Thymeleaf ouvertes sans JWT (`/web/**`) : tableau-de-bord,
  liste des factures, détail + formulaire de paiement, liste des paiements.
  Les erreurs métier s'affichent sur la page sans planter.

### Corrections du socle de l'équipe (documentées)
- `ReleveService` : injection Spring cassée (champs nuls → 500 à la
  création de relevé), unifiée en injection par constructeur ;
- `Releve` : `@JoinColumn` contradictoire avec `@OneToOne(mappedBy=...)` retiré ;
- `application.properties` : doublon corrompu (Latin-1) réécrit en UTF-8 ;
- suppression du code mort (`AuthController` commenté, `repositories/JwtUtil`).

### Testé (application lancée contre MySQL local, données de test nettoyées)
- relevé (35) sur compteur 125 (val 12, tarif 250,5) → facture `FACT-125-1`
  de 3 757,50 FCFA ✅
- double facture refusée ✅ · trop-payé refusé ✅ · paiement sur facture
  payée refusé ✅
- paiements Especes / Virement (+ MobileMoney / Cheque) et cumul `Payee` ✅
- annulation avec trace, reste recalculé ✅
- `/statistiques` et 5 pages `/web` rendues avec contenu complet ✅
- `mvn compile` : OK (Java 21)

### Document de conception
`docs/conception_facturation.md` — comparaison des deux conceptions,
modèle cible et checklist de fusion coché.
