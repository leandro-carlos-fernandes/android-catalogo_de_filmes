package br.tap.filmes;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.CollapsingToolbarLayout;

/**
 * Um fragmento representando os detalhes de um Filme na tela.
 * Este fragment pode estar contido tanto num {@link FeedFilmesActivity} no
 * modo de painel duplo (em tablets) ou num {@link FilmeDetalhesActivity} no
 * celular.
 */
public class FilmeDetalhesFragment extends Fragment {

    // Chave para recuperação do valor que representa o ID do filme, que é
    // recebido pelo framento como um argumento.
    public static final String ARG_ITEM_ID = "item_id";

    // Instância de Filme cujos dados serão apresentado neste fragmento.
    private Filme filme;

    // Para acesso ao BD
    private FilmesBDHelper mDB;

    /**
     * A existencia de um construtor sem parametros é obrigatória para que o
     * gerenciador de fragmentos (fragment manager) possa instanciá-lo.
     */
    public FilmeDetalhesFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDB = new FilmesBDHelper(getContext());

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Recuperamos as informações do BD com base no ID do filme (que
            // foi informado como argumento na criação do fragmento.
            filme = mDB.getFilme( getArguments().getInt(ARG_ITEM_ID) );

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(filme.getTitulo());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Carregando o conteúdo da tela (o fragmento e seus componentes)
        View rootView = inflater.inflate(R.layout.dados_do_filme, container, false);

        // Preenchendo os componentes com as informações do filme, que foi
        // recuperado do BD durante a criação do Fragment (onCreate)
        if (filme != null) {
            ((EditText) rootView.findViewById(R.id.etTitulo)).setText(filme.getTitulo());
            ((EditText) rootView.findViewById(R.id.etSubtitulo)).setText(filme.getSubtitulo());
            String[] generos = getResources().getStringArray(R.array.generos);
            for(int i = 0; i < generos.length; i++)
                if (generos[i].equals(filme.getGenero())) {
                    ((Spinner) rootView.findViewById(R.id.spGeneros)).setSelection(i);
                    break;
                }
            ((RatingBar) rootView.findViewById(R.id.rbAvaliacao)).setRating(filme.getAvaliacao());
        }

        return rootView;
    }
}
