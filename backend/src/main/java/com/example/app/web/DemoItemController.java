package com.example.app.web;

import com.example.app.domain.DemoItem;
import com.example.app.dto.DemoItemDto;
import com.example.app.repo.DemoItemRepository;
import com.example.app.dto.DemoItemCreateUpdateDto;
import com.example.app.web.mapper.DemoItemMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST que expone las operaciones CRUD para la gestión de los {@link DemoItem}.
 *
 * Esta clase es el punto de entrada HTTP de l'API para los elementos Demo.
 * Delga el acceso a los dato en {@link DemoItemRepository} y usa  * {@link DemoItemMapper}
 * para convertir las entidades del dominio en DTO expuestos al cliente.
 * Los endpoints provistos permiten el listado, la recuperación, la creación, la modificación
 * y la supresión de elementos.
 *
 * Los parámetros de paginación y de selección se gestionan manualmente para ofrecer una
 * flexibilidad máxima al cliente. Los errores corrientes (elemento no encontrado,
 * validación, unicidad de nombre) son reenviados en forma de respuesta HTTP adaptada.
 */
@RestController
@RequestMapping("/api/items")
public class DemoItemController {

    private final DemoItemRepository repo;
    private static final String ITEM = "Item";
    private static final String NO_ENC = " no encontrado";

    /**
     * Constructor de la clase.
     * @param repo repositorio de la clase.
     */
    public DemoItemController(DemoItemRepository repo) {
        this.repo = repo;
    }

    /**
     * Devuelve una página de élementos Demo, con paginación y selección configurables.
     *
     * @param page número de página (valor por defecto : 0)
     * @param size tamaño de la página (valor por defecto : 20)
     * @param sort criterio de selección en formato "campo,orden" (ejemplo. "nombre,desc")
     * @return una página de DTO que representa a los elementos
     */
    @GetMapping
    public Page<DemoItemDto> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id,asc") String sort
    ) {
        String[] s = sort.split(",", 2);
        Sort order = Sort.by(Sort.Direction.fromString(s.length > 1 ? s[1] : "asc"), s[0]);
        Page<DemoItem> p = repo.findAll(PageRequest.of(page, size, order));
        return p.map(DemoItemMapper::toDto);
    }

    /**
     * Recupera un elemento Demo por medio de su id.
     *
     * @param id identificador del elemento
     * @return  DTO correspondiente
     * @throws EntityNotFoundException si el elemento no existe.
     */
    @GetMapping("/{id}")
    public Object get(@PathVariable Long id) {
        DemoItem e = repo.findById(id).orElseThrow(() -> new EntityNotFoundException(ITEM + id + NO_ENC));
        return DemoItemMapper.toDto(e);
    }

    /**
     * Crea un elemento nuevoa partir de los données provistos.
     *
     * Verifica igualmente la uniciad del nombre para evitar elementos
     * en doble.
     *
     * @param dto datos de creación validados
     * @return  DTO del elemento creado o bien un error 400 si el nombre ya ha sido utilizado
     */
    @PostMapping
    public Object create(@Valid @RequestBody DemoItemCreateUpdateDto dto) {
        // (Optional) sirve para verificar la unicidad del nombre
        if (repo.existsByNameIgnoreCase(dto.name())) {
            return ResponseEntity.badRequest().body(
                    java.util.Map.of("status", 400, "error", "Bad Request", "message", "Nombre ya ha sido utilizado", "path", "/api/items")
            );
        }
        DemoItem e = new DemoItem();
        DemoItemMapper.apply(e, dto);
        e = repo.save(e);
        return DemoItemMapper.toDto(e);
    }

    /**
     * Actaliza un elemento existente con los datos provistos.
     *
     * @param id id del elemento a modificar
     * @param dto datos validados que se van a aplicar
     * @return le DTO actualizado
     * @throws EntityNotFoundException si el elemento no existe
     */
    @PutMapping("/{id}")
    public Object update(@PathVariable Long id, @Valid @RequestBody DemoItemCreateUpdateDto dto) {
        DemoItem e = repo.findById(id).orElseThrow(() -> new EntityNotFoundException(ITEM + id + NO_ENC));
        DemoItemMapper.apply(e, dto);
        e = repo.save(e);
        return DemoItemMapper.toDto(e);
    }

    /**
     * Suprime un elemento Demo usando su id.
     *
     * @param id id del elemento a suprimir
     * @return Respuesta 204 si la supresión se efectuá correctamente
     * @throws EntityNotFoundException si l'élément n'existe pas
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repo.existsById(id)) throw new EntityNotFoundException(ITEM + id + NO_ENC);
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}