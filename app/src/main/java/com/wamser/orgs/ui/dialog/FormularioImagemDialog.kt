package com.wamser.orgs.ui.dialog

import android.content.Context
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import com.wamser.orgs.databinding.FormularioImagemBinding
import com.wamser.orgs.extensions.tentaCarregarImagem

class FormularioImagemDialog (private val context: Context) {

    fun mostra(urlPadrao:String? = null,quandoImagemCarregada: (imagem:String) -> Unit){

        val binding = FormularioImagemBinding
            .inflate(LayoutInflater.from(context)).apply {

                urlPadrao?.let {
                    formularioImagemImageview.tentaCarregarImagem(it)
                    formularioImagemUrl.setText(it)

                }
                formularioImagemBotaoCarregar.setOnClickListener {
                    val url = formularioImagemUrl.text.toString()
                    formularioImagemImageview.tentaCarregarImagem(url)
                }
                AlertDialog.Builder(context)
                    .setView(root)
                    .setPositiveButton("Confirmar") { _, _ ->
                        val url = formularioImagemUrl.text.toString()
                        quandoImagemCarregada(url)

                    }
                    .setNegativeButton("Cancelar") { _, _ ->

                    }
                    .show()
            }



    }

}