package com.alejandroarriola.tpgrupo2

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.viewmodel.compose.viewModel
import java.util.Calendar

//dibuja la pantalla: por una parte el formulario de registro, y debjao la lista de autos registrados (no se muestra al arrancar el app por no tiener registros aun)
@Composable
fun PantallaRegistro() {
    val listaAutos = viewModel<AutoViewModel>() //viewmodel para que la lista no sea eliminada tras un cambio de configuracion, i.e. cambio de rotacion o dark mode
    var marca by rememberSaveable { mutableStateOf("") } //remembersaveable para que persiste tras reconfiguracion de pantalla *i.e. dark mode)
    var modelo by rememberSaveable { mutableStateOf("") }
    var anho by rememberSaveable { mutableStateOf("") }
    var precio by rememberSaveable { mutableStateOf("") }
    var kilometraje by rememberSaveable { mutableStateOf("") }
    var imagenURL by rememberSaveable { mutableStateOf("") }
    val regex = "^(https?://)?(www\\.)?([\\w-]+\\.)+[a-z]{2,}/?.*$" //expresion regular para verificar validez de URL

    Column(modifier = Modifier.padding(16.dp)) {
        FormularioRegistro(
            marca = marca,
            modelo = modelo,
            anho = anho,
            precio = precio,
            kilometraje = kilometraje,
            imagenURL = imagenURL,
            onMarcaChange = { if(it.length <= 40) { marca = it } },
            onModeloChange = { if(it.length <= 40) { modelo = it } },
            onAnhoChange = { if(it.length <= 4) { anho = it } },
            onPrecioChange = { if(it.length <= 8) { precio = it } },
            onKilometrajeChange = { if(it.length <= 8) { kilometraje = it } },
            onImagenURLChange = { imagenURL = it},
            onRegistrarAuto = {
                if(marca.isNotBlank()
                    && modelo.isNotBlank()
                    && precio.isDigitsOnly()
                    && anho.isDigitsOnly()
                    && kilometraje.isDigitsOnly()) {
                    if(anho.toInt() > Calendar.getInstance().get(Calendar.YEAR)) {
                        anho = Calendar.getInstance().get(Calendar.YEAR).toString()
                    } else if(anho.toInt() < 1900) {
                        anho = "1900"
                    }

                    if(precio.toInt() < 0) { precio = "0" }
                    else if(precio.toInt() > 99999999) { precio = "99999999" }

                    if(kilometraje.toInt() < 0) { kilometraje = "0" }
                    else if(kilometraje.toInt() > 99999999) { kilometraje = "99999999" }

                    if(regex.toRegex().matches(imagenURL)) { //chequea si es una url valida
                        val nuevoAuto = Auto(marca, modelo, anho, precio, kilometraje, imagenURL)
                        listaAutos.agregarAuto(nuevoAuto)

                        marca = ""
                        modelo = ""
                        anho = ""
                        precio = ""
                        kilometraje = ""
                        imagenURL = ""
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(4.dp))

        ListaAutos(listaAutos = listaAutos.listaAutos)
    }
}


