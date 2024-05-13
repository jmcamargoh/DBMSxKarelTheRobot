import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Tabla {
    private ArrayList<String> columnas;
    private Map<String, Map<String, List<String[]>>> indices;     // Index para columnas especificas (clave=nombreColumna, valor=indiceColumna)
    private static final Lock lock = new ReentrantLock();

    // Constructor
    public Tabla() {
        this.columnas = new ArrayList<>();
        this.indices = new HashMap<>();
    }
    //-------------------------------------------------------------------------------------------------------------

    // Método para indexar una columna específica (se hace asi para poder aplicarlo a las consultas)
    public void indexarColumna(String nombreColumna, String ruta) {
        Map<String, List<String[]>> indiceColumna = new HashMap<>();    // Index de valores en la columna (clave=valoresenColumna, valor=fila completa en la que coincide ese valor)
        try (BufferedReader reader = new BufferedReader(new FileReader(ruta))) {
            String linea;
            // Leer la primera línea para obtener los nombres de las columnas
            String[] nombresColumnas = reader.readLine().split(",");
            int indiceColumnaActual = -1;
            for (int i = 0; i < nombresColumnas.length; i++) {
                if (nombresColumnas[i].equals(nombreColumna)) {
                    indiceColumnaActual = i;
                    break;
                }
            }
            if (indiceColumnaActual == -1) {
                System.out.println("La columna ingresada no existe en la tabla");
                return;
            }
            // Leer cada línea del archivo y construir el índice
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(",");
                String valor = partes[indiceColumnaActual];
                List<String[]> registros = indiceColumna.getOrDefault(valor, new ArrayList<>());
                registros.add(partes);
                indiceColumna.put(valor, registros);
            }
            indices.put(nombreColumna, indiceColumna);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    // Insertar una fila en la tabla (se debe modificar) (se podria eliminar la ruta y poner una ruta por default)
    public void agregarFilaCSV(String data, String nombreTabla){
        try {
            String ruta = "Bases de Datos/Prueba/" + nombreTabla + ".csv";
            FileWriter fileWriter = new FileWriter(ruta, true);
            BufferedWriter writer = new BufferedWriter(fileWriter);

            writer.write(data);
            writer.newLine(); // Agrega una nueva línea después de la fila

            // Cerrar el escritor
            writer.close();

            indexarColumna("id"+nombreTabla, ruta); // Para poder indexarlas cuando se inserta un dato, guiandose del id de cada una

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

            System.out.println();
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
    public void consultar(String ruta, String nombreColumna, String valorBuscado) {
        indexarColumna(nombreColumna, ruta);    // Mejorar la eficiencia con indexacion de hashmaps
        long startTime = System.nanoTime();     // Medir los tiempos de ejecucion
        if (!indices.containsKey(nombreColumna)) {      // Verificar la existencia de la columna
            System.out.println("No existe un índice para la columna especificada.");
            return;
        }
        Map<String, List<String[]>> indiceColumna = indices.get(nombreColumna);     // Index de valores en la columna (clave=valoresenColumna, valor=fila completa en la que coincide ese valor)
        if (!indiceColumna.containsKey(valorBuscado)) {     // Verificar la existencia del valor
            System.out.println("No se encontraron resultados para el valor buscado en el índice.");
            return;
        }
        List<String[]> resultados = indiceColumna.get(valorBuscado);    // Lista que almacena las filas completas para imprimirlas (despues de la busqueda)
        System.out.println("Resultados encontrados para la columna '" + nombreColumna + "' con valor '" + valorBuscado + "':");
        for (String[] fila : resultados) {
            for (String dato : fila) {
                System.out.print(dato + ",");
            }
            System.out.println();
        }
        // Medir los tiempos de ejecucion
        long endTime = System.nanoTime();
        double time = (double) ((endTime-startTime)/1e6);
        System.out.println();
        System.out.println("Tiempo de Consulta = " + time + " milisegundos");
    }
    //-------------------------------------------------------------------------------------------------------------

    // Eliminar fila (solo se hace por el ID de cada fila)
    public void eliminar(String nombreTabla, String id) {
        try {
            String ruta = "Bases de Datos/Prueba/" + nombreTabla + ".csv";
            long startTime = System.nanoTime();

            // Abrir el archivo para lectura
            BufferedReader reader = new BufferedReader(new FileReader(ruta));
            
            // Abrir un archivo temporal para escribir los datos actualizados
            File archivoTemporal = new File("Bases de Datos/Prueba/temporal.csv");
            FileWriter fileWriter = new FileWriter(archivoTemporal);
            BufferedWriter writer = new BufferedWriter(fileWriter);
            
            // Variables para llevar el registro de la línea actual
            String linea;
            int numeroLinea = 0;

            // Lee el archivo línea por línea
            while ((linea = reader.readLine()) != null) {
                // Incrementa el número de línea
                numeroLinea++;
                
                // Si la línea comienza con el ID especificado, que escriba vacio
                if (linea.startsWith(id)) {
                    linea = "";
                }
                
                // Escribir la linea si o si con lo que tenga
                writer.write(linea);
                writer.newLine();
            }

            writer.close();
            reader.close();

            // Borra el archivo original
            File archivoOriginal = new File(ruta);
            if (!archivoOriginal.delete()) {
                System.out.println("No se pudo eliminar el archivo original.");
                return;
            }
            
            // Renombra el archivo temporal al nombre del archivo original
            if (!archivoTemporal.renameTo(archivoOriginal)) {
                System.out.println("No se pudo renombrar el archivo temporal.");
            }

            // Medir los tiempos de ejecucion
            long endTime = System.nanoTime();
            double time = (double) ((endTime-startTime)/1e6);
            System.out.println();
            System.out.println("Tiempo de Ejecucion = " + time + " milisegundos");

            //indexarColumna("id"+nombreTabla, ruta);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //-------------------------------------------------------------------------------------------------------------
}