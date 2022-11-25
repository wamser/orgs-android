package com.wamser.orgs.database.dao

import androidx.room.*
import com.wamser.orgs.model.Produto
import kotlinx.coroutines.flow.Flow

@Dao
interface ProdutoDao {

    @Query("SELECT * FROM Produto ORDER BY nome ASC")
    fun buscaTodosNomeAsc(): Flow<List<Produto>>

    @Query("SELECT * FROM Produto ORDER BY nome DESC")
     fun buscaTodosNomeDesc(): Flow<List<Produto>>

    @Query("SELECT * FROM Produto ORDER BY descricao ASC")
     fun buscaTodosDescricaoAsc(): Flow<List<Produto>>

    @Query("SELECT * FROM Produto ORDER BY descricao DESC")
     fun buscaTodosDescricaoDesc(): Flow<List<Produto>>

    @Query("SELECT * FROM Produto ORDER BY valor ASC")
     fun buscaTodosValorAsc(): Flow<List<Produto>>

    @Query("SELECT * FROM Produto ORDER BY valor DESC")
     fun buscaTodosValorDesc(): Flow<List<Produto>>

    @Query("SELECT * FROM Produto")
    fun buscaTodos(): Flow<List<Produto>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun salva(vararg produto: Produto)

    @Delete
    suspend fun remove(vararg produto: Produto)

    /*@Update
    suspend fun altera(vararg produto: Produto)*/

    @Query("SELECT * FROM Produto WHERE id = :id")
    fun buscaPorId(id: Long) : Flow<Produto?>

}