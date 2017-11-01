package ca.csf.mobile1.tp2.model;

import java.util.HashMap;

/**
 * Classe qui représente la logique de cartographie des charactères supportés par l'application
 */
public class SubstitutionKeyMapper {

    private HashMap<Character, Character> substitutionMap;

    /**
     * Construit l'objet et initialise le dictionnaire de charactères
     */
    public SubstitutionKeyMapper(){
        substitutionMap = new HashMap<>();
    }

    //BEN_REVIEW : Ça marche, mais cela aurait été plus facile avec une Map bidirectionelle. La librairie "Guava" en contient une justement.
    //             Voir ici : https://github.com/google/guava/wiki/NewCollectionTypesExplained#bimap
    //
    //             C'est le genre de chose que j'autorise sans problème. Me voir pour l'installation au besoin.
    /**
     * Effectue la cartographie de la clé en remplissant le dictionnaire avec les charactères des tableaux d'entrées et de sorties
     * @param substitutionCypherKey Objet de type SubstitutionCypherKey qui représente la clé de chiffrement avec les tableaux d'entrées et de sorties duquel les
     *                              données du dictionnaire sont tirés.
     * @param isEncrypting Booléen qui indique si la clé passée est utilisée pour la décryption ou l'encryption
     */
    public void mapKey(SubstitutionCypherKey substitutionCypherKey, boolean isEncrypting){
        if (isEncrypting){
            for (int i = 0; i < substitutionCypherKey.getInputCharacters().length; i++){
                substitutionMap.put(substitutionCypherKey.getInputCharacters()[i], substitutionCypherKey.getOutputCharacters()[i]);
            }
        }
        else {
            for (int i = 0; i < substitutionCypherKey.getInputCharacters().length; i++){
                substitutionMap.put(substitutionCypherKey.getOutputCharacters()[i], substitutionCypherKey.getInputCharacters()[i]);
            }
        }
    }

    /**
     * Obtient le charactère qui équivaut à la clé passée en paramètre dans le dictionnaire
     * @param key Clé sous forme de charactère qui représente le charactère qui doit être substitué avec son
     *            équivalent dans le dictionnaire
     * @return Équivalent du charactère prit en paramètre dans le dictionnaire retourné sous forme de charactère
     */
    public char get(char key){
        return substitutionMap.get(key);
    }
}
