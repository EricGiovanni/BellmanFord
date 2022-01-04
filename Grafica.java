import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Clase que modela una grafica
 */
public class Grafica {

    /**
     * Clase interna que modela un vertice
     */
    public class Vertice {
        // Atributos
        public String nombre;
        private int numeroVertice;

        /**
         * Constructor de un vertice al que se le da el nombre
         * 
         * @param nombre
         */
        public Vertice(String nombre) {
            this.nombre = nombre;
            this.numeroVertice = -1;
        }

        /**
         * Metodo que asigna el numero de vertice que es en la lista de la grafica
         * 
         * @param numeroVertice
         */
        public void setNumeroVertice(int numeroVertice) {
            this.numeroVertice = numeroVertice;
        }

        /**
         * Metodo que devuelve el numero de vertice que es en la lista de la grafica
         * 
         * @return
         */
        public int getNumeroVertice() {
            return this.numeroVertice;
        }

        /**
         * Metodo que obtiene el nombre del vertice
         * 
         * @return
         */
        public String getNombre() {
            return this.nombre;
        }
    }

    /**
     * Clase interna que modela una arista
     */
    public class Arista {
        // Atributos
        public Vertice origen;
        public Vertice destino;
        public int peso;

        /**
         * Constructor de la clase Arista
         * 
         * @param origen
         * @param destino
         * @param peso
         */
        public Arista(Vertice origen, Vertice destino, int peso) {
            this.origen = origen;
            this.destino = destino;
            this.peso = peso;
        }
    }

    // Atributos
    private int V;
    private int A;
    private List<Arista> aristas;
    private List<Vertice> vertices;

    /**
     * Constructor que crea una grafica con V vertices
     * 
     * @param V
     */
    public Grafica() {
        this.V = 0;
        this.A = 0;
        vertices = new ArrayList<Vertice>();
        aristas = new ArrayList<Arista>();
    }

    /**
     * Metodo para obtener el numero de vertices V
     * 
     * @return
     */
    public int getV() {
        return V;
    }

    /**
     * Metodo para obtener el numero de aristas A
     * 
     * @return
     */
    public int getA() {
        return A;
    }

    /**
     * Metodo para obtener el vertice en la i-esima posicion
     * 
     * @param i
     */
    public Vertice getVertice(int i) {
        return this.vertices.get(i);
    }

    /**
     * Metodo para obtener el vertice por su nombre
     * 
     * @param nombre
     */
    public Vertice getVertice(String nombre) throws Exception {
        for (Vertice v : this.vertices) {
            if (v.getNombre().equals(nombre)) {
                return v;
            }
        }
        throw new Exception("No existe el nodo con ese nombre");
    }

    /**
     * Metodo para agregar vértice
     * 
     * @param V
     */
    public void agregaVertice(String nombre) {
        Vertice v = new Vertice(nombre);
        v.setNumeroVertice(this.vertices.size());
        this.vertices.add(v);
        this.V++;
    }

    /**
     * Agrega arista
     * 
     * @param origen
     * @param destino
     */
    public void agregaArista(Vertice origen, Vertice destino, int peso) {
        this.aristas.add(new Arista(origen, destino, peso));
        this.A++;
    }

    /**
     * Algoritmo de Bellman-Ford para encontrar el camino mas corto desde una
     * arista de origen a una arista de destino
     * 
     * @param origen
     */
    public void bellmanFord(int origen) {
        int[] dist = new int[this.V];
        int[] ant = new int[this.V];

        // Paso 1: Inicializar todas las distancias a infinito
        for (int i = 0; i < this.V; i++) {
            dist[i] = Integer.MAX_VALUE;
        }
        // Paso 2: La distancia desde el origen a si mismo es 0
        dist[origen] = 0;

        // Paso 3: Recorrer todas las aristas para actualizar las distancias
        // desde el origen a los demas vertices de la grafica (V-1 veces)
        for (int i = 1; i < this.V; i++) {
            for (int j = 0; j < this.A; j++) {
                int u = this.aristas.get(j).origen.getNumeroVertice();
                int v = this.aristas.get(j).destino.getNumeroVertice();
                int weight = this.aristas.get(j).peso;
                if (dist[u] != Integer.MAX_VALUE && dist[u] + weight < dist[v]) {
                    dist[v] = dist[u] + weight;
                    ant[v] = u;
                }
            }
        }

        // Paso 4: Revisar si hay ciclos negativos en la grafica.
        for (int i = 0; i < this.A; i++) {
            int u = this.aristas.get(i).origen.getNumeroVertice();
            int v = this.aristas.get(i).destino.getNumeroVertice();
            int weight = this.aristas.get(i).peso;
            if (dist[u] != Integer.MAX_VALUE && dist[u] + weight < dist[v]) {
                System.out.println("La grafica contiene un ciclo de peso negativo");
            }
        }

        // Paso 5: Imprimir las distancias desde el origen a cada vertice
        printArr(dist, ant, this.V);
    }

    /**
     * Metodo para imprimir la solucion
     * 
     * @param dist
     * @param v2
     */
    private void printArr(int[] dist, int[] ant, int v2) {
        System.out.println("Distancia del vertice v desde el origen u:");
        for (int i = 0; i < v2; i++) {
            System.out.println(
                    this.getVertice(i).getNombre() + " (" + this.getVertice(ant[i]).getNombre() + ", " + dist[i] + ")");
        }
    }

    /**
     * Metodo para crear grafica a partir de un archivo de texto
     * 
     * @param fileName
     */
    public void crearGrafica(String fileName) {
        try {
            File grafica = new File(fileName);
            Scanner sc = new Scanner(grafica);
            boolean flag = false;
            while (sc.hasNextLine()) {
                String[] linea = sc.nextLine().split(" ");
                if (linea.length == 1 && !flag) {
                    this.agregaVertice(linea[0].trim());
                } else if (linea.length == 2) {
                    if (!flag) {
                        flag = true;
                    }
                    String origen = linea[0].substring(0, 1).trim();
                    String destino = linea[0].substring(1, 2).trim();
                    int peso = -1;
                    try {
                        peso = Integer.parseInt(linea[1]);
                    } catch (Exception e) {
                        System.out.println("El formato del archivo es incorrecto");
                        System.exit(0);
                    }
                    this.agregaArista(this.getVertice(origen), this.getVertice(destino), peso);
                } else {
                    System.out.println("El formato del archivo es incorrecto");
                    System.exit(0);
                }
            }
            sc.close();
        } catch (Exception e) {
            System.out.println("Ocurrio un error al leer el archivo");
            System.exit(0);
        }

    }

    public static void main(String[] args) {
        String fileName = "";
        Grafica grafica = new Grafica();
        if (args.length == 1) {
            fileName = args[0];
        } else {
            Scanner sc = new Scanner(System.in);
            System.out.print("Ingrese el nombre del archivo: ");
            fileName = sc.nextLine();
            sc.close();
        }
        try {
            System.out.println("Creando grafica desde el archivo " + fileName + "...");
            grafica.crearGrafica(fileName);
        } catch (Exception e) {
            System.out.println("Error al leer el archivo\nSaliendo...");
            System.exit(0);
        }
        System.out.print("¿Cual deseas que sea el origen? (Debes escribir el nombre del vertice): ");
        String origen = "";
        while (origen == "") {
            try {
                Scanner si = new Scanner(System.in);
                origen = si.nextLine();
                grafica.bellmanFord(grafica.getVertice(origen).getNumeroVertice());
                si.close();
            } catch (Exception e) {
                System.out.println("Debes ingresar un vertice que pertenezca a la grafica ");
                origen = "";
            }
        }
    }
}
