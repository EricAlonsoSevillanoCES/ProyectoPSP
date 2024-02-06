package com.example.proyectoyoutube

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import com.example.proyectoyoutube.databinding.ActivityRegistroBinding
import com.google.android.material.snackbar.Snackbar
import java.security.MessageDigest


class RegistroActivity : AppCompatActivity(), OnClickListener {

    private lateinit var binding : ActivityRegistroBinding

    private lateinit var passHash : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.botonGuardar.setOnClickListener(this)
    }

    override fun onClick(v: View?) {

        when(v?.id){

            R.id.botonGuardar -> {
                var usuario : String = binding.editUsuario.text.toString()
                var pass : String = binding.editPassRegistro.text.toString()
                var passRepetida : String = binding.editPassRepeatRegistro.text.toString()

                if(pass == passRepetida){



                    val sha512 = MessageDigest.getInstance("SHA-512")
                    val bytes = (pass).toByteArray()

                    passHash = sha512.digest(bytes).toString()

                    var intent : Intent = Intent(applicationContext, LoginActivity::class.java)
                    intent.putExtra("nombrePasado", usuario)
                    intent.putExtra("passHaseada", passHash)

                    startActivity(intent)


                }else{
                    Snackbar.make(binding.root, "Las contraseñas no son iguales", Snackbar.LENGTH_SHORT).show()
                }

            }

        }

    }


}