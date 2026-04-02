package com.example.web.controllers;

import com.example.web.dto.ItemDto;
import com.example.web.services.ItemService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/items")
public class ItemController {

    private final ItemService service;

    public ItemController(ItemService service) {
        this.service = service;
    }

    // ------------------------------------------------------------
    // 1) LISTA PUBLICA DE LOS ITEMS  → accesible à todos
    // ------------------------------------------------------------
    @GetMapping("")
    public String list(Model m) {
        m.addAttribute("items", service.findAll());
        m.addAttribute("pageName", "Lista de items");
        return "items";   // templates/items.html
    }

    // ------------------------------------------------------------
    // 2) CREACION DE UN ITEM  → ADMIN únicamente
    // ------------------------------------------------------------
    @GetMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    public String newItem(Model m) {
        m.addAttribute("item", new ItemDto());
        m.addAttribute("pageName", "Nuevo item");
        return "item-new";   // templates/item-new.html
    }

    @PostMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    public String create(@Valid @ModelAttribute("item") ItemDto dto) {
        service.create(dto);
        return "redirect:/items";
    }

    // ------------------------------------------------------------
    // 3) MODIFICACION DE UN ITEM  → ADMIN únicamente
    // ------------------------------------------------------------
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

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String editSubmit(@PathVariable Long id, @ModelAttribute ItemDto dto) {
        service.update(id, dto);
        return "redirect:/items";
    }

    // ------------------------------------------------------------
    // 4) SUPRESION DE UN ITEM  → ADMIN únicamente
    // ------------------------------------------------------------
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/items";
    }
}