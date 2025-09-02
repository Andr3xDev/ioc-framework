package edu.escuelaing.tdse.framework.config;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Logger;

import edu.escuelaing.tdse.framework.annotations.GetMapping;
import edu.escuelaing.tdse.framework.annotations.PostMapping;
import edu.escuelaing.tdse.framework.annotations.RestController;

public class FrameworkSettings {

    private static final Logger logger = Logger.getLogger(FrameworkSettings.class.getName());

    public static HashMap<String, Method> servicesGet = new HashMap<>();
    public static HashMap<String, Method> servicesPost = new HashMap<>();

    public static void loadComponents() throws ClassNotFoundException, IOException, URISyntaxException {
        String packagePath = "edu.escuelaing.tdse.framework.controllers";
        List<Class<?>> classes = new ArrayList<>();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        // Convierte el nombre del paquete (puntos) a una ruta (slashes)
        String path = packagePath.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);

        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            String protocol = resource.getProtocol();

            // CASO 1: Las clases están dentro de un archivo JAR
            if ("jar".equals(protocol)) {
                String jarPath = resource.getPath().substring(5, resource.getPath().indexOf("!"));
                try (JarFile jar = new JarFile(jarPath)) {
                    Enumeration<JarEntry> entries = jar.entries();
                    while (entries.hasMoreElements()) {
                        JarEntry entry = entries.nextElement();
                        String name = entry.getName();
                        if (name.startsWith(path) && name.endsWith(".class") && !entry.isDirectory()) {
                            // Convierte la ruta del archivo a un nombre de clase completamente calificado
                            String className = name.substring(0, name.length() - 6).replace('/', '.');
                            classes.add(Class.forName(className));
                        }
                    }
                }
            }
            // CASO 2: Las clases son archivos sueltos en el sistema de archivos (ejecución
            // desde IDE)
            else if ("file".equals(protocol)) {
                File[] files = new File(resource.toURI()).listFiles();
                if (files != null) {
                    for (File file : files) {
                        if (file.isFile() && file.getName().endsWith(".class")) {
                            String className = packagePath + "."
                                    + file.getName().substring(0, file.getName().length() - 6);
                            classes.add(Class.forName(className));
                        }
                    }
                }
            }
        }

        // Una vez encontradas todas las clases, las procesamos para buscar anotaciones
        for (Class<?> clazz : classes) {
            processClass(clazz);
        }
    }

    /**
     * Procesa una clase para encontrar métodos anotados y los registra.
     * 
     * @param controllerClass La clase a procesar.
     */
    private static void processClass(Class<?> controllerClass) {
        if (!controllerClass.isAnnotationPresent(RestController.class)) {
            return;
        }

        logger.info("Found controller: " + controllerClass.getName());
        for (Method m : controllerClass.getDeclaredMethods()) {
            if (m.isAnnotationPresent(GetMapping.class)) {
                GetMapping a = m.getAnnotation(GetMapping.class);
                servicesGet.put(a.value(), m);
                logger.info("  - Mapped GET " + a.value() + " to method " + m.getName());
            } else if (m.isAnnotationPresent(PostMapping.class)) {
                PostMapping a = m.getAnnotation(PostMapping.class);
                servicesPost.put(a.value(), m);
                logger.info("  - Mapped POST " + a.value() + " to method " + m.getName());
            }
        }
    }

    public static Method getGetService(String path) {
        return servicesGet.get(path);
    }

    public static Method getPostService(String path) {
        return servicesPost.get(path);
    }

}