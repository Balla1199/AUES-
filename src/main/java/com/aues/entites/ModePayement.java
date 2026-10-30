package com.aues.entites;

/**
 * Modes de paiement acceptés par l'AUES.
 * (anciens codes de la brancheérieure : physique -> Especes, mobile -> MobileMoney)
 */
public enum ModePayement {
    Especes,
    MobileMoney,
    Virement,
    Cheque;
}
