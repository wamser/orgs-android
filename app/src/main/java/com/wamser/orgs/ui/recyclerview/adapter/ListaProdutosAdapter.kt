package com.wamser.orgs.ui.recyclerview.adapter
import android.content.Context
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.wamser.orgs.R
import com.wamser.orgs.databinding.ProdutoItemBinding
import com.wamser.orgs.extensions.tentaCarregarImagem
import com.wamser.orgs.model.Produto
import com.wamser.orgs.ui.activity.TAG
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.*

class ListaProdutosAdapter(
    private val context: Context,
    produtos: List<Produto> = emptyList(),
    var quandoClicaNoItemListener: (produto: Produto) -> Unit = {}
) : RecyclerView.Adapter<ListaProdutosAdapter.ViewHolder>() {

    private val produtos = produtos.toMutableList()

       inner class ViewHolder(private val binding: ProdutoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private lateinit var produto: Produto

        init {
            itemView.setOnClickListener {
                if(::produto.isInitialized) {
                    quandoClicaNoItemListener(produto)
                }
            }

            itemView.setOnLongClickListener {
                Log.i("setOnLongClickListener", "clicando e segurando no item")
                showPopup(itemView)


                return@setOnLongClickListener true
            }

        }
           fun showPopup(v: View) {
               val popup = PopupMenu(context, v)
               val inflater: MenuInflater = popup.menuInflater
               inflater.inflate(R.menu.menu_detalhes_produto, popup.menu)

               popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem? ->

                   when (item!!.itemId) {

                       R.id.menu_detalhes_produto_editar -> {
                           Log.i("setOnLongClickListener", "Editar item")
                       }
                       R.id.menu_detalhes_produto_excluir -> {
                           Log.i("setOnLongClickListener", "Excluir item")
                       }
                   }

                   true
               })
           popup.show()
           }


           fun vincula(produto: Produto) {
            this.produto = produto
            val nome = binding.produtoItemNome
            nome.text = produto.nome
            val descricao = binding.produtoItemDescricao
            descricao.text = produto.descricao
            val valor = binding.produtoItemValor
            val valorEmMoeda: String =
                formataParaMoedaBrasileira(produto.valor)
            valor.text = valorEmMoeda

            val visibilidade = if(produto.imagem != null){
                View.VISIBLE
            } else {
                View.GONE
            }

            binding.imageView.visibility = visibilidade

            binding.imageView.tentaCarregarImagem(produto.imagem)
        }


        private fun formataParaMoedaBrasileira(valor: BigDecimal): String {
            val formatador: NumberFormat = NumberFormat
                .getCurrencyInstance(Locale("pt", "br"))
            return formatador.format(valor)
        }

    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val binding = ProdutoItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val produto = produtos[position]
        holder.vincula(produto)
    }

    override fun getItemCount(): Int = produtos.size

    fun atualiza(produtos: List<Produto>) {
        this.produtos.clear()
        this.produtos.addAll(produtos)
        notifyDataSetChanged()
    }


}