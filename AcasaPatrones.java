import java.util.ArrayList;
import java.util.List;

/* ============================================================
   PATRON ESTRUCTURAL: DECORATOR (parte 1: la interfaz comun)
   Modulo es la interfaz que comparten el modulo normal y el modulo
   "decorado". Asi el resto del programa no distingue entre ambos.
   ============================================================ */
interface Modulo {
    String getTitulo();
    boolean admiteGrado(int grado);
    double precio();
}

// Modulo normal de ACASA: titulo, rango de grados y precio.
class ModuloBase implements Modulo {
    private String titulo;
    private int gradoMin;
    private int gradoMax;
    private double precio;

    public ModuloBase(String titulo, int gradoMin, int gradoMax, double precio) {
        this.titulo = titulo;
        this.gradoMin = gradoMin;
        this.gradoMax = gradoMax;
        this.precio = precio;
    }

    public String getTitulo() { return titulo; }
    public boolean admiteGrado(int grado) { return grado >= gradoMin && grado <= gradoMax; }
    public double precio() { return precio; }
}

/* ============================================================
   PATRON ESTRUCTURAL: DECORATOR (parte 2: el decorador)
   Por que se usa: permite agregar "Certificado" a cualquier modulo
   (basico, intermedio, avanzado) SIN crear una clase nueva por cada
   combinacion y SIN modificar ModuloBase. Envuelve al modulo original,
   delega en el y le suma titulo y precio extra.
   ============================================================ */
class ModuloConCertificado implements Modulo {
    private Modulo modulo; // el modulo que se esta envolviendo

    public ModuloConCertificado(Modulo modulo) {
        this.modulo = modulo;
    }

    public String getTitulo() { return modulo.getTitulo() + " + Certificado"; }
    public boolean admiteGrado(int grado) { return modulo.admiteGrado(grado); }
    public double precio() { return modulo.precio() + 50; }
}

/* ============================================================
   PATRON CREACIONAL: FACTORY METHOD
   Por que se usa: centraliza la creacion de modulos. Quien necesita un
   modulo solo dice el nivel ("BASICO", "INTERMEDIO", "AVANZADO") y la
   fabrica decide que objeto crear y con que datos (grados, precio).
   Si cambia un precio o aparece un nivel nuevo, se cambia solo aqui.
   ============================================================ */
class ModuloFactory {
    public static Modulo crear(String nivel) {
        switch (nivel) {
            case "BASICO":
                return new ModuloBase("Exploradores del Espacio", 1, 6, 100);
            case "INTERMEDIO":
                return new ModuloBase("Sistemas de Propulsion y Cohetes", 7, 10, 150);
            case "AVANZADO":
                return new ModuloBase("Ingenieria de Sistemas Aeroespaciales", 11, 12, 200);
            default:
                throw new IllegalArgumentException("Nivel desconocido: " + nivel);
        }
    }
}

/* ============================================================
   PATRON DE COMPORTAMIENTO: OBSERVER
   Por que se usa: cuando alguien se inscribe, varias areas deben
   reaccionar cada una a su manera (Coordinacion registra, Tesoreria
   cobra). Inscripciones (el "sujeto") no conoce a esas areas: solo
   mantiene una lista de Observador y los avisa a todos. Se pueden sumar
   observadores nuevos (ej: Biblioteca) sin tocar la clase Inscripciones.
   ============================================================ */
interface Observador {
    void actualizar(Estudiante est, Modulo modulo, boolean aceptado);
}

class Estudiante {
    private String nombre;
    private int grado;

    public Estudiante(String nombre, int grado) {
        this.nombre = nombre;
        this.grado = grado;
    }

    public String getNombre() { return nombre; }
    public int getGrado() { return grado; }
}

class Coordinacion implements Observador {
    public void actualizar(Estudiante est, Modulo modulo, boolean aceptado) {
        if (aceptado) {
            System.out.println("  [Coordinacion] Registrado: " + est.getNombre()
                    + " en '" + modulo.getTitulo() + "'");
        } else {
            System.out.println("  [Coordinacion] Rechazado: " + est.getNombre()
                    + " no cumple el grado de '" + modulo.getTitulo() + "'");
        }
    }
}

class Tesoreria implements Observador {
    public void actualizar(Estudiante est, Modulo modulo, boolean aceptado) {
        if (aceptado) { // solo cobra cuando la inscripcion fue aceptada
            System.out.println("  [Tesoreria] Cobrar Bs " + modulo.precio()
                    + " a " + est.getNombre());
        }
    }
}

class Inscripciones {
    private List<Observador> observadores = new ArrayList<>();

    public void agregarObservador(Observador o) {
        observadores.add(o);
    }

    public void inscribir(Estudiante est, Modulo modulo) {
        System.out.println("Inscribiendo a " + est.getNombre() + "...");
        boolean aceptado = modulo.admiteGrado(est.getGrado());
        for (Observador o : observadores) { // avisa a todos los observadores
            o.actualizar(est, modulo, aceptado);
        }
    }
}

// Clase principal para ejecutar el programa
public class AcasaPatrones {
    public static void main(String[] args) {
        Inscripciones inscripciones = new Inscripciones();
        Estudiante ana = new Estudiante("Ana (2do primaria)", 2);
        Estudiante luis = new Estudiante("Luis (5to secundaria)", 11);

        // Observer: se suscriben las areas que deben reaccionar
        inscripciones.agregarObservador(new Coordinacion());
        inscripciones.agregarObservador(new Tesoreria());

        // Factory Method: pedimos modulos por nivel, sin usar "new" aqui
        Modulo basico = ModuloFactory.crear("BASICO");
        Modulo avanzado = ModuloFactory.crear("AVANZADO");

        // Decorator: Luis quiere el modulo avanzado con certificado
        Modulo avanzadoCert = new ModuloConCertificado(avanzado);

        inscripciones.inscribir(ana, basico);
        inscripciones.inscribir(luis, avanzadoCert);
        inscripciones.inscribir(ana, avanzado); // Ana no puede
    }
}
