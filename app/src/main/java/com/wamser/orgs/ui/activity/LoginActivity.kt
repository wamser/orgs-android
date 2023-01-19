package com.wamser.orgs.ui.activity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import br.com.alura.orgs.extensions.toHash
import br.com.alura.orgs.extensions.toast
//import br.com.alura.orgs.databinding.ActivityLoginBinding
import br.com.alura.orgs.extensions.vaiPara
import com.wamser.orgs.R
import com.wamser.orgs.database.AppDatabase
import com.wamser.orgs.databinding.ActivityLoginBinding
import com.wamser.orgs.preferences.dataStore
import com.wamser.orgs.preferences.usuarioLogadoPreferences
import com.wamser.orgs.ui.activity.ListaProdutosActivity
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity(R.layout.activity_login) {

    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    private val usuarioDao by lazy {
        AppDatabase.instancia(this).UsuarioDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configuraBotaoCadastrar()
        configuraBotaoEntrar()
    }

    private fun configuraBotaoEntrar() {
        binding.activityLoginBotaoEntrar.setOnClickListener {
            val usuario = binding.activityLoginUsuario.text.toString()
            val senha = binding.activityLoginSenha.text.toString().toHash()
            autentica(usuario, senha)

        }
    }

    private fun autentica(usuario: String, senha: String) {
        lifecycleScope.launch {
            usuarioDao.autentica(usuario, senha)?.let { usuario ->
                dataStore.edit { preferences ->
                    preferences[usuarioLogadoPreferences] = usuario.id

                }
                vaiPara(ListaProdutosActivity::class.java)
                finish()
            } ?:toast("Falha na autenticação")
        }
    }

    private fun configuraBotaoCadastrar() {
        binding.activityLoginBotaoCadastrar.setOnClickListener {
            vaiPara(FormularioCadastroUsuarioActivity::class.java)
        }
    }

}