import java.util.Scanner;
import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    // Main
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        // Primera interaccion con el usuario
        System.out.println("Bienvenido al DBMS para KarelTheRobot!");
        System.out.println("");
        bienvenida(scan);
    }
    //-------------------------------------------------------------------------------------------------------------

    // Metodo de interaccion general
    public static void bienvenida(Scanner scan){
        Tabla tabla = new Tabla(); // Instancia de la clase Tabla
        ExecutorService executor = Executors.newFixedThreadPool(4); // Pool de threads para el paralelismo

        System.out.println("Presione 1 para crear una nueva Base de Datos");
        System.out.println("Presione 2 para ver las Bases de Datos existentes");
        System.out.println("Presione 3 para agregar una fila a una de las tablas");
        System.out.println("Presione 4 para ver el contenido de una de las tablas");
        System.out.println("Presione 5 para hacer consultas");
        System.out.print("Selecione: ");
        String decision = scan.nextLine();
        System.out.println("");

        if (decision.equals("1")) {     // Crear nueva BD con sus Tablas
            // Crear una BD como un folder
            System.out.print("Ingrese el nombre de su nueva Base de Datos: ");
            String nombre = scan.nextLine();
            File db = new File("Bases de Datos/" + nombre);
            if (db.mkdir()) {
                String ruta = db.getPath();     // Ruta a la carpeta de la DB correpondiente
                executor.execute(() -> tabla.generarTablaCSV("Robot", ruta));
                executor.execute(() -> tabla.generarTablaCSV("LogEventos", ruta));
                executor.execute(() -> tabla.generarTablaCSV("EstadoPrograma", ruta));
                executor.execute(() -> tabla.generarTablaCSV("VariablesEstaticas", ruta));
                executor.shutdown();    // Apagar el executor cuando todas las tareas hayan terminado
                System.out.println("La Base de Datos '" + nombre + "' fue creada exitosamente!");
            } else {
                System.out.println("La Base de Datos '" + nombre + "' no se creó porque ya existe o hubo un error.");
            }
            System.out.println("");

        } else if (decision.equals("2")) {      // Revisar las BD existentes en la carpeta
            File dbs = new File("Bases de Datos");
            String[] listado = dbs.list();
            if (listado == null || listado.length == 0) {
                System.out.println("No hay Bases de Datos creadas!");
            } else {
                System.out.println("Bases de Datos existentes:");
                for (int i=0; i< listado.length; i++) {
                    System.out.println(listado[i]);
                }
            }
            System.out.println("");

        } else if (decision.equals("3")) {  // Agregar fila (v1, pendiente de modificacion)
            tabla.agregarFilaCSV("Robot", "Bases de Datos/Prueba");
            System.out.println("");

        } else if (decision.equals("4")) {  // Leer contenido completo
            System.out.print("Ingrese el nombre de la Base de Datos deseada: ");
            String bd = scan.nextLine();
            System.out.print("Ingrese el nombre de la tabla a consultar: ");
            String archivo = scan.nextLine();
            String ruta = "Bases de Datos/" + bd + "/" + archivo + ".csv";

            tabla.leerTablaCSV(ruta);
            System.out.println("");
        
        } else if (decision.equals("5")) {  // Hacer consultas a las BD
            System.out.print("Ingrese la base de datos de la que quiere consultar: ");
            String bd = scan.nextLine();
            System.out.print("Ingrese la tabla de la base de datos '" + bd + "' de la que quiere consultar: ");
            String archivo = scan.nextLine();
            String ruta = "Bases de Datos/" + bd+ "/" + archivo + ".csv";

            System.out.print("Ingrese la columna a buscar: ");
            String nombreColumna = scan.nextLine();
            System.out.print("Ingrese el valor a buscar en la columna '" + nombreColumna + "': ");
            String valorBuscado = scan.nextLine();
            System.out.println("");

            tabla.consultar(ruta, nombreColumna, valorBuscado);
            System.out.println("");

        } else {    // No supo acatar instrucciones :v
            System.out.println("Comando incorrecto, ingrese un comando valido.");
            System.out.println("");
            bienvenida(scan);
        }
    }
    //-------------------------------------------------------------------------------------------------------------
}