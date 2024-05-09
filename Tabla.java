import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Tabla {
    private ArrayList<String> columnas;
    private ArrayList<String> filas;    // Hay que ver como adaptar esto al codigo, aun es manual la insercion
    private static final Lock lock = new ReentrantLock();

    // Constructor
    public Tabla() {
        this.columnas = new ArrayList<>();
        this.filas = new ArrayList<>();
    }
    //-------------------------------------------------------------------------------------------------------------

    // Creacion de la tabla en CSV
    public void generarTablaCSV(String nombre, String ruta){
        try {
            FileWriter writer = new FileWriter(ruta + "/" + nombre + ".csv");

            lock.lock();
            try {
                // Las columnas de cada una de las tablas (se pueden agregar mas columnas dependiendo de los datos)
                if (nombre.equals("Robot")) {
                    columnas.add("idRobot");
                    columnas.add("tipoRobot");
                    columnas.add("encendido");
                    columnas.add("color");
                    columnas.add("direccion");

                } else if (nombre.equals("LogEventos")) {
                    columnas.add("idLogEventos");
                    columnas.add("idRobot");
                    columnas.add("timeStamp");
                    columnas.add("avenida");
                    columnas.add("calle");
                    columnas.add("sirenas");

                } else if (nombre.equals("EstadoPrograma")) {
                    columnas.add("idEstadoPrograma");
                    columnas.add("timeStamp");
                    columnas.add("estado");

                } else if (nombre.equals("VariablesEstaticas")) {
                    columnas.add("idVariablesEstaticas");
                    columnas.add("timeStamp");
                }

                // Escribir los nombres de las columnas como encabezados
                for (int i=0; i < columnas.size(); i++) {
                    writer.write(columnas.get(i));
                    if (i < columnas.size()) {
                        writer.write(",");
                    }
                }
                writer.write("\n");
                columnas.clear();
            } finally {
                lock.unlock();
            }

            writer.close();
            System.out.println("Archivo CSV para la tabla '" + nombre + "' ha sido generado");
        } catch (IOException e) {
            System.out.println("Error al generar el archivo CSV: " + e.getMessage());
        }
    }
    //-------------------------------------------------------------------------------------------------------------

    // Insertar una fila en la tabla (se debe modificar)
    public void agregarFilaCSV(String nombre, String ruta){
        try {
            FileWriter fileWriter = new FileWriter(ruta + "/" + nombre + ".csv", true);
            BufferedWriter writer = new BufferedWriter(fileWriter);

            // Agregar la nueva fila al final del archivo *(ejemplo para tabla Robot)
            String nuevaFila = "1,2,true,"; // Reemplaza con los valores que se desean agregar
            writer.write(nuevaFila);
            writer.newLine(); // Agrega una nueva línea después de la fila

            // Cerrar el escritor
            writer.close();

            System.out.println("Se agregó una nueva fila al archivo CSV.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //-------------------------------------------------------------------------------------------------------------

    // Traer toda la informacion de la tabla
    public void leerTablaCSV(String ruta){
        try {
            BufferedReader reader = new BufferedReader(new FileReader(ruta));
            String linea;

            System.out.println("");
            System.out.println("Resultado: ");

            // Leer cada línea del archivo y mostrarla en la consola
            while ((linea = reader.readLine()) != null) {
                System.out.println(linea);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //-------------------------------------------------------------------------------------------------------------

    // Consultas por valor de columna
    public void consultar(String ruta, String nombreColumna, String valorBuscado){
        List<String[]> resultados = new ArrayList<>();
        List<String> columnas = null;
        int indiceColumna = -1;
        
        try{
            BufferedReader reader = new BufferedReader(new FileReader(ruta));
            String linea;

            // Leer la primera fila para obtener los nombres de las columnas
            if ((linea = reader.readLine()) != null) {
                columnas = Arrays.asList(linea.split(","));
                indiceColumna = columnas.indexOf(nombreColumna);
            }

            // Si el nombre de la columna existe en esa tabla
            if (indiceColumna != -1){
                // Leer cada fila del archivo
                while ((linea = reader.readLine()) != null) {
                    // Dividir la fila en sus partes usando la coma como delimitador
                    String[] partes = linea.split(",");

                    // Si la fila corresponde con el valor buscado
                    if (partes.length > indiceColumna && partes[indiceColumna].equals(valorBuscado)) {
                        resultados.add(partes);
                    }
                }
            } else {
                System.out.println("La columna ingresada no existe en la tabla");
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // Imprimir resultados
        if (!resultados.isEmpty()) {
            System.out.println("Resultados Encontrados para la columna '" + nombreColumna + "' con valor de '" + valorBuscado + "':");
            for (String[] fila : resultados) {
                for (String dato : fila) {
                    System.out.print(dato + ",");
                }
                System.out.println();
            }
        } else {
            System.out.println("No se encontraron resultados");
        }
    }
    //-------------------------------------------------------------------------------------------------------------
}