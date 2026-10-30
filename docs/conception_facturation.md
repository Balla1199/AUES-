# Conception — Module facturation / paiements (AUES)

Comparaison des deux implémentation et conception retenue pour la fusion.

- Branche A : `origin/kariba` (Namoussa) — conception orientée DTO/Interfaces
- Branche B : `kariba-facturation-v2` (règles métier + statistiques + vues web)

---

## 1. Ce qui est bien dans chaque conception

### Branche A — points à conserver ✅

| Élément | Pourquoi c'est bien |
|---|---|
| `EntiteAbstrait` (`@MappedSuperclass`) | Toutes les entités héritent : id commun, `createDate`, `modifieDate`. Conception propre et évolutive. |
| Couche `DTO` (`FactureDto`, `PayementDto`…) | Sépare l'API JPA de l'API REST : l'entité ne fuit jamais côté client. |
| Classes `Verify` (`Factureverify`, `PayementVerify`) | Validation en listes d'erreurs agrégées, plutôt qu'une exception à la première erreur : meilleur retour au client. |
| Interfaces de service (`FactureInterf`, `PayementInterf`) | Contrat explicite, pratique pour tester et pour dupliquer des implémentations. |
| `AdminInitializationService` (@PostConstruct) | Admin par défaut créé au démarrage — utile en déploiement (cf. erreur Java 1507 : supprimer les réflexions de AuthController mort dans AuthController.java). |
| `Compteur → Utilisateur` (@ManyToOne) | La facture doit connaître le **client** : c'est le lien métier central manquant dans la branche B. |
| `Payement.statutPay` (effectuer/annuler) | Modélise l'**annulation de paiement** sans supprimer la trace — indispensable pour l'audit financier. |

### Branche B — point à conserver ✅

| Élément | Pourquoi c'est bien |
|---|---|
| `Facture.montant_paye` cumulatif | Conserve **le montant d'origine** (branche A décrémente `montantTotal` jusqu'à 0 : le total facturé original est perdu — impossible de calculer un taux de recouvrement). |
| `Facture.numero` lisible (`FACT-CPT001-42`) | Référence imprimable sur la facture papier (usage terrain). |
| Règles de paiement | Montant nul refusé, facture déjà payée refusée, **paiement excédentaire refusé** (branche A acepte un paiement supérieur au reste). |
| `StatistiqueService` | Tableau de bord : recettes, impayés, consommation, taux de récupération. |
| Vues Thymeleaf `/web/**` | Interface pour les agents sans client REST. |

## 2. Défauts détectés à corriger dans la fusion

### Branch A
1. **`montantTotal` décrémenté** vers 0 → la valeur facturée d'origine est perdue. La conception doit échange un volant `montant` (immuable) + `montantPaye` (cumulatif).
2. **Seul le mode `physique` déclenche la mise à jour de la facture** (cf. `ModePayement.physique/mobile`) : un paiement `mobile` est enregistré sans modifier la facture ni son statut (bug sûr).
2bis. `FactureRepository` étend `JpaRepository` (branch A) alors que le reste du projet utilise `CrudRepository` (branch B) : unifier avec `CrudRepository` ou migrer tout vers `JpaRepository` — choisir un seul standard.
3. `facture.setPayementlist(savePayement.getFacture()…)` est une référence fausse — utilisée duinverse problème.
4. `@CreatedDate/@LastModifiedDate` inactifs : `@EnableJpaAuditing` n'est pas activé sur l'application, l'audit ne pas fonctionnera.
5. `StatutFact.payer / nopayer` : minuscules + présentation du statut ("nopayer") — à jouer en `Payee / Impayee`.
6. `AuthController` reste entièrement commenté et fenêtre `JwtUtil` dans repositories — code mort à supprimer.

### Branche B
1. Le champ `montant` en `Double` sur des montants FCFA : préférer un type entier-millimeter (`BigDecimal` au moins). Cf. décision ci-desous.
2. Pas de lien `Compteur → Utilisateur` : la facture ne peut pas être deregistrée par client.
3. Pas d'annulation de paiement (branche A le fait).

## 3. Conception retenue (fusion)

### Modèle de données

```
Utilisateur (client / admin)
   └── 1:N  Compteur  (compteur → utilisateur)      [branch A]
              └── 1:N   Releve
                            └── 1:1 → Facture    [une seule par relevé]
                                          ├── 1:N → Payement (effectuer / annuler) [branch A]
                                          └── montant (immuable) + montantPaye (cumulatif) [branch B]
```

