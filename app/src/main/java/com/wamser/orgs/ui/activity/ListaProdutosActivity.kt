package com.wamser.orgs.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.wamser.orgs.R
import com.wamser.orgs.dao.ProdutosDao
import com.wamser.orgs.databinding.ActivityListaProdutosBinding
import com.wamser.orgs.ui.recyclerview.adapter.ListaProdutosAdapter

class ListaProdutosActivity : AppCompatActivity(R.layout.activity_lista_produtos) {

    private val dao = ProdutosDao()
    private val adapter = ListaProdutosAdapter(context = this, produtos = dao.buscaTodos())


    private val binding by lazy {
        ActivityListaProdutosBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configuraRecyclerView()
        configuraFab()
    }

    override fun onResume() {
        super.onResume()
        adapter.atualiza(dao.buscaTodos())

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

            Log.i("ListaProdutosActivity", "quandoClica: ${it.nome}")
            val intent = Intent(this, DetalhesProdutoActivity::class.java)
            //intent.putExtra("nome",makeUsername.text.toString())
            intent.putExtra("nome",it.nome)
            intent.putExtra("descricao",it.descricao)
            intent.putExtra("valor",it.valor.toString())
            intent.putExtra("imagem",it.imagem)
            startActivity(intent)

        }
    }
}