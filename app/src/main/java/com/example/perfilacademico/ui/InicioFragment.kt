package com.example.perfilacademico.ui

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import androidx.fragment.app.Fragment
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.perfilacademico.R
import com.example.perfilacademico.Universidad
import com.example.perfilacademico.repositorios.recycler.view.adapters.UniversidadAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_inicio.*

class InicioFragment : Fragment(), UniversidadAdapter.OnUniversidadClickListener {

    //Query de búsqueda en firestore
    val query = FirebaseFirestore.getInstance().collection("universidades")
    var options: FirestoreRecyclerOptions<Universidad> = FirestoreRecyclerOptions.Builder<Universidad>()
        .setQuery(query, Universidad::class.java)
        .build()

    //Inicializaciones de adaptadores
    private val universidadAdapter by lazy { UniversidadAdapter(requireContext(), this, options) }

    //Variables
    private val dialogoSubirUniversidad by lazy { SubirUniversidadFragment() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_inicio, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        contenedorUniversidades.layoutManager = LinearLayoutManager(context)
        contenedorUniversidades.adapter = universidadAdapter

        //Funciones
        activarBusqueda()
        desactivarBusqueda()
        funcionalidadBotones()

    }

    //Buscar una universidad por su nombre
    fun activarBusqueda(){
        buscarUniversidad.setOnClickListener {
            buscar()
        }


    }
    fun desactivarBusqueda(){
        cerrarBuscarUniversidad.setOnClickListener {
            buscarUniversidadET.visibility = View.GONE
            buscarUniversidadET.text = null
            buscarUniversidad.visibility = View.VISIBLE
            agregarUniversidad.visibility = View.VISIBLE
            cerrarBuscarUniversidad.visibility = View.GONE
            nombreApp.visibility = View.VISIBLE

            //Volviendo a mostrar todas las universidades
            universidadAdapter.updateOptions(options)
            universidadAdapter.notifyDataSetChanged()

            //Cerrando teclado
            view?.cerrarTeclado()
        }
    }
    fun buscar(){
        //Barra de búsqueda visible
        nombreApp.visibility = View.GONE
        buscarUniversidadET.visibility = View.VISIBLE
        buscarUniversidad.visibility = View.GONE
        agregarUniversidad.visibility = View.GONE
        cerrarBuscarUniversidad.visibility = View.VISIBLE
        //Capturando caracteres de la barra de búsqueda
        buscarUniversidadET.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(texto: Editable?) {
                //Nueva query de búsqueda
                val queryBusqueda = FirebaseFirestore.getInstance().collection("universidades").whereGreaterThanOrEqualTo("claveBusqueda", texto.toString().toLowerCase())
                var optionsBusqueda: FirestoreRecyclerOptions<Universidad> = FirestoreRecyclerOptions.Builder<Universidad>()
                    .setQuery(queryBusqueda, Universidad::class.java)
                    .build()
                //Actualizando el adaptador del recyclerView
                universidadAdapter.updateOptions(optionsBusqueda)
                universidadAdapter.notifyDataSetChanged()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(texto: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    fun View.cerrarTeclado(){
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    fun funcionalidadBotones(){
        agregarUniversidad.setOnClickListener {
            dialogoSubirUniversidad.show(parentFragmentManager, "Agregar Universidad")
        }
    }

    override fun OnItemClick(universidad: Universidad) {
        val array = arrayOf(universidad.nombre, universidad.codigo, universidad.direccion, universidad.logo, universidad.claveBusqueda)
        val bundle = Bundle()
        bundle.putStringArray("universidad", array)
        findNavController().navigate(R.id.perfilUniversidadFragment, bundle)
    }
    override fun onStart() {
        super.onStart()
        universidadAdapter.startListening()
    }
    override fun onStop() {
        super.onStop()
        universidadAdapter.stopListening()
    }
}