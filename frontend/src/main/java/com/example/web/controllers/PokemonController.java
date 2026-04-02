package com.example.web.controllers;

import com.example.web.dto.PokemonDto;
import com.example.web.services.PokemonService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/pokemons")
public class PokemonController {

    private static final String REDIR_ADMIN = "redirect:/pokemons/admin";
    private final PokemonService pokemonService;

    public PokemonController(PokemonService pokemonService) {

        this.pokemonService = pokemonService;
    }

    // Accesible a los usuarios conectados
    @GetMapping("")
    public String list(Model model) {
        List<PokemonDto> pokemons = pokemonService.findAll();
        model.addAttribute("pokemons", pokemons);
        model.addAttribute("pageName", "Bibliothèque Pokémon");
        return "pokemons-list"; // Nom du fichier HTML
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminPanel(Model model) {
        model.addAttribute("pokemons", pokemonService.findAll());
        return "pokemons-admin";
    }

    // En los parámetros si hay algún problema pondrá desconocido si es String y 1 si es un entero
    @PostMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    public String createPokemon(@RequestParam String name,
                                @RequestParam(required = false, defaultValue = "Desconocido") String type,
                                @RequestParam(required = false, defaultValue = "1") Integer level) {
        PokemonDto newP = new PokemonDto();
        newP.setName(name);
        newP.setType(type);
        newP.setLevel(level);

        pokemonService.create(newP); // Llamada al servicio
        return REDIR_ADMIN;
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String delete(@PathVariable Long id) {
        pokemonService.delete(id);
        return REDIR_ADMIN;
    }

    // --- PARA VER EL FORMULARIO (GET) ---
    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String editForm(@PathVariable Long id, Model model) {
        PokemonDto pokemon = pokemonService.findById(id);
        model.addAttribute("pokemon", pokemon);
        model.addAttribute("pageName", "Modifier Pokémon");
        return "pokemon-edit";
    }

    // --- PARA REGISTRAR (POST UNICAMENTE) ---
    @PostMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String processUpdate(@PathVariable Long id, @ModelAttribute("pokemon") PokemonDto dto) {
        // IMPORTANT : On utilise un nom de méthode différent (processUpdate au lieu de editForm)
        dto.setId(id);
        pokemonService.update(id, dto);
        return REDIR_ADMIN;
    }

}
