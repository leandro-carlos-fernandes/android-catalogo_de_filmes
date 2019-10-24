package br.tap.filmes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;

public class FilmesBDHelper extends SQLiteOpenHelper {

    // Versão e nome de arquivo para armazenamento
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Filmes.db";

    /**
     * Inner class que desempenha o papel de esquema para a
     * definição da tabela Filmes do banco de dados
     */
    public static class FilmesDesc implements BaseColumns {
        public static final String TABELA_NOME    = "Filmes";
        public static final String COL_TITULO     = "titulo"   , TIPO_TITULO    = "text";
        public static final String COL_SUBTITULO  = "subtitulo", TIPO_SUBTITULO = "text";
        public static final String COL_GENERO     = "genero"   , TIPO_GENERO    = "text";
        public static final String COL_AVALIACAO  = "avaliacao", TIPO_AVALIACAO = "float";
    }

    /*
     * Composição de instruções SQL para criação das tabelas
     * segundo o esquema definido para o banco de dados
     */
    public static final String SQL_CRIAR_TABELA  =
            "CREATE TABLE " + FilmesDesc.TABELA_NOME + " (" +
                    FilmesDesc._ID + " INTEGER PRIMARY KEY," +
                    FilmesDesc.COL_TITULO    + " " + FilmesDesc.TIPO_TITULO    + "," +
                    FilmesDesc.COL_SUBTITULO + " " + FilmesDesc.TIPO_SUBTITULO + "," +
                    FilmesDesc.COL_GENERO    + " " + FilmesDesc.TIPO_GENERO    + "," +
                    FilmesDesc.COL_AVALIACAO + " " + FilmesDesc.TIPO_AVALIACAO + " )";

    public static final String SQL_APAGAR_TABELA =
            "DROP TABLE IF EXISTS " + FilmesDesc.TABELA_NOME;


    /**
     * Métodos obrigatórios na criação de uma classe derivada
     * de SQLiteOpenHelper: construtor, onCreate e onUpgrade
     */
    public FilmesBDHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CRIAR_TABELA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_APAGAR_TABELA);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    /**
     * Métodos que implementam o "contrato" entre app e
     * o banco de dados, atuando como DAO para Filme
     */
    public boolean inserir(Filme filme) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues tupla = new ContentValues();
        tupla.put(FilmesDesc._ID, filme.getId());
        tupla.put(FilmesDesc.COL_TITULO, filme.getTitulo());
        tupla.put(FilmesDesc.COL_SUBTITULO, filme.getSubtitulo());
        tupla.put(FilmesDesc.COL_GENERO, filme.getGenero());
        tupla.put(FilmesDesc.COL_AVALIACAO, filme.getAvaliacao());

        db.insert(FilmesDesc.TABELA_NOME, null, tupla);

        return true;
    }


    public Filme getFilme(int id) {
        Filme filme = null;

        SQLiteDatabase db = this.getReadableDatabase();
        String[] colunas = {
                FilmesDesc._ID,
                FilmesDesc.COL_TITULO,
                FilmesDesc.COL_SUBTITULO,
                FilmesDesc.COL_GENERO,
                FilmesDesc.COL_AVALIACAO
        };
        String criterio = FilmesDesc._ID + " = ?";
        String[] valorArgumentos = { Integer.toString(id) };

        Cursor cursor =  db.query(
                FilmesDesc.TABELA_NOME,  // A tabela a ser consultada
                colunas,                 // As colunas que serão retornadas (projeção)
                criterio,                // A cláusula WHERE
                valorArgumentos,         // Os valores para a cláusula WHERE
                null,            // não agruparemos as linhas
                null,             // critério de filtragem para os agrupamentos
                null             // não ordenar por coluna
        );

        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            filme = new Filme();
            filme.setId( cursor.getInt( cursor.getColumnIndex(FilmesDesc._ID)) );
            filme.setTitulo( cursor.getString( cursor.getColumnIndex(FilmesDesc.COL_TITULO)) );
            filme.setSubtitulo( cursor.getString( cursor.getColumnIndex(FilmesDesc.COL_SUBTITULO)) );
            filme.setGenero( cursor.getString( cursor.getColumnIndex(FilmesDesc.COL_GENERO)) );
            filme.setAvaliacao( cursor.getFloat( cursor.getColumnIndex(FilmesDesc.COL_AVALIACAO)) );
        }
        if (!cursor.isClosed())
            cursor.close();

        return filme;
    }


    public boolean atualizar(Filme filme) {
        SQLiteDatabase db = this.getWritableDatabase();
        
        ContentValues dadosDaTupla = new ContentValues();
        dadosDaTupla.put(FilmesDesc.COL_TITULO, filme.getTitulo());
        dadosDaTupla.put(FilmesDesc.COL_SUBTITULO, filme.getSubtitulo());
        dadosDaTupla.put(FilmesDesc.COL_GENERO, filme.getGenero());
        dadosDaTupla.put(FilmesDesc.COL_AVALIACAO, filme.getAvaliacao());

        db.update(
                FilmesDesc.TABELA_NOME,
                dadosDaTupla,
                FilmesDesc._ID +" = ? ", new String[] { Integer.toString(filme.getId()) }
        );

        return true;
    }


    public Integer remover(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(FilmesDesc.TABELA_NOME,
                FilmesDesc._ID + " = ? ",
                new String[] { Integer.toString(id) });
    }

    public ArrayList<Filme> getTodosFilmes() {
        ArrayList<Filme> lstFilmes = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery(
                "select * from " + FilmesDesc.TABELA_NOME,
                null );

        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            Filme filme = new Filme();
            filme.setId( cursor.getInt( cursor.getColumnIndex(FilmesDesc._ID)) );
            filme.setTitulo( cursor.getString( cursor.getColumnIndex(FilmesDesc.COL_TITULO)) );
            filme.setSubtitulo( cursor.getString( cursor.getColumnIndex(FilmesDesc.COL_SUBTITULO)) );
            filme.setGenero( cursor.getString( cursor.getColumnIndex(FilmesDesc.COL_GENERO)) );
            filme.setAvaliacao( cursor.getFloat( cursor.getColumnIndex(FilmesDesc.COL_AVALIACAO)) );
            lstFilmes.add(filme);
            cursor.moveToNext();
        }
        if (!cursor.isClosed())
            cursor.close();

        return lstFilmes;
    }

//    public Cursor getCursorFilmes() {
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor =  db.rawQuery(
//                "select * from " + FilmesDesc.TABELA_NOME,
//                null );
//        return cursor;
//    }

    public int getNextID() {
        SQLiteDatabase db = this.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db, FilmesDesc.TABELA_NOME);
    }

}
