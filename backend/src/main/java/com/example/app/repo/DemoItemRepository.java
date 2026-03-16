package com.example.app.repo;

import com.example.app.domain.DemoItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DemoItemRepository extends JpaRepository<DemoItem, Long> {
    boolean existsByNameIgnoreCase(String name);
}