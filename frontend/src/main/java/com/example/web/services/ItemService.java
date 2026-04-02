package com.example.web.services;

import com.example.web.dto.ItemDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class ItemService {

    private final RestTemplate rest = new RestTemplate();

    // Comunicación directa con py2, Nginx bloquea
    @Value("${services.py2.items-url}")
    private String py2Url;

    public List<ItemDto> findAll() {
        ItemDto[] items = rest.getForObject(py2Url, ItemDto[].class);
        return Arrays.asList(items);
    }

    public ItemDto findById(Long id) {

        return rest.getForObject(py2Url + "/" + id, ItemDto.class);
    }

    public void create(ItemDto item) {
        if (item.getDescription() == null || item.getDescription().isBlank()) {
            item.setDescription("N/A");
        }
        rest.postForObject(py2Url, item, ItemDto.class);
    }

    public void update(Long id, ItemDto item) {
        if (item.getDescription() == null || item.getDescription().isBlank()) {
            item.setDescription("N/A");
        }
        // Así se asegura que la URL es correcta (en este caso sería: http://localhost:5000/items/1)
        rest.put(py2Url + "/" + id, item);
    }


    public void delete(Long id) {

        rest.delete(py2Url + "/" + id);
    }
}