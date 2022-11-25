package com.wamser.orgs.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
//import br.com.alura.orgs.databinding.ActivityLoginBinding
import br.com.alura.orgs.extensions.vaiPara
import com.wamser.orgs.R
import com.wamser.orgs.databinding.ActivityLoginBinding
import com.wamser.orgs.ui.activity.ListaProdutosActivity

class LoginActivity : AppCompatActivity(R.layout.activity_login) {

    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
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
            val senha = binding.activityLoginSenha.text.toString()
            Log.i("LoginActivity", "onCreate: $usuario - $senha")
            vaiPara(ListaProdutosActivity::class.java)
        }
    }

    private fun configuraBotaoCadastrar() {
        binding.activityLoginBotaoCadastrar.setOnClickListener {
            vaiPara(FormularioCadastroUsuarioActivity::class.java)
        }
    }

}