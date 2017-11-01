package ca.csf.mobile1.tp2.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class SubstitutionCypherKeyMixin extends SubstitutionCypherKey {

    @JsonCreator
    public SubstitutionCypherKeyMixin(@JsonProperty("key") int key, @JsonProperty("inputCharacters") char[] inputCharacters, @JsonProperty("outputCharacters")char[] outputCharacters) {
        super(key, inputCharacters, outputCharacters);
    }

    @JsonProperty("key")
    public abstract int getKey();

    @JsonProperty("key")
    public abstract void setKey(int key);

    @JsonProperty("inputCharacters")
    public abstract char[] getInputCharacters();

    @JsonProperty("outputCharacters")
    public abstract char[] getOutputCharacters();
}