- **`Facture`** : `numero` (lisible), `montant` (immuable, `BigDecimal`),
  `montantPaye` (cumulatif), `statut` calculé *jamais stocké en dur nu* (`Payee/Impayee`,
  recalculé par `recalculerStatut()`), `dateEmission`, `dateEcheance` (+30 j),
  lien `releve` (1:1), hérite de `EntiteAbstrait` (audit).
- **`Payement`** : `montant`(`BigDecimal`), `mode`, `statutPay`
  (`Effectuer / Annuler`), lien `facture`, **l'annulation ne supprime pas la ligne**
  : elle ajoute un versement de statut `Annuler` et re-décrémente `montantPaye`.
- **Écarts corriger sur branch A** : règles de la branch B appliquées quel que
  soit le mode de paiement, plus `montant ≤ montantRestant()`.

### Couches

```
Controllers REST (FactureCtrl / PayementCtrl)   ← DTO en entrée/sortie
        ↓ implémente les interfaces (branch A)
Services (règles métier, transactions)         ← Liste<String> errors de Verify
        ↓
Repositories (Spring Data)
        ↓
Entités JPA (héritent EntiteAbstrait)          ← audit activé via @EnableJpaAuditing
```

- Statistiques : service de la branch B (`StatistiqueService`), calcul côté
  Java (volume d'une AUES est compatibles).
- Vues web : branch B, renommées avec les statuts et les notions de reste à
  payer et de calcul.
- Sécurité : JWT (ConfigurationCryptage/JwtService/JwtFilter de la branch B
  + AdminInitializationService de la branch A, authentif login par téléphone).

## 4. Accords de fusion — checklist

> ✅ **Réalisée** sur la branche `kariba-facturation-fusion` (établie depuis
> `origin/kariba`), validée par tests REST + vues web, données de test nettoyées.

1. ~~Branche de fusion créée depuis le code de l'équipe (`origin/kariba`)~~ —
   faite par réécriture ciblée fichier par fichier (branche dédiée, aucune
   autre branche du projet n'est touchée).
2. Résolution : `EntiteAbstrait`, `DTO/`, `Verify/`, interfaces,
   `AdminInitializationService`, lien `Compteur→Utilisateur` (branch A) ;
   `numero`, règle `montantPaye`, `StatistiqueService`, vues `/web/**`
   (branch B) ; `PayementService` réécrit autour du modèle cumulatif.
3. `@EnableJpaAuditing` ajouté sur `AuesApplication`.
4. Enums unifiés : `StatutFact → Payee/Impayee`, `StatutPay → Effectuer/Annuler`,
   `ModePayement → Especes/MobileMoney/Virement/Cheque`
   (l'ancien `physique` correspond à `Especes`, `mobile` à `MobileMoney`).
4bis. Conserver l'enum `TypeNotif` de branch A (payement, releve, rappel,
   confirmation) : hypothèse d'un système de notifications rapprochées — utile pour
   rappels de factures impayées. *(préservé, aucune modification)*
5. Montants migrés `Long → BigDecimal` (colonnes `DECIMAL(10,2)`).
6. Supprimés : `AuthController` (commenté), `repositories/JwtUtil` (doublon des
   classes de sécurité de `securite/`).
7. Cas REST rejoués sur l'application assemblée :
   - génération de facture depuis un relevé (montant = unités × tarif) ✅
   - refus de double facture (rule `existsByReleveId`, endpoint `/facture/generer/{releveId}`) ✅
   - paiement partiel (montantPaye cumulatif, quel que soit le mode : Especes,
     Virement, MobileMoney, Cheque testés) ✅
   - refus de paiement sur facture payée ✅
   - refus de trop-payé ✅
   - annulation d'un versement : trace conservée (`StatutPay.Annule`),
     rester recalculé ✅
   - `/statistiques` : recettes, impayés, taux de recouvrement, par mois ✅
   - vues web `/web/**` : tableau-de-bord, factures, détail + formulaire de
     paiement, payements ; erreurs affichées sans planter ✅

## 5. Corrections apportées au code de l'équipe pendant la fusion

- `ReleveService` : mélange `@AllArgsConstructor/@NoArgsConstructor` + champs
  `@Autowired` + constructeur manuel → Spring injectait un service à champs
  nuls (500 à la création de relevé). Unifié en injection par constructeur.
- `Releve` : `@OneToOne(mappedBy="releve")` accompagné d'un `@JoinColumn`
  contradictoire — le `@JoinColumn` superflu a été retiré.
- `ReleveController`/chemins : l'URL de liste des paiements de l'équipe
  (`/payment/searchs`) est conservée telle quelle (pas de breaking change).
- `application.properties` doublé d'une ligne corrompue en Latin-1, réécrit
  en UTF-8.
