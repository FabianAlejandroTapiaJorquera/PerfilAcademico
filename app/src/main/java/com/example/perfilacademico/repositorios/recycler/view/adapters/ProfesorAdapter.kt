package com.example.perfilacademico.repositorios.recycler.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.perfilacademico.Profesor
import com.example.perfilacademico.R
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
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
            var notaProfesor = 0.0
            var cantRamos = profesor.ramos.size
            profesor.notas.forEach { nota-> notaProfesor = notaProfesor + nota.toInt() }
            notaProfesor = (notaProfesor/profesor.notas.size)
            val porcentajeAprobados = ((profesor.aprobacion / (profesor.aprobacion + profesor.reprobacion).toFloat() * 100)).toInt()
            val porcentajeReprobacion = 100 - porcentajeAprobados

            //Set datos
            itemView.nombreProfesor.text = profesor.nombre
            itemView.asignaturasProfesor.text = cantRamos.toString()
            itemView.aprobadosProfesorPorcentual.text = porcentajeAprobados.toString() + "%"
            itemView.reprobadosProfesorPorcentual.text = porcentajeReprobacion.toString() +"%"
            itemView.notaProfesor.text = formato.format(notaProfesor).toString()
        }
    }
}