package proyecto.pkg1;

import java.util.Scanner;
import java.util.Date;
import java.util.Vector;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;

public class Proyecto1 {

    private static Scanner scanner = new Scanner(System.in);
    public static boolean salir;
    private static Vector<Personaje> personajes = new Vector<>();
    private static Vector<String> bitacoraActual = new Vector<>();

    static class Personaje {
        int codUnico;
        String nombre;
        String categoria;
        int precio;
        int stock;

        public Personaje(int codUnico, String nombre, String categoria, int precio, int stock) {
            this.codUnico = codUnico;
            this.nombre = nombre;
            this.categoria = categoria;
            this.precio= precio;
            this.stock = stock;
        }
    }

     public static void registrarBitacora(String accion, boolean correcto) {
        Date fecha = new Date();
        String estado = correcto ? "Correcta" : "Errónea";
        String entrada = fecha + " | Acción: " + accion + " | Estado: " + estado + " | Usuario: Jhostin Ramírez";
        bitacoraActual.add(entrada);
    }

    public static void guardarBitacoraEnArchivo() {
        try (PrintWriter pw = new PrintWriter(new FileWriter("bitacora.txt", true))) {
            pw.println("\n===== Nueva Sesión =====");
            for (String entrada : bitacoraActual) {
                pw.println(entrada);
            }
            pw.println("===== Fin de Sesión =====\n");
        } catch (Exception e) {
            System.out.println("Error al guardar la bitácora en archivo.");
        }
    }
    public static void agregarproducto() {
        try {
            System.out.println("=====Agregar Producto====");
            int nuevocodUnico = personajes.size() + 1;
            System.out.println("Nombre del Producto: ");
            String producto = scanner.nextLine().trim();
            System.out.println("Categoria (camisas, pantalones, accesorios, etc): ");
            String categoria = scanner.nextLine();
            System.out.println("Ingrese el precio: ");
            int precio = Integer.parseInt(scanner.nextLine());
            while (precio < 0) {
                System.out.println("Error: EL precio debe de ser positivo. Intentar de nuevo");
                precio = Integer.parseInt(scanner.nextLine());
            }
            System.out.println("Ingrese la cantidad de stock: ");
            int stock = Integer.parseInt(scanner.nextLine());
            while (stock < 0) {
                System.out.println("Error: EL stock debe de ser positivo. Intentar de nuevo");
                stock = Integer.parseInt(scanner.nextLine());
            }
            Personaje nuevoPersonaje = new Personaje(nuevocodUnico, producto, categoria, precio,stock);
            personajes.add(nuevoPersonaje);

            System.out.println("\nProducto agregado exitosamente!");
            System.out.println("Codigo Unico: " + nuevocodUnico);
            System.out.println("Nombre: " + producto);

            registrarBitacora("Agregar Producto", true);
        } catch (Exception e) {
            System.out.println("Error al agregar producto.");
            registrarBitacora("Agregar Producto", false);
        }
    }

