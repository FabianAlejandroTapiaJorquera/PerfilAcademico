package com.example.perfilacademico

data class Profesor(val nombre: String, val notas: ArrayList<Float>, val aprobacion: Int,
                    val reprobacion: Int, val comentarios: ArrayList<String>,
                    val ramos: ArrayList<String>, val universidades: ArrayList<String>
){
    constructor() : this("", arrayListOf(), 0, 0, arrayListOf(), arrayListOf(), arrayListOf())
}