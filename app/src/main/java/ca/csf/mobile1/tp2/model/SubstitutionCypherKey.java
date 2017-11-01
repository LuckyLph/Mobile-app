package ca.csf.mobile1.tp2.model;

/**
 * Classe qui représente la clé de substitution avec sa valeur ainsi que le tableau d'entréé et de sortie afin d'effectuer la substitution
 */
public class SubstitutionCypherKey {

    private int key;
    private char[] inputCharacters;
    private char[] outputCharacters;

    /**
     * Constructeur de la classe SubstitutionKey
     * @param key Clé de substitution sous forme de intconnard
     * @param inputCharacters Charactères à substituer sous forme de tableau de char
     * @param outputCharacters Charactères substitué sous forme de tableau de char
     */
    public SubstitutionCypherKey(int key, char[] inputCharacters, char[] outputCharacters){
        this.key = key;
        this.inputCharacters = inputCharacters;
        this.outputCharacters = outputCharacters;
    }

    /**
     * Accesseur de la propriété key
     * @return Clé de substitution sous forme de int
     */
    public int getKey(){ return key; }

    /**
     * Accesseur de la propriété key
     * @param key Clé de substitution sous forme de int
     */
    public void setKey(int key){
        this.key = key;
    }

    /**
     * Accesseur de la propriété inputCharacters
     * @return Charactères à substituer sous forme de tableau de char
     */
    public char[] getInputCharacters(){ return inputCharacters; }

    /**
     * Accesseur de la propriété outputCharacters
     * @return Charactères substitué sous forme de tableau de char
     */
    public char[] getOutputCharacters() { return outputCharacters; }

}
