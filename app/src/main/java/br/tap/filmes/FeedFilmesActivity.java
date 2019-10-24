package br.tap.filmes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

/**
 * Uma activity respresentando um Catálogo de Filmes (Lista de Filmes).
 * Esta atividade tem diferentes apresentações, dependendo do dispositivo.
 * Em celulares, a atividade apresenta uma lista de itens que, quando clicados,
 * remetem a {@link FilmeDetalhesActivity} representando os detalhes sobre o
 * filme. Em um tablet, a atividade apresenta uma lista de itens e os detalhes
 * do item lado-a-lado usando dois painéis veticais.
 */
public class FeedFilmesActivity extends AppCompatActivity {

    // Sinaliza se está ou não no modo painel duplo (i.e., se executando num
    // dispositivo do tipo tablet).
    private boolean mTwoPane;

    // Acesso ao BD
    private FilmesBDHelper mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_filmes);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.btnAdicionar);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), CadastrarFilmeActivity.class));
            }
        });

        if (findViewById(R.id.detalhes_filme_container) != null) {
            mTwoPane = true;
        }

        mDB = new FilmesBDHelper(this);

    }

    @Override
    protected void onResume() {
        super.onResume();

        // A associação do adapter no método onResume permite que após a
        // adição ou exclusão de um filme noutra activity, ao retomar, esta
        // atividade atualize o conteúdo da lista com as novas informações.
        View recyclerView = findViewById(R.id.item_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
    }

    /**
     * Método para configuração do RecyclerView que define que entidade operará
     * como elemento adaptador entre o componente gráfico que representa a lista
     * e o que contém a lista de filmes (conteúdo da lista).
     * @param recyclerView componente gráfico que apresentará o catálogo de filmes.
     */
    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new RecyclerViewAdapter(this, mDB.getTodosFilmes(), mTwoPane));
    }

    /**
     * Inner class adaptadora para recuperação do conteúdo da lista.
     * Esta classe funciona como uma fonte de dados (datasource) dos filmes a
     * serem apresentado na lista, criando sincronismo entre o conteúdo exibido
     * e a fonte de dados. Deste modo, as interações do usuário com a lista
     * são detectadas e o ID do item correspondente é passado como argumento
     * para a activity ou o fragmento responsável pelo detalhamento.
     */
    public static class RecyclerViewAdapter
            extends RecyclerView.Adapter<RecyclerViewAdapter.FilmeHolder> {

        private final FeedFilmesActivity mParentActivity;
        private final List<Filme> mValues;
        private final boolean mTwoPane;

        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Filme item = (Filme) view.getTag();
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putInt(FilmeDetalhesFragment.ARG_ITEM_ID, item.getId());
                    FilmeDetalhesFragment fragment = new FilmeDetalhesFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.detalhes_filme_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, FilmeDetalhesActivity.class);
                    intent.putExtra(FilmeDetalhesFragment.ARG_ITEM_ID, item.getId());
                    context.startActivity(intent);
                }
            }
        };

        RecyclerViewAdapter(FeedFilmesActivity parent,
                            List<Filme> items,
                            boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public FilmeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_da_lista, parent, false);
            return new FilmeHolder(view);
        }

        /**
         * Método que combina conteúdo e apresentação, usando o view holder
         * (container do item) e um indice para referenciar a lista de filmes.
         * @param holder componente visual que abriga os dados de um item
         * @param position corresponde ao indice do item a ser utilizado
         */
        @Override
        public void onBindViewHolder(final FilmeHolder holder, int position) {
            // Popula o conteúdo dos componentes com as informações do filme
            // de acordo com a sua posição na lista (posição do catálogo)
            holder.mIdView.setText( String.valueOf(mValues.get(position).getId()) );
            holder.mTituloView.setText( mValues.get(position).getTitulo() + " - " + mValues.get(position).getSubtitulo() );

            // Associa o ID como referencia de busca para aquele item (Filme) e
            // define qual será o elemento (listener) que manipulará o evento
            // de touch sobre o item (seleção do filme).
            holder.itemView.setTag( mValues.get(position) );
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        /**
         * Inner class que representa graficamente (View) um único item da
         * lista (i.e., id e o título do filme). Esta classe atua com Holder
         * dos dados do filme, sendo o componente gráfico a ser apresentado
         * num dos slots da lista.
         */
        class FilmeHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;
            final TextView mTituloView;

            FilmeHolder(View view) {
                super(view);
                mIdView = (TextView) view.findViewById(R.id.item_id);
                mTituloView = (TextView) view.findViewById(R.id.item_titulo);
            }
        }
    }
}
