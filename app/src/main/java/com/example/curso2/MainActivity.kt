package com.example.curso2

import android.os.Bundle
import android.util.Log
import android.view.View.OnLongClickListener
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    private lateinit var db: TareasDatabase
    lateinit var tareasDao: TareasDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Principal(tareasDao)
        }

    }

    override fun onResume() {
        super.onResume()
        db = TareasDatabase.getDatabase(applicationContext)
        tareasDao = db.tareasDao()
    }
}

@Composable
fun Principal(tareasDAO: TareasDAO) {
    var descTarea by rememberSaveable { mutableStateOf("") }
    var guarda by remember { mutableStateOf(false) }
    var lista by remember { mutableStateOf(tareasDAO.getAll()) }
    var alerta by remember { mutableStateOf(false) }
    var idtarea by remember { mutableStateOf(0) }


    if (guarda) {
        Log.i("Guarda", descTarea)
        if (descTarea != "") {
            var t = Tareas(id = 0, descrip = descTarea, checked = false)
            tareasDAO.insert(t)
            descTarea = ""

        }
        guarda = false
        lista = tareasDAO.getAll()
    }

    if (alerta) {
        lista.find { it.id == idtarea }?.let {tarea ->
            Confirmar(tarea,onConfirm = {
                tareasDAO.delete(tarea)
                lista = tareasDAO.getAll()
                alerta = false
            }, onDissmiss = {
                alerta = false
            })
        }
    }

    //Interfaz
    Column {
        Row {
            CampoTexto(descTarea, onValueChange = { descTarea = it })
            BotonGuardado(onClick = { guarda = it })
        }
        ListadoTareas(lista = lista, onChecked = {
            Log.i("Ckecked", it.checked.toString())
            tareasDAO.update(it)
        },
            onLongClick = { tarea ->
                idtarea = tarea.id
                alerta = true

            })

    }
}

@Composable
fun CampoTexto(descTarea: String = "", onValueChange: (String) -> Unit) {
    TextField(value = descTarea, onValueChange = { onValueChange(it) }, label = {
        Text("Tarea")
    })
}

@Composable
fun BotonGuardado(onClick: (Boolean) -> Unit) {
    Button(onClick = { onClick(true) }) {
        Text("Guardar")

    }
}

@Composable
fun ListadoTareas(lista: List<Tareas>, onChecked: (Tareas) -> Unit, onLongClick: (Tareas) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(lista) {
            Linea(tarea = it,
                onChecked = { onChecked(it) },
                onLongClick = { onLongClick(it) })


        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Linea(tarea: Tareas, onChecked: (Tareas) -> Unit, onLongClick: (Tareas) -> Unit) {
    var checked by remember { mutableStateOf(tarea.checked) }
    Card(modifier = Modifier
        .padding(5.dp)
        .combinedClickable(enabled = true, onLongClick = {
            onLongClick(tarea)
        }, onClick = {}), elevation = CardDefaults.cardElevation(10.dp)
    ) {
        Row {
            Icon(Icons.Filled.List, contentDescription = "Tarea")
            Text(tarea.descrip, modifier = Modifier.weight(1f))
            Checkbox(checked = checked, onCheckedChange = {
                checked = it
                tarea.checked = checked
                onChecked(tarea)
            })
        }
    }

}

@Composable
fun Confirmar(tarea:Tareas,onConfirm: (Boolean) -> Unit, onDissmiss: (Boolean) -> Unit) {
    AlertDialog(onDismissRequest = { /*TODO*/ },
        title = {
            Text("Eliminar")
        },
        icon = { Icon(Icons.Filled.Delete, contentDescription = null) },
        text = {
            Text("Desea cancelar la tarea? -> ${tarea.descrip}")
        }, confirmButton = {
            Button(onClick = { onConfirm(true) }) {
                Text("OK")
            }

        },
        dismissButton = {
            Button(onClick = { onDissmiss(false) }) {
                Text("Cancela")
            }
        })

}


