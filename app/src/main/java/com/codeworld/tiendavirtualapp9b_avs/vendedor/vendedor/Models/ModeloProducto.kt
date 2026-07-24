package com.codeworld.tiendavirtualapp9b_avs.vendedor.vendedor.Models

class ModeloProducto {
    var id: String = ""
    var nombre: String = ""
    var descripcion: String = ""
    var precio: String = ""
    var uidVendedor: String = ""
    var tiempoRegistro: Long = 0

    constructor()

    constructor(
        id: String,
        nombre: String,
        descripcion: String,
        precio: String,
        uidVendedor: String,
        tiempoRegistro: Long
    ) {
        this.id = id
        this.nombre = nombre
        this.descripcion = descripcion
        this.precio = precio
        this.uidVendedor = uidVendedor
        this.tiempoRegistro = tiempoRegistro
    }
}