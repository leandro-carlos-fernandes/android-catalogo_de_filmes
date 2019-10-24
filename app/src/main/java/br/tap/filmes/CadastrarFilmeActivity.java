package br.tap.filmes;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class CadastrarFilmeActivity extends AppCompatActivity {

    private FilmesBDHelper mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_filme);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mDB = new FilmesBDHelper(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            navigateUpTo(new Intent(this, FeedFilmesActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Método que grava os dados do novo filme no BD.
     * @param view referência ao botão "Salvar"
     */
    public void salvar(View view) {

        Filme filme = new Filme();

        filme.setId( mDB.getNextID() );
        filme.setTitulo( ((EditText) findViewById(R.id.etTitulo)).getText().toString() );
        filme.setSubtitulo( ((EditText) findViewById(R.id.etSubtitulo)).getText().toString() );
        filme.setGenero( ((Spinner) findViewById(R.id.spGeneros)).getSelectedItem().toString() );
        filme.setAvaliacao( ((RatingBar) findViewById(R.id.rbAvaliacao)).getRating() );

        mDB.inserir(filme);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("As informações do novo filme foram adicionadas com sucesso.")
                .setTitle("Dados salvos");
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });
        builder.create().show();
    }
}
