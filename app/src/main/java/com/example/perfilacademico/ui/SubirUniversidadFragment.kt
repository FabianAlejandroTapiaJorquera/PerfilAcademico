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
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.perfilacademico.R
import com.example.perfilacademico.Universidad
import com.example.perfilacademico.repositorios.subir.BaseDatosUniversidad
import com.example.perfilacademico.repositorios.subir.SubirUniversidad
import kotlinx.android.synthetic.main.fragment_subir_universidad.*
import kotlin.random.Random

class SubirUniversidadFragment : DialogFragment() {

    //Generación del código de cada institución
    private val letras = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    private val codigo by lazy { generarCodigo() }

    //Variables
    private lateinit var subir: Button
    private lateinit var descartar: Button
    private lateinit var nombre: EditText
    private lateinit var direccion: EditText

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return crearDialogo()
    }

    private fun crearDialogo(): AlertDialog {

        val builder = AlertDialog.Builder(activity)
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.fragment_subir_universidad, null)
        builder.setView(view)

        //Instanciando elementos
        instanciarObjetos(view)

        //Llamando a la funcionalidad de los botones
        funcionalidadBotones()

        return builder.create()
    }

    fun generarCodigo(): String{
        //Código único para cada institución
        var codigo = ""
        for(i in 0..10)
            codigo = codigo + letras[Random.nextInt(0, letras.size)]
        return codigo
    }
    fun funcionalidadBotones(){
        //Acciones a ejecutarse al tocar en los botones
        subir.setOnClickListener {
            val subir = BaseDatosUniversidad(SubirUniversidad(Universidad(nombre.text.toString(), codigo, direccion.text.toString(), "", nombre.text.toString().toLowerCase())))
            subir.agregarUniversidad()
            Toast.makeText(requireContext(), "Se ha agredado una nueva institución", Toast.LENGTH_SHORT).show()
            dismiss()
        }
        descartar.setOnClickListener {
            Toast.makeText(requireContext(), "Se descartaron los datos", Toast.LENGTH_SHORT).show()
            dismiss()
        }
    }
    fun instanciarObjetos(view: View){
        subir = view.findViewById(R.id.agregarUniversidadBTN)
        descartar = view.findViewById(R.id.descartarUniversidadBTN)
        nombre = view.findViewById(R.id.nombreNuevaUniversidad)
        direccion = view.findViewById(R.id.direccionNuevaUniversidad)
    }
}