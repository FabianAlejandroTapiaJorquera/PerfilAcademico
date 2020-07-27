package com.example.perfilacademico

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.add
import com.bumptech.glide.Glide
import com.example.perfilacademico.repositorios.subir.BaseDatosUniversidad
import com.example.perfilacademico.repositorios.subir.SubirUniversidad
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_host.*
import kotlinx.android.synthetic.main.fragment_perfil_universidad.*

class PerfilUniversidadFragment : Fragment() {

    //Variable global
    private var universidad: Array<String>? = null
    private val GALLERY_PICK_INTENT by lazy { 1 }

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

        setearVista()
        activarAjustes()
        desactivarAjustes()
        subirNuevosDatosUniversidad()
    }

    fun setearVista(){
        nombreUniversidad.text = universidad!![0]
        direccionUniversidad.text = universidad!![2]
        if(universidad!![3] != "")
            Glide.with(this).load(universidad!![3]).into(universidadLogoPF)
    }

    //Funcionalidad de los botones
    fun activarAjustes(){
        //Botón de ajustes
        editar.setOnClickListener {
            //Cambiando a visible los botones de ajuste
            cerrarEditar.visibility = View.VISIBLE
            editarImagenPerfil.visibility = View.VISIBLE
            editarDatosUniversidad.visibility = View.VISIBLE
            editar.visibility = View.GONE
        }
        //Funciones de ajuste
        cambiarLogoUniversidad()
        cambiarDatosUniversidad()
    }
    fun desactivarAjustes(){
        cerrarEditar.setOnClickListener {
            //Volviendo a la visibilidad normal
            cerrarEditar.visibility = View.GONE
            editar.visibility = View.VISIBLE
            editarImagenPerfil.visibility = View.GONE
            editarDatosUniversidad.visibility = View.GONE
            nombreUniversidad.visibility = View.VISIBLE
            direccionUniversidad.visibility = View.VISIBLE
            nuevoNombreUniversidadET.visibility = View.GONE
            nuevaDireccionUniversidadET.visibility = View.GONE
            aceptarCambios.visibility = View.GONE
        }
    }
    fun cambiarDatosUniversidad(){
        editarDatosUniversidad.setOnClickListener {
            //Ocultando los campos con información
            nombreUniversidad.visibility = View.GONE
            direccionUniversidad.visibility = View.GONE
            editarDatosUniversidad.visibility  = View.GONE
            //Mostrando los EditText
            nuevoNombreUniversidadET.visibility = View.VISIBLE
            nuevaDireccionUniversidadET.visibility = View.VISIBLE
            aceptarCambios.visibility = View.VISIBLE
            //Rellenando los EditText con los datos actuales
            nuevoNombreUniversidadET.setText(universidad!![0])
            nuevaDireccionUniversidadET.setText(universidad!![2])
        }
    }
    fun subirNuevosDatosUniversidad(){
        aceptarCambios.setOnClickListener {
            val nuevosDatos = BaseDatosUniversidad(SubirUniversidad(Universidad(nuevoNombreUniversidadET.text.toString(), universidad!![1], nuevaDireccionUniversidadET.text.toString(), universidad!![3], nuevoNombreUniversidadET.text.toString().toLowerCase())))
            nuevosDatos.agregarUniversidad()

            //Volviendo la vista a la normalidad
            direccionUniversidad.visibility = View.VISIBLE
            editarDatosUniversidad.visibility  = View.VISIBLE
            editarDatosUniversidad.visibility = View.VISIBLE
            nuevoNombreUniversidadET.visibility = View.GONE
            nuevaDireccionUniversidadET.visibility = View.GONE
            aceptarCambios.visibility = View.GONE
            editarImagenPerfil.visibility = View.GONE
            editarDatosUniversidad.visibility = View.GONE
            cerrarEditar.visibility = View.GONE

            //Cerrando Teclado
            view?.cerrarTeclado()

            //Actualizando vista
            updateUI(nuevoNombreUniversidadET.text.toString(), nuevaDireccionUniversidadET.text.toString())
        }
    }
    fun cambiarLogoUniversidad(){
        editarImagenPerfil.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, GALLERY_PICK_INTENT)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GALLERY_PICK_INTENT && resultCode == RESULT_OK) {
            var url: String
            val uri = data?.data
            val referencia = FirebaseStorage.getInstance().getReference().child("fotos").child(universidad!![1])
            referencia.putFile(uri!!).addOnSuccessListener {
                referencia.downloadUrl.addOnCompleteListener {
                    url = it.result.toString()
                    val nuevaImagen = BaseDatosUniversidad(SubirUniversidad(Universidad(universidad!![0], universidad!![1], universidad!![2], url, universidad!![4])))
                    nuevaImagen.agregarUniversidad()
                    Glide.with(this).load(url).into(universidadLogoPF)
                }
            }
            Toast.makeText(requireContext(), "Se ha actualizado la foto de perfil", Toast.LENGTH_SHORT).show()
        }
    }

    fun View.cerrarTeclado(){
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
    fun updateUI(nombre: String, direccion: String){
        val array = arrayOf(nombre, universidad!![1], direccion, universidad!![3], nombre.toLowerCase())
        val fragment = PerfilUniversidadFragment()
        val bundle = Bundle()
        bundle.putStringArray("universidad", array)
        val transaction = parentFragmentManager.beginTransaction()
        fragment.arguments = bundle
        transaction.remove(this).add(R.id.fragment, fragment).commit()
    }
}