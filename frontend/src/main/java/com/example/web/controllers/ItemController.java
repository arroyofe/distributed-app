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

    @GetMapping("/items")
    public String list(Model m) {
        m.addAttribute("items", service.findAll());
        m.addAttribute("pageName", "Lista de items");
        return "items";
    }

    @GetMapping("/admin/items/new")
    @PreAuthorize("hasRole('ADMIN')")
    public String newItem(Model m) {
        m.addAttribute("item", new ItemDto());
        m.addAttribute("pageName", "Nuevo item");
        return "item-new";
    }

    @PostMapping("/admin/items/new")
    @PreAuthorize("hasRole('ADMIN')")
    public String create(@Valid @ModelAttribute("item") ItemDto dto) { //@Valid valida los formularios
        service.create(dto);
        return "redirect:/items";
    }

    @GetMapping("/admin/items/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String editForm(@PathVariable Long id, Model m) {
        m.addAttribute("item", service.findById(id));
        m.addAttribute("pageName", "Modificar item");
        return "item-edit";
    }

    @PostMapping("/admin/items/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String editSubmit(@PathVariable Long id, @ModelAttribute ItemDto dto) {
        service.update(id, dto);
        return "redirect:/items";
    }

    @PostMapping("/admin/items/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/items";
    }
}