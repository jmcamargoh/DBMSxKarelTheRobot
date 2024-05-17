# DBMS X Karel The Robot
En este proyecto vamos a construir un manejador de Bases de Datos (DBMS) simple desde cero. Un DBMS es un sistema de software que permite a los usuarios almacenar, recuperar y administrar datos en una forma estructurada. El DBMS debería soportar accesos concurrentes a múltiples usuarios y asegurar que haya integridad y consistencia en los datos a través de varios mecanismos de concurrencia. A la par de esto, lo que se busca con este proyecto es conectar dicho DBMS con la funcionalidad del programa integrado en "Karel The Robot". A través del log que van dejando los robots en la ejecución de sus tareas, el DBMS será capaz de almacenar dichos datos en archivos CSV y también permitirle al usuario hacer consultas, creaciones y eliminaciones sobre los mismos datos.

## Consideraciones para Ejecutar
1. Tener el JDK (Java) en versión 21.0.2.
2. Descargar en su totalidad todo el repositorio (rama main).
3. El DBMS se encuentra en la carpeta "Trabajos", y se compone de Main.java, Tabla.java y el folder completo "Bases de Datos". **IMPORTANTE:** No borrar el folder "Prueba" ni sus archivos CSV, dado que las rutas del DBMS por defecto apuntan a este folder.
4. Al momento de ejecutar "Main.java" revisar que desde donde se ejecuta es en el folder "Trabajos". De no ser así, el programa DBMS para consultas, creaciones y eliminaciones podría no funcionar correctamente.
5. Para insertar datos, ejecutar "Run.bat", esto ejecutará el programa en "Karel The Robot" y paralelamente empezará a almacenar los datos en los archivos CSV.
6. Para las consultas hay que seguir el formato definido. Las variables a buscar tienen que ir entre <>. **Ejemplo:** SELECT * FROM <Robot> WHERE <tipoRobot> == <2>.