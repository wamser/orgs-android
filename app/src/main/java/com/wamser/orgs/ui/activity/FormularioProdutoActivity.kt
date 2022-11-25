package com.wamser.orgs.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.wamser.orgs.R
import com.wamser.orgs.database.AppDatabase
import com.wamser.orgs.databinding.ActivityFormularioProdutoBinding
import com.wamser.orgs.extensions.tentaCarregarImagem
import com.wamser.orgs.model.Produto
import com.wamser.orgs.ui.dialog.FormularioImagemDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal

const val TAG = "Estado da Activity"

class FormularioProdutoActivity : AppCompatActivity(R.layout.activity_formulario_produto) {

    private val binding by lazy {
        ActivityFormularioProdutoBinding.inflate(layoutInflater)
    }

    private var url: String? = null
    private var idProduto = 0L
    private val produtoDao by lazy {
        val db = AppDatabase.instancia(this)
        db.produtoDao()
    }
    //private val scope = CoroutineScope(Dispatchers.IO)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate: ")
        title = "Cadastrar produto"
        configuraBotaoSalvar()
        setContentView(binding.root)
        binding.activityFormularioProdutoImagem.setOnClickListener {
            FormularioImagemDialog(this).mostra(url) { imagem ->
                url = imagem
                binding.activityFormularioProdutoImagem.tentaCarregarImagem(url)
            }
        }
        tentaCarregarProduto()

    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "onResume: ")
        tentaBuscarProduto()
    }

    private fun buscaProduto() {
        lifecycleScope.launch {
            produtoDao.buscaPorId(idProduto).collect{produto->
                withContext(Dispatchers.Main) {
                    produto?.let {
                        preencheCampos(it)
                    } ?: finish()
                }
            }
        }

    }

    private fun tentaBuscarProduto() {
        lifecycleScope.launch {
            produtoDao.buscaPorId(idProduto).collect{produto->
                withContext(Dispatchers.Main) {
                    produto?.let {
                        title = "Alterar produto"
                        preencheCampos(it)
                    } ?: finish()
                }
            }
        }

    }

    private fun tentaCarregarProduto() {
        idProduto = intent.getLongExtra(CHAVE_PRODUTO_ID, 0L)
    }

    private fun preencheCampos(produto: Produto) {
        url = produto.imagem
        binding.activityFormularioProdutoImagem.tentaCarregarImagem(produto.imagem)
        binding.activityFormularioProdutoNome.setText(produto.nome)
        binding.activityFormularioProdutoDescricao.setText(produto.descricao)
        binding.activityFormularioProdutoValor.setText(produto.valor.toPlainString())
    }

    private fun configuraBotaoSalvar() {
        val botaoSalvar = binding.activityFormularioProdutoBotaoSalvar



        botaoSalvar.setOnClickListener {

            val produtoNovo = criaProduto()

            lifecycleScope.launch {
                produtoDao.salva(produtoNovo)
                finish()
            }
        }
    }


    private fun criaProduto(): Produto {

        val campoNome =
            binding.activityFormularioProdutoNome//findViewById<EditText>(R.id.activity_formulario_produto_nome)
        val nome = campoNome.text.toString()

        val campoDesc =
            binding.activityFormularioProdutoDescricao//findViewById<EditText>(R.id.activity_formulario_produto_descricao)
        val descricao = campoDesc.text.toString()

        val campoValor =
            binding.activityFormularioProdutoValor//findViewById<EditText>(R.id.activity_formulario_produto_valor)
        val valorEmTexto = campoValor.text.toString()

        val valor = if (valorEmTexto.isBlank()) {
            BigDecimal.ZERO
        } else {
            BigDecimal(valorEmTexto)
        }

        return Produto(
            id = idProduto,
            nome = nome,
            descricao = descricao,
            valor = valor,
            imagem = url
        )

    }

    override fun onStart() {
        super.onStart()
        Log.i(TAG, "onStart: ")
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
}