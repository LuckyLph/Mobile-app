package ca.csf.mobile1.tp2.services;

import ca.csf.mobile1.tp2.model.SubstitutionCypherKey;

public interface OnKeyFoundListener {
    public void onKeyFound(SubstitutionCypherKey SubstitutionCypherKey, AsyncTaskCallType type);

    public void onExceptionThrown(ErrorType errorType);
}
