package ca.csf.mobile1.tp2.services;

/**
 * enum qui représente les types d'erreurs qui peuvent être retournés par la tâche asynchrone d'obtention de la clé
 */
public enum ErrorType {
    WifiError,                              //Est lancé lorsqu'il y a une erreur de connection à internet
    ServerError,                            //Est lancé lorsqu'il y a une erreur d'obtention de la clé du serveur
    None,                                   //Est lancé lorsqu'il n'y a pas d'erreur
}
