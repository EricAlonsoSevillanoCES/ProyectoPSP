package com.example.proyectoyoutube

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import com.example.proyectoyoutube.databinding.ActivityLoginBinding
import com.google.android.material.snackbar.Snackbar
import java.security.MessageDigest

class LoginActivity : AppCompatActivity(), OnClickListener {

    private lateinit var  binding : ActivityLoginBinding

    private lateinit var nombre : String

    private lateinit var  pass : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.botonEntrar.setOnClickListener(this)
        binding.textoRegistro.setOnClickListener(this)


    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.botonEntrar -> {
                var nombreIntroducido = binding.editNombre.text.toString()
                var passIntroducida = binding.editPass.text.toString()

                val sha512 = MessageDigest.getInstance("SHA-512")
                val bytes = (passIntroducida).toByteArray()

                val passHash = sha512.digest(bytes).joinToString("") { "%02x".format(it) }

                nombre = intent.getStringExtra("nombrePasado").toString()
                pass = intent.getStringExtra("passHaseada").toString()


                if (nombre == nombreIntroducido && pass == passHash){

                    var intent : Intent = Intent(applicationContext, SuperHeroListActivity::class.java)
                    startActivity(intent)

                }else{
                    Snackbar.make(binding.root, "Contraseña o Usuario incorrectos",
                        Snackbar.LENGTH_SHORT).show()
                }

            }
            R.id.textoRegistro -> {

                var intent : Intent = Intent(applicationContext, RegistroActivity::class.java)
                startActivity(intent)

            }
        }
    }
}