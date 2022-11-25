package com.wamser.orgs.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import br.com.alura.orgs.extensions.formataParaMoedaBrasileira
import com.wamser.orgs.R
import com.wamser.orgs.database.AppDatabase
import com.wamser.orgs.databinding.ActivityDetalhesProdutoBinding
import com.wamser.orgs.extensions.tentaCarregarImagem
import com.wamser.orgs.model.Produto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.*

class DetalhesProdutoActivity : AppCompatActivity(R.layout.activity_detalhes_produto) {


    private var idProduto: Long = 0L
    private var produto: Produto? = null
    private val binding by lazy {
        ActivityDetalhesProdutoBinding.inflate(layoutInflater)
    }

    private val produtoDao by lazy {
        AppDatabase.instancia(this).produtoDao()
    }

    private var url: String? = null

    //private val scope = CoroutineScope(IO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        tentaCarregarProduto()

    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "onResume: ")
        buscaProduto()
    }

    private fun buscaProduto() {
        lifecycleScope.launch {
            produtoDao.buscaPorId(idProduto).collect{produto->
            withContext(Main) {
                produto?.let {
                    preencheCampos(it)
                } ?: finish()
            }
            }
        }

    }

    private fun tentaCarregarProduto() {
        //intent.getParcelableExtra<Produto>("CHAVE_PRODUTO_ID")?.let { produtoCarregado ->
        idProduto=intent.getLongExtra(CHAVE_PRODUTO_ID,0L)
    }

    private fun preencheCampos(produtoCarregado: Produto) {
        with(binding) {
            activityDetalhesProdutoImagem.tentaCarregarImagem(produtoCarregado.imagem)
            activityDetalhesProdutoNome.text = produtoCarregado.nome
            activityDetalhesProdutoDescricao.text = produtoCarregado.descricao
            activityDetalhesProdutoValor.text =
                produtoCarregado.valor.formataParaMoedaBrasileira()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_detalhes_produto, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {


        when (item.itemId) {
            R.id.menu_detalhes_produto_editar -> {
                Intent(this, FormularioProdutoActivity::class.java).apply {
                    putExtra(CHAVE_PRODUTO_ID,idProduto)
                    startActivity(this)
                }
            }
            R.id.menu_detalhes_produto_excluir -> {
                lifecycleScope.launch {
                    produto?.let { produtoDao.remove(it) }
                    finish()
                }

            }
        }

        return super.onOptionsItemSelected(item)
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

    private fun formataParaMoedaBrasileira(valor: BigDecimal): String {
        val formatador: NumberFormat = NumberFormat
            .getCurrencyInstance(Locale("pt", "br"))
        return formatador.format(valor)
    }

}