package com.example.proyectoyoutube

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.proyectoyoutube.databinding.ActivityBatallaBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import java.net.DatagramPacket
import java.net.InetAddress
import java.net.MulticastSocket


class BatallaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBatallaBinding
    private var valorCombate: Int = 0
    private var multicastJob: Job? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBatallaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        valorCombate = intent.getIntExtra("valor_combate", 0)

        binding.textoCombate.text = "Esperando al oponente.\nTus puntos de Combate: $valorCombate"

        // Corutina para iniciar el metodo del multicast (Borja)
        multicastJob = CoroutineScope(Dispatchers.IO).launch {
            iniciarMulticast(valorCombate.toString())
        }
    }

    //Corrutina
    // live cycle coorutina

    private suspend fun iniciarMulticast(mensaje: String) {
        val multigrupo = "224.0.0.251"
        val puerto = 1234

        try {
            val direccion = InetAddress.getByName(multigrupo)
            val socketmulti = MulticastSocket(puerto)
            socketmulti.joinGroup(direccion)

            val mensajeb = mensaje.toByteArray()
            val enviar = DatagramPacket(mensajeb, mensajeb.size, direccion, puerto)
            socketmulti.send(enviar)

            while (true) {
                val array = ByteArray(256)
                val recibir = DatagramPacket(array, array.size)
                socketmulti.receive(recibir)
                val mensajec = String(recibir.data, recibir.offset, recibir.length)


                // Cambia al contexto de hilo (Borja)
                withContext(Dispatchers.Main) {
                    procesarMensaje(mensajec)

                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun procesarMensaje(mensaje: String) {
        val puntosOponente = mensaje.toIntOrNull()
        if (puntosOponente != null) {
            mostrarResultadoBatalla(puntosOponente)
        }
    }

    private fun mostrarResultadoBatalla(puntosOponente: Int) {
        Log.d("TAG", "valorCombate: $valorCombate, puntosOponente: $puntosOponente")

        val resultado = if (valorCombate > puntosOponente) {
            "¡Has ganado!"
        } else if (valorCombate < puntosOponente) {
            "¡Has perdido!"
        } else {
            "¡Es un empate!"
        }

        if (valorCombate > puntosOponente) {
            binding.textoCombate.text = "Has ganado!"
            binding.imagenCombate.setImageResource(R.drawable.victoria)
        } else if (valorCombate < puntosOponente) {
            binding.textoCombate.text = "¡Has perdido!"
            binding.imagenCombate.setImageResource(R.drawable.derrota)
        } else {
            binding.textoCombate.text = "¡Es un empate!"
            binding.imagenCombate.setImageResource(R.drawable.juegolucha)
        }

        Snackbar.make(binding.root, resultado, Snackbar.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        // asegura que la tarea asociada a la corutina se detenga
        // y libere recursos cuando la actividad se destruye
        multicastJob?.cancel()
    }


}