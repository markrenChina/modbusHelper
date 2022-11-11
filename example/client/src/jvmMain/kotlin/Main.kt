// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.zhipuchina.client.ModbusTcpClient
import com.zhipuchina.model.Buffer
import com.zhipuchina.model.MemoryTypes
import com.zhipuchina.utils.ConvertTo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import viewPager.ViewPager
import java.net.InetSocketAddress

@Composable
@Preview
fun App() {
    var address by remember { mutableStateOf("127.0.0.1") }
    var port by remember { mutableStateOf("502") }
    var slaveId by remember { mutableStateOf("1") }
    var count  by remember { mutableStateOf("10") }
    var currentTabPage : MemoryPage by remember { mutableStateOf(MemoryPage.HoldingRegister) }
    Buffer.malloc(currentTabPage.memoryType,0,count.toInt())

    val flow : StateFlow<List<Int>> = MutableStateFlow(Buffer.getValueAsInt(currentTabPage.memoryType,0,count.toInt()))
    val list : List<Int> by flow.collectAsState()

    var client: ModbusTcpClient? by remember { mutableStateOf(null) }
    MaterialTheme {
        Column(modifier = Modifier.padding(10.dp)) {
            Box {
                Column() {
                    TextField(label = {
                        Text(text = "server address", style = MaterialTheme.typography.h6) },
                        value = address, onValueChange = { address = it },
                        enabled = client == null
                    )
                    Spacer(Modifier.height(5.dp))
                    TextField(label = {
                        Text(text = "server port", style = MaterialTheme.typography.h6) },
                        value = port, onValueChange = { port = it },
                        enabled = client == null)
                    Spacer(Modifier.height(5.dp))
                    TextField(label = {
                        Text(text = "slaveId", style = MaterialTheme.typography.h6) },
                        value = slaveId, onValueChange = { slaveId = it },
                        enabled = client == null)
                    Spacer(Modifier.height(5.dp))
                    SpaceAroundRow(Modifier) {
                        Button(onClick = {
                            client = ModbusTcpClient(InetSocketAddress(address,port.toInt()),slaveId.toInt(),null,null)
                        }, enabled = client==null){
                            Text("connect", style = MaterialTheme.typography.body1)
                        }
                        Spacer(Modifier.width(5.dp))
                        Button(onClick = {
                            client?.close()
                            client = null
                        }, enabled = client!=null){
                            Text("disConnect", style = MaterialTheme.typography.body1)
                        }
                    }
                    TextField(label = {
                        Text(text = "count", style = MaterialTheme.typography.h6) },
                        value = count, onValueChange = {
                            Buffer.malloc(currentTabPage.memoryType,0,count.toInt())
                            client?.readVAsync(currentTabPage.memoryType,0,count.toInt()){
                                when (currentTabPage.memoryType){
                                    MemoryTypes.InputCoil,MemoryTypes.OutputCoil ->{
                                        //Buffer.setValue(currentTabPage.memoryType,0,count.toInt())
                                    }
                                    MemoryTypes.InputRegister,MemoryTypes.HoldingRegister -> {
                                        //Buffer.setValue(currentTabPage.memoryType,0,count.toInt())
                                    }
                                }
                            }
                            count = it
                        })
                    Spacer(Modifier.height(5.dp))

                }
            }
            HorizontalDivider(
                thickness = 5.dp,
                startIndent = 10.dp,
                endIndent = 10.dp
            )
            Box(modifier = Modifier.padding(top = 5.dp)) {
                Column {
                    ViewPager(currentTabPage = currentTabPage, tabPagesList = MemoryPage.values().asList()){ cuurent ->
                        cuurent as MemoryPage
                        Buffer.malloc(cuurent.memoryType,0,count.toInt())
                        currentTabPage = cuurent
                    }
                    LazyScrollable(
                        modifier = Modifier, list
                    ) { index: Int, item: Int ->
                        SpaceAroundRow(Modifier){
                            Text(index.toString())
                            TextButton(onClick =  {

                            }){
                                Text(item.toString())
                            }
                        }
                    }
                }

            }
        }
    }
}

fun main() = application {
    Window(
        title = "ModbusTcp-Client",
        onCloseRequest = ::exitApplication) {
        App()
    }

}
