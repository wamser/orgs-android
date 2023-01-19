package com.wamser.orgs.database.dao

import androidx.room.*
import com.wamser.orgs.model.Produto
import kotlinx.coroutines.flow.Flow

@Dao
interface ProdutoDao {

    @Query("SELECT * FROM Produto WHERE usuarioId = :usuarioId ORDER BY nome ASC")
    fun buscaTodosNomeAsc(usuarioId:String): Flow<List<Produto>>

    @Query("SELECT * FROM Produto WHERE usuarioId = :usuarioId ORDER BY nome DESC")
     fun buscaTodosNomeDesc(usuarioId:String): Flow<List<Produto>>

    @Query("SELECT * FROM Produto WHERE usuarioId = :usuarioId ORDER BY descricao ASC")
     fun buscaTodosDescricaoAsc(usuarioId:String): Flow<List<Produto>>

    @Query("SELECT * FROM Produto WHERE usuarioId = :usuarioId ORDER BY descricao DESC")
     fun buscaTodosDescricaoDesc(usuarioId:String): Flow<List<Produto>>

    @Query("SELECT * FROM Produto WHERE usuarioId = :usuarioId ORDER BY valor ASC")
     fun buscaTodosValorAsc(usuarioId:String): Flow<List<Produto>>

    @Query("SELECT * FROM Produto WHERE usuarioId = :usuarioId ORDER BY valor DESC")
     fun buscaTodosValorDesc(usuarioId:String): Flow<List<Produto>>

    @Query("SELECT * FROM Produto WHERE usuarioId = :usuarioId")
    fun buscaTodos(usuarioId:String): Flow<List<Produto>>

    @Query("SELECT * FROM Produto WHERE usuarioId = '' OR usuarioId IS NULL ")
    fun buscaTodosSemUsuario(): Flow<List<Produto>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun salva(vararg produto: Produto)

    @Delete
    suspend fun remove(vararg produto: Produto)

    /*@Update
    suspend fun altera(vararg produto: Produto)*/

    @Query("SELECT * FROM Produto WHERE id = :id")
    fun buscaPorId(id: Long) : Flow<Produto?>

}