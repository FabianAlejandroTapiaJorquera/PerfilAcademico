package com.example.perfilacademico.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.perfilacademico.Profesor
import com.example.perfilacademico.R
import com.example.perfilacademico.Universidad
import com.example.perfilacademico.repositorios.recycler.view.adapters.ProfesorAdapter
import com.example.perfilacademico.repositorios.recycler.view.adapters.UniversidadAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.android.synthetic.main.fragment_inicio.*
import kotlinx.android.synthetic.main.fragment_perfil_editar.*
import kotlinx.android.synthetic.main.fragment_perfil_universidad.*
import kotlinx.android.synthetic.main.fragment_perfil_universidad.universidadFotoPerfil

class PerfilUniversidadFragment : Fragment() {

    //Firestore
    private val referencia by lazy { FirebaseFirestore.getInstance().collection("universidades") }
    private lateinit var listener: ListenerRegistration
    //Query de búsqueda en firestore
    val query = FirebaseFirestore.getInstance().collection("profesores")
    var options: FirestoreRecyclerOptions<Profesor> = FirestoreRecyclerOptions.Builder<Profesor>()
        .setQuery(query, Profesor::class.java)
        .build()

    //Variable global
    private var universidad: Array<String>? = null
    private val profesorAdapter by lazy { ProfesorAdapter(requireContext(), options) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {informacion ->
            universidad = informacion.getStringArray("universidad")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_perfil_universidad, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Funciones
        setearVista()
        setRecyclerView()
        editarPerfil()
    }

    fun setRecyclerView(){
        contenedorProfesores.layoutManager = LinearLayoutManager(context)
        contenedorProfesores.adapter = profesorAdapter
    }

    fun setearVista(){
        //Obteniendo información en tiempo real desde Firestore
        listener = referencia.whereEqualTo("codigo", universidad!![1]).addSnapshotListener { snapshot, error ->
            if (error != null) {
                Toast.makeText(requireContext(), "Error al cargar el perfil", Toast.LENGTH_SHORT).show()
            }
            if (snapshot != null) {
                val universidad = snapshot.documents.get(0).toObject(Universidad::class.java)!!
                universidadNombre.text = universidad.nombre
                direccionUniversidad.text = universidad.direccion
                if(universidad.logo != "")
                    Glide.with(this).load(universidad.logo).into(universidadFotoPerfil)
            }
        }
    }
    fun editarPerfil(){
        //Función del boton editar
        editarPerfilUniversidadBTN.setOnClickListener {
            val bundle = Bundle()
            bundle.putStringArray("universidad", universidad!!)
            findNavController().navigate(R.id.perfilEditarFragment, bundle)
        }
    }

    override fun onStart() {
        super.onStart()
        profesorAdapter.startListening()
    }
    override fun onStop() {
        super.onStop()
        profesorAdapter.stopListening()
        listener.remove()
    }
}