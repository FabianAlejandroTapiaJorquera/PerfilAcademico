package com.example.perfilacademico.repositorios.subir

import com.example.perfilacademico.Universidad
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class SubirUniversidad(val universidad: Universidad): ISubirUniversidad {

    override fun subirUniversidad() {
        FirebaseFirestore.getInstance().collection("universidades").document(universidad.codigo).set(universidad, SetOptions.merge())
    }
}