package com.wamser.orgs.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal

@Entity
@Parcelize
class Produto(
    @PrimaryKey(autoGenerate = true) val id:Long = 0L,
    val nome: String?,
    val descricao: String?,
    val valor: BigDecimal,
    val imagem:String? = null,
    val usuarioId: String? = null
) : Parcelable {

    fun salvoSemUsuario() = usuarioId.isNullOrBlank() &&
            id > 0L

}