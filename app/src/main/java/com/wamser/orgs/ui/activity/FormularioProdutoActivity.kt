package com.wamser.orgs.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.lifecycle.lifecycleScope
import br.com.alura.orgs.model.Usuario
import com.wamser.orgs.R
import com.wamser.orgs.database.AppDatabase
import com.wamser.orgs.databinding.ActivityFormularioProdutoBinding
import com.wamser.orgs.extensions.tentaCarregarImagem
import com.wamser.orgs.model.Produto
import com.wamser.orgs.ui.dialog.FormularioImagemDialog
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.math.BigDecimal

const val TAG = "Estado da Activity"

class FormularioProdutoActivity : UsuarioBaseActivity() {

    private val binding by lazy {
        ActivityFormularioProdutoBinding.inflate(layoutInflater)
    }

    private var url: String? = null
    private var idProduto = 0L
    private val produtoDao by lazy {
        val db = AppDatabase.instancia(this)
        db.produtoDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "FormularioProdutoActivity - onCreate: ")
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
        Log.i(TAG, "FormularioProdutoActivity - onResume: ")
        tentaBuscarProduto()
    }

    private fun tentaBuscarProduto() {
        lifecycleScope.launch {
            produtoDao.buscaPorId(idProduto).collect {
                it?.let { produtoEncontrado ->
                    title = "Alterar produto"
                    val campoUsuarioId = binding.activityFormularioProdutoUsuarioId
                    campoUsuarioId.visibility = if (produtoEncontrado.salvoSemUsuario()) {
                            configuraCampoUsuario()
                            VISIBLE
                        } else{
                            GONE
                        }
                    preencheCampos(produtoEncontrado)
                }
            }
        }
    }


    private fun configuraCampoUsuario() {
        lifecycleScope.launch {
            usuarios()
                .map { usuarios -> usuarios.map { it.id } }
                .collect { usuarios ->
                    configuraAutoCompleteTextView(usuarios)
                }
        }
    }

    private fun configuraAutoCompleteTextView(
        usuarios: List<String>
    ) {
        val campoUsuarioId = binding.activityFormularioProdutoUsuarioId
        /*val adapter = ArrayAdapter(
            this@FormularioProdutoActivity,
            android.R.layout.simple_dropdown_item_1line,
            usuarios
        )*/
        val adapter = ArrayAdapter<String>(this@FormularioProdutoActivity, R.layout.dropdown_item, usuarios)
        (campoUsuarioId.editText as? AutoCompleteTextView)?.setAdapter(adapter)

        //campoUsuarioId.
        /*campoUsuarioId.setOnFocusChangeListener { _, focado ->
            if (!focado) {
                usuarioExistenteValido(usuarios)
            }
        }*/
    }

    private fun usuarioExistenteValido(
        usuarios: List<String>
    ): Boolean {
        val campoUsuarioId = binding.activityFormularioProdutoUsuarioId
        val usuarioId = campoUsuarioId.getEditText() //text.toString()
        /*if (!usuarios.contains(usuarioId)) {
            campoUsuarioId.error = "usuário inexistente!"
            return false
        }*/
        return true
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

    private suspend fun tentaSalvarProduto() {
        usuario.value?.let { usuario ->
            try {
                val usuarioId = defineUsuarioId(usuario)
                val produto = criaProduto()
                produtoDao.salva(produto)
                finish()
            } catch (e: RuntimeException) {
                Log.e("FormularioProduto", "configuraBotaoSalvar: ", e)
            }
        }
    }

    private fun configuraBotaoSalvar() {
        val botaoSalvar = binding.activityFormularioProdutoBotaoSalvar
        botaoSalvar.setOnClickListener {

            lifecycleScope.launch {
                usuario.value?.let {
                    tentaSalvarProduto()
                }
            }

        }
    }

    private suspend fun defineUsuarioId(usuario: Usuario): String = produtoDao
        .buscaPorId(idProduto)
        .first()?.let { produtoEncontrado ->
            if (produtoEncontrado.usuarioId.isNullOrBlank()) {
                val usuarios = usuarios()
                    .map { usuariosEncontrados ->
                        usuariosEncontrados.map { it.id }
                    }.first()
                if (usuarioExistenteValido(usuarios)) {
                    val campoUsuarioId =
                        binding.activityFormularioProdutoUsuarioId
                    return campoUsuarioId.toString()//text.toString()
                } else {
                    throw RuntimeException("Tentou salvar produto com usuário inexistente")
                }
            }
            null
        } ?: usuario.id

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
        val campoUsuarioId = binding.activityFormularioProdutoUsuarioId

        val usuarioId = campoUsuarioId.getEditText()?.text



        return Produto(
            id = idProduto,
            nome = nome,
            descricao = descricao,
            valor = valor,
            imagem = url,
            usuarioId = usuarioId?.toString()
        )

    }
}