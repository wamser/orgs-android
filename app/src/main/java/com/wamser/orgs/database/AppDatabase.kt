package com.wamser.orgs.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.alura.orgs.model.Usuario
import com.wamser.orgs.database.converter.Converters
import com.wamser.orgs.database.dao.ProdutoDao
import com.wamser.orgs.database.dao.UsuarioDao
import com.wamser.orgs.model.Produto

@Database(entities = [
    Produto::class,
    Usuario::class
                     ], version = 4, exportSchema = true)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun produtoDao(): ProdutoDao
    abstract fun UsuarioDao(): UsuarioDao

    companion object {
        @Volatile private var db: AppDatabase?=null
        fun instancia(context: Context) :AppDatabase{

           return db?:Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "orgs.db"
            ).addMigrations(MIGRATION_1_2,MIGRATION_2_3,MIGRATION_3_4).build().also{
                db=it
           }
        }
    }

}