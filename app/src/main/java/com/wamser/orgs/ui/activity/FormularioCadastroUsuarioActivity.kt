package com.wamser.orgs.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import br.com.alura.orgs.extensions.toHash
import br.com.alura.orgs.extensions.toast
//import br.com.alura.orgs.databinding.ActivityFormularioCadastroUsuarioBinding
import br.com.alura.orgs.model.Usuario
import com.wamser.orgs.R
import com.wamser.orgs.database.AppDatabase
import com.wamser.orgs.databinding.ActivityFormularioCadastroUsuarioBinding
import com.wamser.orgs.extensions.tentaCarregarImagem
import com.wamser.orgs.ui.dialog.FormularioImagemDialog
import kotlinx.coroutines.launch

class FormularioCadastroUsuarioActivity : AppCompatActivity(R.layout.activity_formulario_cadastro_usuario) {

    private val binding by lazy {
        ActivityFormularioCadastroUsuarioBinding.inflate(layoutInflater)
    }

    private val dao by lazy{
        AppDatabase.instancia(this).UsuarioDao()
    }
    private var url: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configuraBotaoCadastrar()
        binding.activityFormularioUsuarioImagem.setOnClickListener {
            FormularioImagemDialog(this).mostra(url) { imagem ->
                url = imagem
                binding.activityFormularioUsuarioImagem.tentaCarregarImagem(url)
            }
        }
    }

    private fun configuraBotaoCadastrar() {
        binding.activityFormularioCadastroBotaoCadastrar.setOnClickListener {
            val novoUsuario = criaUsuario()
            Log.i("CadastroUsuario", "onCreate: $novoUsuario")
            cadastra(novoUsuario)
        }
    }

    private fun cadastra(usuario: Usuario) {
        lifecycleScope.launch {
            try {
                dao.salva(usuario)
                finish()
            } catch (e: Exception) {
                toast("Falha ao cadastrar usuário.")
            }
        }
    }

    private fun criaUsuario(): Usuario {
        val usuario = binding.activityFormularioCadastroUsuario.text.toString()
        val nome = binding.activityFormularioCadastroNome.text.toString()
        val senha = binding.activityFormularioCadastroSenha.text.toString().toHash()
        val imagem = url
        return Usuario(usuario, nome, senha,imagem)
    }

}