package com.example.perfilacademico

data class Universidad(val nombre: String, val codigo: String, val direccion: String, val logo: String, val claveBusqueda: String) {
    constructor() : this("", "", "", "", "")
}