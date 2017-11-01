package ca.csf.mobile1.tp2.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Random;

import ca.csf.mobile1.tp2.model.SubstitutionCypherKey;
import ca.csf.mobile1.tp2.model.SubstitutionCypherKeyCrypter;
import ca.csf.mobile1.tp2.model.SubstitutionKeyMapper;
import ca.csf.mobile1.tp2.R;
import ca.csf.mobile1.tp2.services.AsyncTaskCallType;
import ca.csf.mobile1.tp2.services.ErrorType;
import ca.csf.mobile1.tp2.services.OnKeyFoundListener;
import ca.csf.mobile1.tp2.services.SubstitutionCypherAsyncTask;
import ca.csf.mobile1.tp2.view.dialog.KeyPickerDialog;
import ca.csf.mobile1.tp2.view.dialog.OnKeySelectedListener;
import ca.csf.mobile1.tp2.view.input.filter.CharactersInputFilter;

/**
 * Activité qui permet à l'utilisateur d'encrypter et de décrypter un message texte en utilisant un service de cryptage par substitution. L'utilisateur peut choisir
 * sa clé d'encryption et copier le message obtenu dans le clipboard. L'acitvité supporte le changement de rotation et le démarrage par intent explicite
 */
public class MainActivity extends AppCompatActivity implements OnKeyFoundListener, OnKeySelectedListener {

    private final static int MAX_KEY_VALUE = 99999;
    private final static int KEY_LENGTH = 5;

    //Éléments de l'interface
    private EditText inputEditText;
    private ProgressBar progressBar;
    private TextView outputTextView;
    private TextView keyTextView;

    private SubstitutionCypherKey substitutionCypherKey;
    private CharactersInputFilter[] charactersInputFilters;
    private SubstitutionKeyMapper substitutionKeyMapper;
    private SubstitutionCypherKeyCrypter substitutionCypherKeyCrypter;
    Intent intent;
    private boolean isFromIntent;
    private boolean keyHasChanged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputEditText = (EditText) findViewById(R.id.inputEditText);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        outputTextView = (TextView) findViewById(R.id.outputTextView);
        keyTextView = (TextView) findViewById(R.id.keyTextView);

        substitutionCypherKey = new SubstitutionCypherKey(generateRandomKey(), null, null);
        callSubstitutionCypherAsyncTask(AsyncTaskCallType.Startup);
        keyTextView.setText(getResources().getString(R.string.text_key) + Integer.toString(substitutionCypherKey.getKey())); //BEN_REVIEW : String.format aurait été plus sage
        charactersInputFilters = new CharactersInputFilter[1];
        substitutionCypherKeyCrypter = new SubstitutionCypherKeyCrypter();
        substitutionKeyMapper = new SubstitutionKeyMapper();

        intent = getIntent();
        //BEN_CORRECTION : Constante... Quand j'écris dans les diapositives, souvent, j'essaie d'économiser de l'espace. Faut pas juste prendre
        //                 ça et faire comme si c'était parfait.
        if ("text/plain".equals(intent.getType())) {
            isFromIntent = true;
        }
        else {
            charactersInputFilters[0] = new CharactersInputFilter(new char[] {});
            inputEditText.setFilters(charactersInputFilters);
        }

        if (savedInstanceState != null) {
            //BEN_CORRECTION : Constante...et elles sont super importantes en plus celles là !!
            char[] letters = savedInstanceState.getCharArray("input");
            String text = "";
            for (int i = 0; i < letters.length; i++) {
                text += letters[i];
            }
            inputEditText.setText(text);
            //BEN_CORRECTION : Constante...
            letters = savedInstanceState.getCharArray("output");
            text = "";
            for (int i = 0; i < letters.length; i++) {
                text += letters[i];
            }
            outputTextView.setText(text);
            //BEN_CORRECTION : Constante...
            letters = savedInstanceState.getCharArray("keyText");
            text = "";
            for (int i = 0; i < letters.length; i++) {
                text += letters[i];
            }
            keyTextView.setText(text);
            //BEN_CORRECTION : Constante...
            substitutionCypherKey.setKey(savedInstanceState.getInt("key"));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //BEN_CORRECTION : Constante...
        outState.putCharArray("input", inputEditText.getText().toString().toCharArray());
        //BEN_CORRECTION : Constante...
        outState.putCharArray("output", outputTextView.getText().toString().toCharArray());
        //BEN_CORRECTION : Constante...
        outState.putCharArray("keyText", keyTextView.getText().toString().toCharArray());
        //BEN_CORRECTION : Constante...
        outState.putInt("key", substitutionCypherKey.getKey());

        //BEN_CORRECTION : Appel à "super" doit être fait en premier.
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        //BEN_CORRECTION : Constante...
        char[] letters = savedInstanceState.getCharArray("input");
        String text = "";
        for (int i = 0; i < letters.length; i++) {
            text += letters[i];
        }
        inputEditText.setText(text);
        //BEN_CORRECTION : Constante...
        letters = savedInstanceState.getCharArray("output");
        text = "";
        for (int i = 0; i < letters.length; i++) {
            text += letters[i];
        }
        outputTextView.setText(text);
        //BEN_CORRECTION : Constante...
        letters = savedInstanceState.getCharArray("keyText");
        text = "";
        for (int i = 0; i < letters.length; i++) {
            text += letters[i];
        }
        keyTextView.setText(text);
        //BEN_CORRECTION : Constante...
        substitutionCypherKey.setKey(savedInstanceState.getInt("key"));
        //BEN_CORRECTION : Appel à "super" doit être fait en premier.
        super.onRestoreInstanceState(savedInstanceState);
    }

