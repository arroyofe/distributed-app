package com.example.web.services;

import com.example.web.models.ItemDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Arrays;
import java.util.List;

@Service
public class ItemService {

    private final RestTemplate rest = new RestTemplate();
    // private final String BASE_URL = "http://localhost/api3/items"; Antiguo nuevo abajo a borrar si fucniona el de abajo
    //
    private final String BASE_URL = "http://nginx/api3/items";
    public List<ItemDto> findAll() {
        ItemDto[] items = rest.getForObject(BASE_URL, ItemDto[].class);
        return Arrays.asList(items);
    }

    public ItemDto findById(Long id) {
        return rest.getForObject(BASE_URL + "/" + id, ItemDto.class);
    }

    public void create(ItemDto item) {
        rest.postForObject(BASE_URL, item, ItemDto.class);
    }

    public void update(Long id, ItemDto item) {
        rest.patchForObject(BASE_URL + "/" + id, item, Void.class);
    }

    public void delete(Long id) {
        rest.delete(BASE_URL + "/" + id);
    }
}