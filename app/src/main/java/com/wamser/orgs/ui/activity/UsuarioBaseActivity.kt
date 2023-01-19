package com.wamser.orgs.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.lifecycleScope
import androidx.room.Query
import br.com.alura.orgs.extensions.vaiPara
import br.com.alura.orgs.model.Usuario
import com.wamser.orgs.database.AppDatabase
import com.wamser.orgs.model.Produto
import com.wamser.orgs.preferences.dataStore
import com.wamser.orgs.preferences.usuarioLogadoPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

abstract class UsuarioBaseActivity : AppCompatActivity(){

    private val usuarioDao by lazy {
        AppDatabase.instancia(this).UsuarioDao()
    }
    private val _usuario: MutableStateFlow<Usuario?> = MutableStateFlow(null)
    protected val usuario: StateFlow<Usuario?> = _usuario

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch{
            verificaUsuarioLogado()
        }
    }

    protected suspend fun deslogaUsuario() {
        dataStore.edit { preferences ->
            preferences.remove(usuarioLogadoPreferences)

        }
    }

    private fun vaiParaLogin() {
        vaiPara(LoginActivity::class.java){
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        finish()
    }

    private suspend fun verificaUsuarioLogado() {
        dataStore.data.collect { preferences ->
            preferences[usuarioLogadoPreferences]?.let { usuarioId ->
                buscaUsuario(usuarioId)
            } ?: vaiParaLogin()
        }
    }

    private suspend fun buscaUsuario(usuarioId: String) :Usuario? {

        return usuarioDao.buscaPorId(usuarioId).firstOrNull().also {

            _usuario.value = it

        }

    }

    protected fun usuarios() = usuarioDao.buscaTodos()

}