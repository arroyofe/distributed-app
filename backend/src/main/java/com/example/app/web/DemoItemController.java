package com.example.app.web;

import com.example.app.domain.DemoItem;
import com.example.app.repo.DemoItemRepository;
import com.example.app.dto.DemoItemCreateUpdateDto;
import com.example.app.web.mapper.DemoItemMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/items")
public class DemoItemController {

    private final DemoItemRepository repo;

    public DemoItemController(DemoItemRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public Page<?> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id,asc") String sort
    ) {
        String[] s = sort.split(",", 2);
        Sort order = Sort.by(Sort.Direction.fromString(s.length > 1 ? s[1] : "asc"), s[0]);
        Page<DemoItem> p = repo.findAll(PageRequest.of(page, size, order));
        return p.map(DemoItemMapper::toDto);
    }

    @GetMapping("/{id}")
    public Object get(@PathVariable Long id) {
        DemoItem e = repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Item " + id + " introuvable"));
        return DemoItemMapper.toDto(e);
    }

    @PostMapping
    public Object create(@Valid @RequestBody DemoItemCreateUpdateDto dto) {
        // (Optional) sirve para verificar la unicidad del nombre
        if (repo.existsByNameIgnoreCase(dto.name())) {
            return ResponseEntity.badRequest().body(
                    java.util.Map.of("status", 400, "error", "Bad Request", "message", "Nom déjà utilisé", "path", "/api/items")
            );
        }
        DemoItem e = new DemoItem();
        DemoItemMapper.apply(e, dto);
        e = repo.save(e);
        return DemoItemMapper.toDto(e);
    }

    @PutMapping("/{id}")
    public Object update(@PathVariable Long id, @Valid @RequestBody DemoItemCreateUpdateDto dto) {
        DemoItem e = repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Item " + id + " introuvable"));
        DemoItemMapper.apply(e, dto);
        e = repo.save(e);
        return DemoItemMapper.toDto(e);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repo.existsById(id)) throw new EntityNotFoundException("Item " + id + " introuvable");
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}