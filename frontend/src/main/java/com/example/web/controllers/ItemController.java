package com.example.web.controllers;

import com.example.web.dto.ItemDto;
import com.example.web.services.ItemService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador principal para la gestión del catálogo de ítems.
 * <p>
 * Ofrece una interfaz pública para la visualización y una interfaz restringida
 * para operaciones administrativas (Creación, Edición, Eliminación).
 * Utiliza {@link ItemDto} para la transferencia de datos y {@link ItemService} para la lógica.
 */
@Controller
@RequestMapping("/items")
public class ItemController {

    private final ItemService service;

    /**
     * Inyección de dependencias del servicio de negocio.
     * @param service Servicio que gestiona la persistencia y lógica de los ítems.
     */
    public ItemController(ItemService service) {

        this.service = service;
    }

    // ------------------------------------------------------------
    // 1) LISTA PUBLICA DE LOS ITEMS  → accesible à todos
    // ------------------------------------------------------------
    /**
     * Recupera y muestra la lista completa de ítems.
     * Accesible para todos los usuarios autenticados o anónimos.
     *
     * @param m Modelo de UI para enviar la lista de ítems a la vista.
     * @return El nombre del template "items".
     */
    @GetMapping("")
    public String list(Model m) {
        m.addAttribute("items", service.findAll());
        m.addAttribute("pageName", "Lista de items");
        return "items";   // templates/items.html
    }

    // ------------------------------------------------------------
    // 2) CREACION DE UN ITEM  → ADMIN únicamente
    // ------------------------------------------------------------
    /**
     * Muestra el formulario de creación de un nuevo ítem.
     * Restringido a usuarios con rol ADMIN.
     *
     * @param m Modelo donde se inicializa un DTO vacío para el formulario.
     * @return El nombre del template "item-new".
     */
    @GetMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    public String newItem(Model m) {
        m.addAttribute("item", new ItemDto());
        m.addAttribute("pageName", "Nuevo item");
        return "item-new";   // templates/item-new.html
    }

    /**
     * Procesa la creación de un nuevo ítem.
     *
     * @param dto Objeto de transferencia validado mediante anotaciones de Jakarta Validation.
     * @return Redirección a la lista general tras el éxito.
     */
    @PostMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    public String create(@Valid @ModelAttribute("item") ItemDto dto) {
        service.create(dto);
        return "redirect:/items";
    }

    // ------------------------------------------------------------
    // 3) MODIFICACION DE UN ITEM  → ADMIN únicamente
    // ------------------------------------------------------------
    /**
     * Prepara el formulario de edición con los datos de un ítem existente.
     *
     * @param id Identificador único del ítem a editar.
     * @param m Modelo para pasar los datos actuales a la vista.
     * @return El template "item-edit" o redirección si el ítem no existe.
     */
    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String editForm(@PathVariable Long id, Model m) {
        // Si findById devuelve un Optional, hace un .get() sino un  .orElseThrow()
        ItemDto dto = service.findById(id);
        if (dto == null) return "redirect:/items";

        m.addAttribute("item", dto);
        m.addAttribute("pageName", "Modificar item");
        return "item-edit";
    }

    /**
     * Procesa la actualización de un ítem existente.
     *
     * @param id ID del ítem a modificar (obtenido de la URL).
     * @param dto Datos actualizados desde el formulario.
     * @return Redirección a la lista general.
     */
    @PostMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String editSubmit(@PathVariable Long id, @ModelAttribute ItemDto dto) {
        service.update(id, dto);
        return "redirect:/items";
    }

    // ------------------------------------------------------------
    // 4) SUPRESION DE UN ITEM  → ADMIN únicamente
    // ------------------------------------------------------------
    /**
     * Elimina un ítem del sistema.
     *
     * @param id ID del ítem a suprimir.
     * @return Redirección a la lista general tras completar la operación.
     */
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/items";
    }
}