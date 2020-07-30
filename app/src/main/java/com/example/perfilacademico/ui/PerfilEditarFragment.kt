package com.example.perfilacademico.ui

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
import com.bumptech.glide.Glide
import com.example.perfilacademico.R
import com.example.perfilacademico.Universidad
import com.example.perfilacademico.repositorios.subir.BaseDatosUniversidad
import com.example.perfilacademico.repositorios.subir.SubirUniversidad
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_host.*
import kotlinx.android.synthetic.main.fragment_perfil_editar.*

class PerfilEditarFragment : Fragment() {

    //Firestore
    private val referencia by lazy { FirebaseFirestore.getInstance().collection("universidades") }
    private lateinit var listener: ListenerRegistration

    //Variable global
    private var universidad: Array<String>? = null
    private val GALLERY_PICK_INTENT by lazy { 1 }
    private val editarDialog by lazy { EditarDatosFragment() }


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
        return inflater.inflate(R.layout.fragment_perfil_editar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Funciones
        setearVista()
        editarLogoUniversidad()
        editarDatosUniversidad()
        eliminarUniversidad()
    }
    fun setearVista(){
        //Obteniendo información en tiempo real desde Firestore
        listener = referencia.whereEqualTo("codigo", universidad!![1]).addSnapshotListener { snapshot, error ->
            if (error != null) {
                Toast.makeText(requireContext(), "Error al cargar el perfil", Toast.LENGTH_SHORT).show()
            }
            if (snapshot != null) {
                val universidad = snapshot.documents.get(0).toObject(Universidad::class.java)!!
                nombreUniversidadEditar.text = universidad.nombre
                direccionUniversidadEditar.text = universidad.direccion
                if(universidad.logo != "")
                    Glide.with(this).load(universidad.logo).into(universidadFotoPerfil)
            }
        }
    }
    //Funciones de los botones
    fun editarLogoUniversidad(){
        editarLogoBTN.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, GALLERY_PICK_INTENT)
        }
    }
    fun editarDatosUniversidad(){
        val bundle = Bundle()
        editarNombreUniversidad.setOnClickListener {
            val array = arrayOf(nombreUniversidadEditar.text.toString(),universidad!![1], direccionUniversidadEditar.text.toString(), universidad!![3], nombreUniversidadEditar.text.toString().toLowerCase(), "nombre")
            bundle.putStringArray("universidad",array)
            editarDialog.arguments = bundle
            editarDialog.show(parentFragmentManager, "Editar Nombre")
        }
        editarDireccionUniversidad.setOnClickListener {
            val array = arrayOf(nombreUniversidadEditar.text.toString(), universidad!![1], direccionUniversidadEditar.text.toString(), universidad!![3], nombreUniversidadEditar.text.toString().toLowerCase(), "direccion")
            bundle.putStringArray("universidad",array)
            editarDialog.arguments = bundle
            editarDialog.show(parentFragmentManager, "Editar Direccion")
        }
    }
    fun eliminarUniversidad(){
        eliminarUniversidad.setOnClickListener {
            confirmacionEliminar.visibility = View.VISIBLE
            okEliminar.visibility = View.VISIBLE
        }
        okEliminar.setOnClickListener {
            if(confirmacionEliminar.text.toString() != nombreUniversidadEditar.text.toString())
                Toast.makeText(requireContext(), "No se han Borrado los datos", Toast.LENGTH_SHORT).show()
            else{
                val fragment = InicioFragment()
                referencia.document(universidad!![1]).delete()
                parentFragmentManager.popBackStack()
                parentFragmentManager.popBackStack()
                parentFragmentManager.beginTransaction().add(R.id.fragment, fragment).commit()
                requireView().cerrarTeclado()
            }
        }
    }

    fun actualizarUI(url: String){
        val fragment = PerfilEditarFragment()
        val array = universidad!!
        array[3] = url
        val bundle = Bundle()
        bundle.putStringArray("universidad", array)
        fragment.arguments = bundle
        parentFragmentManager.beginTransaction().remove(this).replace(R.id.fragment, fragment).commit()
    }

    fun View.cerrarTeclado(){
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
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
                    val nuevaImagen = BaseDatosUniversidad(SubirUniversidad(Universidad(nombreUniversidadEditar.text.toString(), universidad!![1], direccionUniversidadEditar.text.toString(), url, nombreUniversidadEditar.text.toString().toLowerCase())))
                    nuevaImagen.agregarUniversidad()
                    Glide.with(this).load(url).into(universidadFotoPerfil)

                    //Actualizando el bundle de la UI
                    actualizarUI(url)
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        listener.remove()
    }

}