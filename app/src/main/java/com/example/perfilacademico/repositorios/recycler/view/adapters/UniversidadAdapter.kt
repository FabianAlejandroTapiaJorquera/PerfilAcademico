package com.example.perfilacademico.repositorios.recycler.view.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.perfilacademico.R
import com.example.perfilacademico.Universidad
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kotlinx.android.synthetic.main.universidad.view.*

class UniversidadAdapter(private val context: Context,
                         private val itemClickListener: OnUniversidadClickListener,
                         options: FirestoreRecyclerOptions<Universidad>
): FirestoreRecyclerAdapter<Universidad, UniversidadAdapter.ViewHolder>(options) {

    //Click sobre los RecyclerView
    interface OnUniversidadClickListener{
        fun OnItemClick(universidad: Universidad)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.universidad, null, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, universidad: Universidad) {
        when(holder){
            is UniversidadAdapter.ViewHolder ->{
                holder.llenarRecycler(universidad)
            }
        }
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        //Función para rellenar los elementos de cada layout del recyclerView
        fun llenarRecycler(universidad: Universidad) {
            itemView.setOnClickListener { itemClickListener.OnItemClick(universidad) }
            itemView.nombreUniversidad.text = universidad.nombre
            itemView.direccionUniversidad.text = universidad.direccion
            if(universidad.logo != "")
                Glide.with(context).load(universidad.logo).into(itemView.universidadLogo)
            else
                Glide.with(context).load(R.drawable.perfil).into(itemView.universidadLogo)
        }
    }

}