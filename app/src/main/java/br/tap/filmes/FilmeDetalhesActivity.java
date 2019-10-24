package br.tap.filmes;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

/**
 * Uma atividade para representar a tela de detalhamento do Filme.
 * Esta atividade é utilizada apenas em dispositivos do tipo celular
 * ou que tenham tela estreita. Em aparelhos do tipo tablet, os detalhes
 * serão apresentados num painel lado-a-lado com a lista de filmes na.
 * {@link FeedFilmesActivity}.
 */
public class FilmeDetalhesActivity extends AppCompatActivity {

    private int mFilmeId;
    private FilmesBDHelper mBD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filme_detalhes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        // Apresentando o botão "Up" (retornar) na barra de ações,
        // Isto implementando o fluxo de navegação que permite voltar a tela
        // anterior (i.e., a activity parent informada no manifesto).
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mBD = new FilmesBDHelper(this);

        // Caso não haja uma instância anterior salvas (decorente de uma
        // rotação de tela, por exemplo), fazemos o carregamento do fragmento.
        if (savedInstanceState == null) {

            mFilmeId = getIntent().getIntExtra(FilmeDetalhesFragment.ARG_ITEM_ID, 0);

            Bundle arguments = new Bundle();
            arguments.putInt(FilmeDetalhesFragment.ARG_ITEM_ID, mFilmeId);

            FilmeDetalhesFragment fragment = new FilmeDetalhesFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.detalhes_filme_container, fragment)
                    .commit();
        }
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
     * Handler para tratar eventos do botão Remover
     * @param view referencia ao botão remover
     */
    public void remover(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Deseja realmente excluir este filme?")
                .setTitle("Confirmar exclusão:");

        builder.setPositiveButton(R.string.lblSim, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                mBD.remover(mFilmeId);
                Toast.makeText(getApplicationContext(), "Filme excluído!", Toast.LENGTH_LONG).show();
                finish();
            }
        });

        builder.setNegativeButton(R.string.lblNao, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(getApplicationContext(), "Operação cancelada.", Toast.LENGTH_LONG).show();
            }
        });
        builder.create().show();
    }
}
