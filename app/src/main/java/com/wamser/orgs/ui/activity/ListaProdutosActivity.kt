package com.wamser.orgs.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.lifecycleScope
import br.com.alura.orgs.extensions.vaiPara
import com.wamser.orgs.R
import com.wamser.orgs.database.AppDatabase
import com.wamser.orgs.databinding.ActivityListaProdutosBinding
import com.wamser.orgs.ui.recyclerview.adapter.ListaProdutosAdapter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class ListaProdutosActivity : UsuarioBaseActivity() {

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

        lifecycleScope.launch {

            val launch = launch {
                usuario.filterNotNull().collect {usuario->
                    buscaProdutosUsuario(usuario.id)
                }

            }
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_detalhes_ordenacao, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {


        when (item.itemId) {

            R.id.menu_detalhes_ordenacao_nome_asc -> {
                lifecycleScope.launch {
                    usuario.filterNotNull().collect { usuario ->
                        produtoDao.buscaTodosNomeAsc(usuario.id).collect { produtos ->
                            adapter.atualiza(produtos)
                        }
                    }
                }
            }
            R.id.menu_detalhes_ordenacao_nome_desc -> {
                lifecycleScope.launch {
                    usuario.filterNotNull().collect { usuario ->
                        produtoDao.buscaTodosNomeDesc(usuario.id).collect { produtos ->
                            adapter.atualiza(produtos)
                        }
                    }
                }
            }
            R.id.menu_detalhes_ordenacao_descricao_asc -> {
                lifecycleScope.launch {
                    usuario.filterNotNull().collect { usuario ->
                        produtoDao.buscaTodosDescricaoAsc(usuario.id).collect { produtos ->
                            adapter.atualiza(produtos)
                        }
                    }
                }
            }
            R.id.menu_detalhes_ordenacao_descricao_desc -> {
                lifecycleScope.launch {
                    usuario.filterNotNull().collect { usuario ->
                        produtoDao.buscaTodosDescricaoDesc(usuario.id).collect { produtos ->
                            adapter.atualiza(produtos)
                        }
                    }
                }
            }
            R.id.menu_detalhes_ordenacao_valor_asc -> {
                lifecycleScope.launch {
                    usuario.filterNotNull().collect { usuario ->
                    produtoDao.buscaTodosValorAsc(usuario.id).collect { produtos ->
                        adapter.atualiza(produtos)
                    }
                    }
                }
            }
            R.id.menu_detalhes_ordenacao_valor_desc -> {
                lifecycleScope.launch {
                    usuario.filterNotNull().collect { usuario ->
                        produtoDao.buscaTodosValorDesc(usuario.id).collect { produtos ->
                            adapter.atualiza(produtos)
                        }
                    }
                }
            }
            R.id.menu_detalhes_ordenacao_sem_ordem -> {
                lifecycleScope.launch {
                    usuario.filterNotNull().collect { usuario ->
                        produtoDao.buscaTodos(usuario.id).collect { produtos ->
                            adapter.atualiza(produtos)

                        }
                    }
                }
            }
            R.id.menu_lista_produtos_perfil_usuario -> {
                Log.i("ListaProdutosActivity", "menu_lista_produtos_perfil_usuario")

                vaiPara(DetalhesUsuarioActivity::class.java)

            }


        R.id.menu_lista_produtos_produto_sem_usuario -> {
            Log.i("ListaProdutosActivity", "menu_lista_produtos_produto_sem_usuario")


            //vaiPara(ListaProdutosSemUsuarioActivity::class.java)

            lifecycleScope.launch {

                    produtoDao.buscaTodosSemUsuario().collect { produtos ->
                        adapter.atualiza(produtos)
                    }

            }

        }

            R.id.menu_lista_produtos_sair_app -> {
                lifecycleScope.launch {
                    deslogaUsuario()
                }
            }
        }



        return super.onOptionsItemSelected(item)
    }


    private suspend fun buscaProdutosUsuario(usuarioId: String) {
        produtoDao.buscaTodos(usuarioId).collect { produtos ->
            adapter.atualiza(produtos)
        }
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
                this, DetalhesProdutoActivity::class.java
            ).apply {

                putExtra(CHAVE_PRODUTO_ID, it.id)
            }
            startActivity(intent)

        }
    }
}

