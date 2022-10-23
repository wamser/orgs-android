package com.wamser.orgs.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.wamser.orgs.R
import com.wamser.orgs.database.AppDatabase
import com.wamser.orgs.databinding.ActivityListaProdutosBinding
import com.wamser.orgs.model.Produto
import com.wamser.orgs.ui.recyclerview.adapter.ListaProdutosAdapter

class ListaProdutosActivity : AppCompatActivity(R.layout.activity_lista_produtos) {

    private val adapter = ListaProdutosAdapter(context = this)
    private val binding by lazy {
        ActivityListaProdutosBinding.inflate(layoutInflater)
    }

    private val produtoDao by lazy {
        val db = AppDatabase.instancia(this)
        db.produtoDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configuraRecyclerView()
        configuraFab()
    }

    override fun onResume() {
        super.onResume()

        val db = AppDatabase.instancia(this)

        val produtoDao = db.produtoDao()
        adapter.atualiza(produtoDao.buscaTodos())

    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_detalhes_ordenacao, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {


        when (item.itemId) {

            R.id.menu_detalhes_ordenacao_nome_asc -> {
                adapter.atualiza(produtoDao.buscaTodosNomeAsc())
            }
            R.id.menu_detalhes_ordenacao_nome_desc -> {
                adapter.atualiza(produtoDao.buscaTodosNomeDesc())
            }
            R.id.menu_detalhes_ordenacao_descricao_asc -> {
                adapter.atualiza(produtoDao.buscaTodosDescricaoAsc())
            }
            R.id.menu_detalhes_ordenacao_descricao_desc -> {
                adapter.atualiza(produtoDao.buscaTodosDescricaoDesc())
            }
            R.id.menu_detalhes_ordenacao_valor_asc -> {
                adapter.atualiza(produtoDao.buscaTodosValorAsc())
            }
            R.id.menu_detalhes_ordenacao_valor_desc -> {
                adapter.atualiza(produtoDao.buscaTodosValorDesc())
            }
            R.id.menu_detalhes_ordenacao_sem_ordem -> {
                adapter.atualiza(produtoDao.buscaTodos())
            }

        }

        return super.onOptionsItemSelected(item)
    }

    private fun configuraFab() {
        val fab = binding.activityListaProdutosFab
        fab.setOnClickListener {

            val intent = Intent(this, FormularioProdutoActivity::class.java)

            startActivity(intent)
        }
    }

    private fun configuraRecyclerView() {
        val recyclerView = binding.activityListaProdutosRecyclerView
        recyclerView.adapter = adapter
        adapter.quandoClicaNoItemListener = {


            val intent = Intent(
                this,
                DetalhesProdutoActivity::class.java
            ).apply {
                // envio do produto por meio do extra
                putExtra(CHAVE_PRODUTO_ID, it.id)
            }
            startActivity(intent)

        }
    }
}