    public static void buscarproducto() {
        try {
            System.out.println("====Buscar Producto====");
            if (personajes.isEmpty()) {
                System.out.println("\nNo hay ningun producto registrado.");
                registrarBitacora("Buscar Producto", false);
                return;
            }

            System.out.println("Seleccione el tipo de búsqueda:");
            System.out.println("1. Buscar por Código");
            System.out.println("2. Buscar por Nombre");
            System.out.println("3. Buscar por Categoría");
            System.out.print("Elige una opción: ");
            int opcion = Integer.parseInt(scanner.nextLine());

            boolean encontrado = false;

            switch (opcion) {
                case 1:
                    int codUnico;
                    while (true) {
                        try {
                            System.out.print("\nIngrese el codigo del producto a buscar: ");
                            codUnico = Integer.parseInt(scanner.nextLine());

                            if (codUnico < 1 || codUnico > personajes.size()) {
                                System.out.println("Error: Codigo no válido. Intente nuevamente.");
                            } else {
                                break;
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Error: Debe ingresar un número válido.");
                        }
                    }

                    Personaje personaje = personajes.get(codUnico - 1);
                    mostrarDatosProducto(personaje);
                    encontrado = true;
                    break;

                case 2:
                    System.out.print("\nIngrese el nombre del producto: ");
                    String nombreBuscar = scanner.nextLine().trim().toLowerCase();
                    for (Personaje p : personajes) {
                        if (p.nombre.toLowerCase().contains(nombreBuscar)) {
                            mostrarDatosProducto(p);
                            encontrado = true;
                        }
                    }
                    break;

                case 3:
                    System.out.print("\nIngrese la categoría del producto: ");
                    String categoriaBuscar = scanner.nextLine().trim().toLowerCase();
                    for (Personaje p : personajes) {
                        if (p.categoria.toLowerCase().contains(categoriaBuscar)) {
                            mostrarDatosProducto(p);
                            encontrado = true;
                        }
                    }
                    break;

                default:
                    System.out.println("Opción inválida.");
                    registrarBitacora("Buscar Producto", false);
                    return;
            }

            if (!encontrado) {
                System.out.println("\nNo se encontró ningún producto con ese criterio.");
                registrarBitacora("Buscar Producto", false);
            } else {
                registrarBitacora("Buscar Producto", true);
            }
        } catch (Exception e) {
            System.out.println("Error en la búsqueda.");
            registrarBitacora("Buscar Producto", false);
        }
    }

    private static void mostrarDatosProducto(Personaje personaje) {
        System.out.println("\nDatos del producto:");
        System.out.println("1. Código: " + personaje.codUnico);
        System.out.println("2. Nombre: " + personaje.nombre);
        System.out.println("3. Categoría: " + personaje.categoria);
        System.out.println("4. Precio: " + personaje.precio);
        System.out.println("5. Stock: " + personaje.stock);
    }

    private static void eliminarProdcuto() {
        try {
            if (personajes.isEmpty()) {
                System.out.println("\nNo hay ningun producto registrado para eliminar.");
                registrarBitacora("Eliminar Producto", false);
                return;
            }

            System.out.println("\n--- Eliminar Producto ---");
            verListadoPersonajes();

            int codUnico;
            while (true) {
                try {
                    System.out.print("\nIngrese el codigo del Producto a eliminar: ");
                    codUnico = Integer.parseInt(scanner.nextLine());

                    if (codUnico < 1 || codUnico > personajes.size()) {
                        System.out.println("Error: Codigo no válido. Intente nuevamente.");
                    } else {
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Error: Debe ingresar un número válido.");
                }
            }

            System.out.print("¿Está seguro que desea eliminar este Producto " + personajes.get(codUnico-1).nombre + "? (S/N): ");
            String confirmacion = scanner.nextLine().trim().toUpperCase();

            if (confirmacion.equals("S")) {
                String nombreEliminado = personajes.get(codUnico-1).nombre;
                personajes.remove(codUnico-1);

                for (int i = 0; i < personajes.size(); i++) {
                    personajes.get(i).codUnico = i + 1;
                }

                System.out.println("El producto " + nombreEliminado + " fue eliminado correctamente.");
                registrarBitacora("Eliminar Producto", true);
            } else {
                System.out.println("Eliminación cancelada.");
                registrarBitacora("Eliminar Producto", false);
            }
        } catch (Exception e) {
            System.out.println("Error al eliminar producto.");
            registrarBitacora("Eliminar Producto", false);
        }
    }

    private static void verListadoPersonajes() {
        if (personajes.isEmpty()) {
            System.out.println("\nNo hay ningun producto registrado en el sistema.");
            return;
        }

        System.out.println("\n--- Listado de Productos ---");
        System.out.println("Codigo\tNombre\tPrecio\tStock");
        System.out.println("--------------------------------");
        for (Personaje p : personajes) {
            System.out.println(p.codUnico + "\t" + p.nombre + "\t" + p.precio+"\t"+p.stock);
        }
    }

    public static void registrarVenta() {
        try {
            if (personajes.isEmpty()) {
                System.out.println("\nNo hay productos registrados para vender.");
                registrarBitacora("Registrar Venta", false);
                return;
            }

            verListadoPersonajes();

            int codProducto;
            while (true) {
                try {
                    System.out.print("\nIngrese el código del producto a vender: ");
                    codProducto = Integer.parseInt(scanner.nextLine());

                    if (codProducto < 1 || codProducto > personajes.size()) {
                        System.out.println("Error: Código no válido. Intente nuevamente.");
                    } else {
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Error: Debe ingresar un número válido.");
                }
            }

            Personaje producto = personajes.get(codProducto - 1);

            System.out.print("Ingrese la cantidad vendida: ");
            int cantidadVendida = Integer.parseInt(scanner.nextLine());

            if (cantidadVendida <= 0) {
                System.out.println("Error: La cantidad debe ser mayor a 0.");
                registrarBitacora("Registrar Venta", false);
                return;
            }

            if (cantidadVendida > producto.stock) {
                System.out.println("Error: Stock insuficiente. Stock actual: " + producto.stock);
                registrarBitacora("Registrar Venta", false);
                return;
            }

            // Restar stock
            producto.stock -= cantidadVendida;

            // Calcular total
            int totalVenta = cantidadVendida * producto.precio;

            // Fecha y hora
            String fechaHora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

            // Registrar en archivo de texto
            try (FileWriter writer = new FileWriter("ventas.txt", true)) {
                writer.write("Fecha: " + fechaHora +
                             " | Producto: " + producto.nombre +
                             " | Código: " + producto.codUnico +
                             " | Cantidad: " + cantidadVendida +
                             " | Total: Q" + totalVenta + "\n");
            }

            System.out.println("\nVenta registrada exitosamente!");
            System.out.println("Producto: " + producto.nombre);
            System.out.println("Cantidad vendida: " + cantidadVendida);
            System.out.println("Total: Q" + totalVenta);
            System.out.println("Fecha y hora: " + fechaHora);

            registrarBitacora("Registrar Venta", true);

        } catch (Exception e) {
            System.out.println("Error al registrar venta.");
            registrarBitacora("Registrar Venta", false);
        }
    }
    public static void verBitacoras() {
        System.out.println("\n===== BITÁCORA DE ESTA EJECUCIÓN =====");
        if (bitacoraActual.isEmpty()) {
            System.out.println("No hay acciones registradas en esta sesión.");
        } else {
            for (String entrada : bitacoraActual) {
                System.out.println(entrada);
            }
        }
    }

    public static void datosestudiante() {
        System.out.println("=====Datos del Estudiante=====");
        System.out.println("Nombre: Jhostin Javier Ramírez Enríquez");
        System.out.println("Carnet: 202404407");
        System.out.println("Nombre del Laboratorio: Laboratorio Introducción a la Programación y Computación");
        System.out.println("Sección: A");
        System.out.println("Usuario de GitHub: jhostinramir2006-beep ");
        registrarBitacora("Ver Datos del Estudiante", true); // ← Se registra en bitácora

    }

    public static void leermenuprincipal() {
        try {
            int opcion = Integer.parseInt(scanner.nextLine());
            switch (opcion) {
                case 1:
                    agregarproducto();
                    break;
                case 2:
                    buscarproducto();
                    break;
                case 3:
                    eliminarProdcuto();
                    break;
                case 4:
                    registrarVenta();
                    break;
                case 6:
                    verBitacoras();
                break;
                case 7:
                    datosestudiante();
                    break;
                case 8:
                    salir = true;
                    registrarBitacora("Salir del Programa", true);
                    guardarBitacoraEnArchivo(); // Guardar bitácora al salir
                    break;
                default:
                    System.out.println("Opción no válida.");
                    registrarBitacora("Opción inválida de menú", false);
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: El valor ingresado es invalido");
            registrarBitacora("Entrada de menú inválida", false);
        }
    }

    public static void menuprincipal() {
        salir = false;
        while (!salir) {
            System.out.println("\n=====Menú de Gestion de Productos=====");
            System.out.println("1. Agregar Producto");
            System.out.println("2. Buscar Producto");
            System.out.println("3. Eliminar Producto");
            System.out.println("4. Registrar Venta");
            System.out.println("5. Generar Reportes");
            System.out.println("6. Ver Bitácoras");
            System.out.println("7. Ver Datos del Estudiante");
            System.out.println("8. salir");
            System.out.println("Elige una opcion:");
            leermenuprincipal();
        }
    }

    public static void main(String[] args) {
        menuprincipal();
    }
}
