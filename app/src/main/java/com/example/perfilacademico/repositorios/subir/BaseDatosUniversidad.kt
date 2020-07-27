package com.example.perfilacademico.repositorios.subir

class BaseDatosUniversidad(private val universidad: ISubirUniversidad) {
    fun agregarUniversidad(){
        universidad.subirUniversidad()
    }
}