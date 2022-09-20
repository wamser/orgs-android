package com.wamser.orgs.dao

import com.wamser.orgs.model.Produto
import java.math.BigDecimal

class ProdutosDao {

    fun adiciona(produto: Produto) {
        produtos.add(produto)
    }

    fun buscaTodos(): List<Produto> {
        return produtos.toList()
    }

    companion object {
        private val produtos = mutableListOf<Produto>(
            /*Produto(
                nome = "Salada de frutas",
                descricao = "Laranjas, Maças e Uvas",
                valor = BigDecimal("53.29")
            )*/
        )
    }

}