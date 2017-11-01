package ca.csf.mobile1.tp2.services;

/**
 * enum qui représente les types d'appel réseau qui peuvent être fait avec la tâche synchrone
 */
public enum AsyncTaskCallType {
    Startup,                                //Est passé au démarrage de l'activité afin que l'application n'effectue pas de cryptage inutile
    Decrypting,                             //Est passé lorsque l'application effectue une décryption
    Encrypting,                             //Est passé lorsque l'application effectue une encryption
}
