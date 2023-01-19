package com.wamser.orgs.ui.activity

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.wamser.orgs.databinding.ActivityDetalhesUsuarioBinding
import com.wamser.orgs.extensions.tentaCarregarImagem
import com.wamser.orgs.ui.activity.UsuarioBaseActivity
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class DetalhesUsuarioActivity : UsuarioBaseActivity() {

    private val binding by lazy {
        ActivityDetalhesUsuarioBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        preencheCampos()
    }

    private fun preencheCampos() {
        lifecycleScope.launch {
            usuario
                .filterNotNull()
                .collect { usuarioLogado ->

                    binding.activityDetalhesUsuarioId.text = usuarioLogado.id
                    binding.activityDetalhesUsuarioNome.text = usuarioLogado.nome
                    binding.activityDetalhesUsuarioImagem.tentaCarregarImagem(usuarioLogado.imagem)

                }
        }
    }
}