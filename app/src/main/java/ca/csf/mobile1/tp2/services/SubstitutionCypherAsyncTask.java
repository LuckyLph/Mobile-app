package ca.csf.mobile1.tp2.services;

import android.os.AsyncTask;
import ca.csf.mobile1.tp2.model.SubstitutionCypherKey;
import ca.csf.mobile1.tp2.model.SubstitutionCypherKeyMixin;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class SubstitutionCypherAsyncTask extends AsyncTask<SubstitutionCypherKey, Void, SubstitutionCypherKey> {

    //BEN_CORRECTION : private ?
    AsyncTaskCallType type;
    ErrorType errorStatus;
    OnKeyFoundListener[] listeners;

    public SubstitutionCypherAsyncTask(AsyncTaskCallType type, OnKeyFoundListener[] listeners){
        this.type = type;
        this.listeners = listeners;
    }

    @Override
    protected SubstitutionCypherKey doInBackground(SubstitutionCypherKey... substitutionCypherKeys) {
        SubstitutionCypherKey substitutionCypherKey = substitutionCypherKeys[0];

        OkHttpClient client = new OkHttpClient();
        //BEN_CORRECTION : Constante..
        Request request = new Request.Builder().url("http://cypherkeys-acodebreak.rhcloud.com/substitution/key/" + substitutionCypherKey.getKey() + ".json").build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String json = response.body().string();
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.addMixIn(SubstitutionCypherKey.class, SubstitutionCypherKeyMixin.class);
                substitutionCypherKey = objectMapper.readValue(json, SubstitutionCypherKey.class);
                errorStatus = ErrorType.None;
            }
            else {
                errorStatus = ErrorType.ServerError;
            }

        }
        catch (IOException e){
            e.printStackTrace();
            errorStatus = ErrorType.WifiError;
        }
        return substitutionCypherKey;
    }

    @Override
    protected void onPostExecute(SubstitutionCypherKey substitutionCypherKey) {
        for (int i = 0; i < listeners.length; i++){
            if (errorStatus == ErrorType.None) {
                listeners[i].onKeyFound(substitutionCypherKey, type);
            }
            else {
                listeners[i].onExceptionThrown(errorStatus);
            }
        }
    }
}
