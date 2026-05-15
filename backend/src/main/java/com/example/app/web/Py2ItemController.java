package com.example.app.web;

import com.example.app.service.Py2ItemService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
/**
 * Controlador REST que sirve de pasaela entre el backend Java y el módulo Python.
 * <p>
 * Esta clase expone una API simple que permite la recuperació, la creación y la supresión
 * de elementos gestionado por el servio Python. Delefa el conjunto de la lógica de llamada
 * HTTP al {@link Py2ItemService}, lo cual permite una separación clara de la capa web y la
 * lógica de integración externa.
 * <p>
 * Les datos intercambiados son manipulados en forma de estructuras genéricas ({@code Map<String, Object>})
 *  con el fin de reflejar directement las respuestas JSON enviada por el módulo Python.
 */
@RestController
@RequestMapping("/api/py2/items")
public class Py2ItemController {

    private final Py2ItemService service;

    /**
     * Constructor de la clase
     * @param service que se utiliza
     */
    public Py2ItemController(Py2ItemService service) {

        this.service = service;
    }

    /**
     * Recupera la lista completa de los elemento provistos por el módulo Python.
     *
     * @return una lista de objetos JSON representados en forma de maps
     */
    @GetMapping
    public List<Map<String, Object>> list() {
        return service.getItems();
    }

    /**
     * Envia una petición de creación de un nuevo elmento al módulo Python.
     *
     * @param body datoss JSON que representan el elemento a crear
     * @return resûesta devuelta por el módulo Python, en forma de map
     */
    @PostMapping
    public Map<String, Object> create(@RequestBody Map<String, Object> body) {

        return service.addItem(body);
    }

    /**
     * Solicita la supresión de un elemento identificado por su id.
     *
     * @param id identificador del elemento a suprimir
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {

        service.deleteItem(id);
    }
}

