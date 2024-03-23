import java.util.ArrayList;
import java.util.List;

public class Controlador_Trenes {
    // clase creada solo para cambiar la condición de parada de todos los trenes
    private List<Tren> listaTrenes;

    // Constructor de ControladorTrenes que inicializa la lista de trenes
    public Controlador_Trenes() {
        this.listaTrenes = new ArrayList<>();
    }

    // Método para agregar un tren a la lista de trenes
    public void agregarTren(Tren tren) {
        listaTrenes.add(tren);
    }

    // Método para establecer la variable salida como false en todos los trenes
    public void darSalidaTrenes() {
        for (Tren tren : listaTrenes) {
            tren.setSalida(false);
        }
    }
}