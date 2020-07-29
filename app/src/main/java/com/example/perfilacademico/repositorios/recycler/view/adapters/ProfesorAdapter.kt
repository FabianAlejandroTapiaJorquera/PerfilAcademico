package com.example.perfilacademico.repositorios.recycler.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.RecyclerView
import com.example.perfilacademico.Profesor
import com.example.perfilacademico.R
import com.example.perfilacademico.Universidad
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.profesor.view.*
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.round
import kotlin.random.Random

class ProfesorAdapter(private val context: Context,
                      options: FirestoreRecyclerOptions<Profesor>
): FirestoreRecyclerAdapter<Profesor, ProfesorAdapter.ViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfesorAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.profesor, null, false))
    }

    override fun onBindViewHolder(holder: ProfesorAdapter.ViewHolder, position: Int, profesor: Profesor) {
        when(holder){
            is ProfesorAdapter.ViewHolder ->{
                holder.llenarRecycler(profesor)
            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        //Llenando el layout profesor
        fun llenarRecycler(profesor: Profesor){
            //Formato de 2 decimales
            val formato = DecimalFormat("#.##")
            formato.roundingMode = RoundingMode.CEILING
            //Textos
            var ramosTexto = "Asignaturas: "
            var notaProfesor = 0.0
            var comentario = profesor.comentarios[Random.nextInt(0, profesor.comentarios.size)]
            profesor.ramos.forEach { ramo-> ramosTexto = ramosTexto + ramo + "," }
            profesor.notas.forEach { nota-> notaProfesor = notaProfesor + nota.toInt() }
            notaProfesor = (notaProfesor/profesor.notas.size)
            val porcentajeAprobados = ((profesor.aprobacion / (profesor.aprobacion + profesor.reprobacion).toFloat() * 100)).toInt()
            val porcentajeReprobacion = 100 - porcentajeAprobados

            //Set datos
            itemView.nombreProfesor.text = profesor.nombre
            itemView.asignaturasProfesor.text = ramosTexto
            itemView.aprobadosProfesor.text = profesor.aprobacion.toString()
            itemView.reprobadosProfesor.text = profesor.reprobacion.toString()
            itemView.aprobadosProfesorPorcentual.text = porcentajeAprobados.toString() + "%"
            itemView.reprobadosProfesorPorcentual.text = porcentajeReprobacion.toString() +"%"
            itemView.notaProfesor.text = "Nota: "+ formato.format(notaProfesor).toString()
        }
    }
}