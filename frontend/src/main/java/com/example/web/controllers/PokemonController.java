package com.example.web.controllers;

import com.example.web.dto.PokemonDto;
import com.example.web.services.PokemonService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador de gestión de la enciclopedia Pokémon.
 * <p>
 * Centraliza las operaciones de visualización pública y administración privada de los datos.
 * Interactúa con {@link PokemonService} para reflejar los cambios en la base de datos compartida
 * con los módulos Python (py1 y py2).
 */
@Controller
@RequestMapping("/pokemons")
public class PokemonController {

    private static final String REDIR_ADMIN = "redirect:/pokemons/admin";
    private final PokemonService pokemonService;

    /**
     * Inyección del servicio de lógica de negocio para Pokémon.
     * @param pokemonService servicio encargado del CRUD de Pokémon.
     */
    public PokemonController(PokemonService pokemonService) {

        this.pokemonService = pokemonService;
    }

    /**
     * Lista todos los Pokémon disponibles para los usuarios autenticados.
     *
     * @param model objeto de UI para transferir la lista de Pokémon a la vista.
     * @return el nombre del template "pokemons-list".
     */
    @GetMapping("")
    public String list(Model model) {
        List<PokemonDto> pokemons = pokemonService.findAll();
        model.addAttribute("pokemons", pokemons);
        model.addAttribute("pageName", "Bibliothèque Pokémon");
        return "pokemons-list"; // Nom du fichier HTML
    }

    /**
     * Acceso al panel administrativo para la gestión avanzada de la base de datos.
     * Requiere rol ADMIN.
     *
     * @param model modelo para la visualización de datos administrativos.
     * @return el template "pokemons-admin".
     */
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminPanel(Model model) {
        model.addAttribute("pokemons", pokemonService.findAll());
        return "pokemons-admin";
    }

       /**
     * Crea un nuevo registro de Pokémon en el sistema.
     * <p>
     * Implementa lógica de seguridad y robustez mediante valores por defecto:
     * - Si el tipo no se especifica, se asigna "Desconocido".
     * - Si el nivel no se especifica, se asigna 1 por defecto.
     *
     * @param name Nombre del Pokémon (obligatorio).
     * @param type Tipo de Pokémon (opcional, por defecto "Desconocido").
     * @param level Nivel inicial (opcional, por defecto 1).
     * @return redirección al panel de administración.
     */
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

    /**
     * Elimina de forma permanente un Pokémon del sistema.
     *
     * @param id Identificador único del Pokémon a suprimir.
     * @return redirección al panel de administración.
     */
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String delete(@PathVariable Long id) {
        pokemonService.delete(id);
        return REDIR_ADMIN;
    }

    /**
     * Carga el formulario de edición con los datos actuales de un Pokémon.
     *
     * @param id Identificador del Pokémon a modificar.
     * @param model modelo para pasar el objeto DTO a la vista.
     * @return el template "pokemon-edit".
     */
    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String editForm(@PathVariable Long id, Model model) {
        PokemonDto pokemon = pokemonService.findById(id);
        model.addAttribute("pokemon", pokemon);
        model.addAttribute("pageName", "Modifier Pokémon");
        return "pokemon-edit";
    }

    /**
     * Procesa la actualización de los datos de un Pokémon existente.
     *
     * @param id ID del registro a actualizar.
     * @param dto Objeto con los nuevos datos capturados del formulario.
     * @return redirección al panel de administración tras guardar los cambios.
     */
    @PostMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String processUpdate(@PathVariable Long id, @ModelAttribute("pokemon") PokemonDto dto) {
        // IMPORTANT : On utilise un nom de méthode différent (processUpdate au lieu de editForm)
        dto.setId(id);
        pokemonService.update(id, dto);
        return REDIR_ADMIN;
    }

}
