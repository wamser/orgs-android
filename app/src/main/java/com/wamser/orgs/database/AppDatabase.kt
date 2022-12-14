package com.wamser.orgs.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.wamser.orgs.database.converter.Converters
import com.wamser.orgs.database.dao.ProdutoDao
import com.wamser.orgs.model.Produto

@Database(entities = [Produto::class], version = 1, exportSchema = true)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun produtoDao(): ProdutoDao

    companion object {
        @Volatile private var db: AppDatabase?=null
        fun instancia(context: Context) :AppDatabase{

           return db?:Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "orgs.db"
            ).allowMainThreadQueries().build().also{
                db=it
           }
        }
    }

}