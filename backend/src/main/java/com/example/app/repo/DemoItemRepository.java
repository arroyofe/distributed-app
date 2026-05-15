package com.example.app.repo;

import com.example.app.domain.DemoItem;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository Spring Data JPA para la gestión de las entidades {@link DemoItem}.
 * <p>
 * Este interfaz permite las operaciones CRUD estándar por medio de {@link JpaRepository}
 * y expone los métodos de petición que se derivan y con lo que se puede interrogar sobre
 * la existencia de elementos según los criterios necesarios. Sirve de capa de acceso a los
 * datos actuando de intermediario entre el servicio y la base de datos, lo que evita la
 * escritura manual de de peticiones SQL o JPQL.
 */
public interface DemoItemRepository extends JpaRepository<DemoItem, Long> {

    /**
     * Vérifica si un elemento ya exite en la base usando el nombre de dicho elementos,
     * y sin tener en cuenta mayúsculas o minúsculas.
     * Este método se genera automática por Srping.
     * Los datos de  se generan a partir de la convención de nombres para evitar escribir
     * peticiones explícitas.
     *
     * @param name nombre que se debe verificar.
     * @return {@code true} si un elemento con ese nombre existe,
     *         {@code false} en caso contrario
     */
    boolean existsByNameIgnoreCase(String name);
}
