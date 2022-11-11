import androidx.compose.ui.graphics.Color
import com.zhipuchina.model.MemoryTypes
import viewPager.ITabPage

enum class MemoryPage(
    override val title: String,
    override val icon: String,
    override val backColor: Color,
    override val serial: Int,
    val memoryType: MemoryTypes
) : ITabPage{
    OutputCoil("输出线圈","", Color(0,0,255,30),0,MemoryTypes.OutputCoil),
    InputCoil("输入线圈","", Color(0,0,255,30),1,MemoryTypes.InputCoil),
    HoldingRegister("保持寄存器","", Color(0,0,255,30),2,MemoryTypes.HoldingRegister),
    InputRegister("输入寄存器","", Color(0,0,255,30),3,MemoryTypes.InputRegister);

    override fun toString(): String {
        return "MemoryPage(title='$title')"
    }

}