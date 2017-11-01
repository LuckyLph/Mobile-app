package ca.csf.mobile1.tp2.model;

/**
 * Classe qui représente la logique de décryption et d'encryption nécessaire à l'activité
 */
public class SubstitutionCypherKeyCrypter {

    /**
     * Décrypte le message en utlisant l'objet SubstitutionKeyMapper prit en paramètre et le retourne sous forme de String
     * @param messageToDecrypt Message à décrypter sous forme de String
     * @param substitutionMap Objet de type SubstitutionKeyMapper qui sert à décrypter le message
     * @return Message décrypté sous forme  de String
     */
    public String decryptMessage(String messageToDecrypt, SubstitutionKeyMapper substitutionMap){

        String decryptedMessage = "";
        for(int i = 0; i < messageToDecrypt.length(); i++){
            decryptedMessage += substitutionMap.get(messageToDecrypt.charAt(i));
        }
        return decryptedMessage;
    }

    /**
     * Encrypte le message en utlisant l'objet SubstitutionKeyMapper prit en paramètre et le retourne sous forme de String
     * @param messageToEncrypt  Message à encrypter sous forme de String
     * @param substitutionMap Objet de type SubstitutionKeyMapper qui sert à encrypter le message
     * @return Message décrypté sous forme  de String
     */
    public String encryptMessage(String messageToEncrypt, SubstitutionKeyMapper substitutionMap){

        String encryptedMessage = "";
        for(int i = 0; i < messageToEncrypt.length(); i++){
            encryptedMessage += substitutionMap.get(messageToEncrypt.charAt(i));
        }
        return encryptedMessage;
    }
}
