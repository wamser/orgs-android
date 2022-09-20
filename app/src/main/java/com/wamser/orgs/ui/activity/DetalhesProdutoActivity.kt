package com.wamser.orgs.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.wamser.orgs.R
import com.wamser.orgs.databinding.ActivityDetalhesProdutoBinding
import com.wamser.orgs.extensions.tentaCarregarImagem
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.*

//const val TAG = "Estado da Activity"

class DetalhesProdutoActivity : AppCompatActivity(R.layout.activity_detalhes_produto) {

    private val binding by lazy {
        ActivityDetalhesProdutoBinding.inflate(layoutInflater)
    }

    private var url: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate: ")
        this.supportActionBar!!.hide()

        val extras = getIntent().getExtras()
        if (null != extras) {
            val nome = extras.getString("nome")
            val descricao = extras.getString("descricao")
            val valor = extras.getString("valor")?.toBigDecimal()//extras.getDouble("valor")
            val valorEmMoeda: String = formataParaMoedaBrasileira(valor!!)
            val urlImagem = extras.getString("imagem")

            Log.i(TAG, "onCreate: $nome")
            binding.activityDetalhesProdutoNome.setText(nome)
            Log.i(TAG, "onCreate: $descricao")
            binding.activityDetalhesProdutoDescricao.setText(descricao)
            Log.i(TAG, "onCreate: $valorEmMoeda")
            binding.activityDetalhesProdutoValor.setText(valorEmMoeda)
            Log.i(TAG, "onCreate: $urlImagem")
            binding.activityDetalhesProdutoImagem.tentaCarregarImagem(urlImagem)

        }
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        Log.i(TAG, "onStart: ")
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "onResume: ")
    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG, "onPause: ")
    }

    override fun onStop() {
        super.onStop()
        Log.i(TAG, "onStop: ")
    }

    override fun onRestart() {
        super.onRestart()
        Log.i(TAG, "onRestart: ")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy: ")
    }

    private fun formataParaMoedaBrasileira(valor: BigDecimal): String {
        val formatador: NumberFormat = NumberFormat
            .getCurrencyInstance(Locale("pt", "br"))
        return formatador.format(valor)
    }

}