    public void putTextInClipboard(String text) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.setPrimaryClip(ClipData.newPlainText(getResources().getString(R.string.clipboard_encrypted_text), text));
        Snackbar.make(findViewById(R.id.rootView), getResources().getString(R.string.clipboard_encrypted_text),
                Snackbar.LENGTH_LONG).show();
    }

    public void onEncryptButtonClick(View view) {
        if (keyHasChanged) {
            callSubstitutionCypherAsyncTask(AsyncTaskCallType.Encrypting);
        }
        else {
            //BEN_REVIEW : En général, on évite d'appeler les évènements soit même. Mieux vaut avoir une méthode intermédiaire que l'on peut appeler et
            //             que l'évènement va aussi appeler.
            onKeyFound(substitutionCypherKey, AsyncTaskCallType.Encrypting);
        }
    }

    public void onDecryptButtonClick(View view) {
        if (keyHasChanged) {
            callSubstitutionCypherAsyncTask(AsyncTaskCallType.Decrypting);
        }
        else {
            //BEN_REVIEW : En général, on évite d'appeler les évènements soit même. Mieux vaut avoir une méthode intermédiaire que l'on peut appeler et
            //             que l'évènement va aussi appeler.
            onKeyFound(substitutionCypherKey, AsyncTaskCallType.Decrypting);
        }
    }

    public void onFloatingActionButtonClick(View view) {
        showKeyPickerDialog();
    }

    public void onCopyButtonClick(View view) {
        putTextInClipboard(outputTextView.getText().toString());
    }

    @Override
    public void onKeySelected(int key) {
        substitutionCypherKey.setKey(key);
        keyTextView.setText(getResources().getString(R.string.text_key) + Integer.toString(key));
        keyHasChanged = true;
        if (isFromIntent) {
            callSubstitutionCypherAsyncTask(AsyncTaskCallType.Decrypting);
        }
    }

    @Override
    public void onKeySelectionCancelled() {
        if (isFromIntent) {
            this.finish();
        }
    }

    @Override
    public void onKeyFound(SubstitutionCypherKey substitutionCypherKey, AsyncTaskCallType type) {
        this.substitutionCypherKey = substitutionCypherKey;
        progressBar.setVisibility(View.INVISIBLE);
        keyHasChanged = false;
        if (type == AsyncTaskCallType.Encrypting) {
            substitutionKeyMapper.mapKey(this.substitutionCypherKey, true);
            outputTextView.setText(substitutionCypherKeyCrypter.encryptMessage(inputEditText.getText().toString(), substitutionKeyMapper));
        }
        else if (type == AsyncTaskCallType.Decrypting) {
            substitutionKeyMapper.mapKey(this.substitutionCypherKey, false);
            outputTextView.setText(substitutionCypherKeyCrypter.decryptMessage(inputEditText.getText().toString(), substitutionKeyMapper));
        }
        else {
            charactersInputFilters[0] = new CharactersInputFilter(this.substitutionCypherKey.getInputCharacters());
            inputEditText.setFilters(charactersInputFilters);
            if (isFromIntent){
                inputEditText.setText(intent.getStringExtra(Intent.EXTRA_TEXT));
                showKeyPickerDialog();
            }
        }
    }

    @Override
    public void onExceptionThrown(ErrorType errorType) {
        if (errorType == ErrorType.WifiError) {
            Snackbar.make(findViewById(R.id.rootView), getResources().getString(R.string.wifi_error_text), Snackbar.LENGTH_LONG).show();
            startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
        } else {
            Snackbar.make(findViewById(R.id.rootView), getResources().getString(R.string.server_error_text), Snackbar.LENGTH_LONG).show();
        }
    }

    private void showKeyPickerDialog() {
        KeyPickerDialog.show(this, generateRandomKey(), KEY_LENGTH, new OnKeySelectedListener[]{this});
    }

    private int generateRandomKey() {
        Random rnd = new Random();
        return rnd.nextInt(MAX_KEY_VALUE + 1);
    }

    private void callSubstitutionCypherAsyncTask(AsyncTaskCallType type) {
        SubstitutionCypherAsyncTask keyGetter = new SubstitutionCypherAsyncTask(type, new OnKeyFoundListener[]{this});
        progressBar.setVisibility(View.VISIBLE);
        keyGetter.execute(substitutionCypherKey);
    }
}
