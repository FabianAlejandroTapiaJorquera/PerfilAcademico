package com.example.perfilacademico.ui

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.perfilacademico.R
import com.example.perfilacademico.Universidad
import com.example.perfilacademico.repositorios.subir.BaseDatosUniversidad
import com.example.perfilacademico.repositorios.subir.SubirUniversidad
import kotlinx.android.synthetic.main.fragment_perfil_editar.*

class EditarDatosFragment : DialogFragment() {

    //Variables
    private lateinit var aceptar: Button
    private lateinit var cancelar: Button
    private lateinit var datoEditar: EditText
    private var universidad: Array<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {informacion ->
            universidad = informacion.getStringArray("universidad")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return crearDialogo()
    }

    fun crearDialogo(): AlertDialog {

        val builder = AlertDialog.Builder(activity)
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.fragment_editar_datos, null)
        builder.setView(view)

        //Instancia de objetos
        instanciarObjetos(view)

        //Set de datos
        setearDatos()

        //Llamando a la funcionalidad de los botones
        funcionalidadBotones()

        return builder.create()
    }

    fun instanciarObjetos(view: View){
        aceptar = view.findViewById(R.id.botonAceptarEDU)
        cancelar = view.findViewById(R.id.botonDescartarEDU)
        datoEditar = view.findViewById(R.id.datoEditar)
    }

    private fun setearDatos(){
        if(universidad!![5] == "nombre")
            datoEditar.setText(universidad!![0])
        if(universidad!![5] == "direccion")
            datoEditar.setText(universidad!![2])
    }

    private fun funcionalidadBotones() {
        cancelar.setOnClickListener { dismiss() }
        aceptar.setOnClickListener { actualizarDato() }
    }

    fun actualizarDato(){
        if(universidad!![5] == "nombre"){
            val nuevosDatos = BaseDatosUniversidad(SubirUniversidad(Universidad(datoEditar.text.toString(), universidad!![1], universidad!![2], universidad!![3], datoEditar.text.toString().toLowerCase())))
            nuevosDatos.agregarUniversidad()
            dismiss()
        }
        if(universidad!![5] == "direccion"){
            val nuevosDatos = BaseDatosUniversidad(SubirUniversidad(Universidad(universidad!![0], universidad!![1], datoEditar.text.toString(), universidad!![3], universidad!![4])))
            nuevosDatos.agregarUniversidad()
            dismiss()
        }
    }
}