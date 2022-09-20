package com.wamser.orgs.extensions

import android.widget.ImageView
import coil.load

fun ImageView.tentaCarregarImagem(url: String? = null){
    load(url) {
        fallback(com.wamser.orgs.R.drawable.erro)
        error(com.wamser.orgs.R.drawable.erro)
        placeholder(com.wamser.orgs.R.drawable.placeholder)
    }
}