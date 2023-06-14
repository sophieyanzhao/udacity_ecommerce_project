package com.example.demo.controllers;

import java.util.ArrayList;
import java.util.List;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import org.springframework.http.ResponseEntity;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;



public class ItemControllerTest {
    private ItemController itemController;
    private ItemRepository itemRepository=mock(ItemRepository.class);
    
    @Before 
    public void setUp(){
        itemController=new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
        Item mockItem = new Item();
        mockItem.setId(1L);
        mockItem.setName("testItem");
        List<Item> mockItems = new ArrayList<Item>();
        mockItems.add(mockItem);
        when(itemRepository.findAll()).thenReturn(mockItems);
        when(itemRepository.findById(0L)).thenReturn(java.util.Optional.of(mockItem));
        when(itemRepository.findByName("testItem")).thenReturn(mockItems);
    }

    @Test
    public void testGetItems() {
        ResponseEntity<List<Item>> response = itemController.getItems();
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        List<Item> items = response.getBody();        
        assert items.size()==1;
    }

    @Test
    public void testGetItemById() {
        ResponseEntity<Item> response = itemController.getItemById(0L);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        Item item = response.getBody();
        assertEquals("testItem", item.getName());
        
    }


    @Test
    public void testGetItemsByName() {
        ResponseEntity<List<Item>> response = itemController.getItemsByName("testItem");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        List<Item> items = response.getBody();
        assertNotNull(items);
        assertEquals("testItem", items.get(0).getName());
        
    }

}
