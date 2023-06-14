package com.example.demo.controllers;



import java.math.BigDecimal;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import org.springframework.http.ResponseEntity;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import com.example.demo.TestUtils;


public class CartControllerTest {
    private CartController cartController;
    private UserRepository userRepository = mock(UserRepository.class);
    private ItemRepository itemRepository = mock(ItemRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);

    
    @Before
    public void setUp(){
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        User mockUser = TestUtils.createUserObject("test", "testpassword");
        Item mockItem = TestUtils.createItemObject("testItem", "test description", "0.99");
        
        Cart mockCart = new Cart();
        mockCart.setId(1L);
        mockCart.setUser(mockUser);
        ArrayList<Item> mockItems = new ArrayList<Item>();
        mockItem.setId(1L);
        mockItems.add(mockItem);
        mockCart.setItems(mockItems);
        mockCart.setTotal(new BigDecimal("0.99"));
        mockUser.setCart(mockCart);
        when(userRepository.findByUsername("test")).thenReturn(mockUser);
        when(itemRepository.findById(1L)).thenReturn(java.util.Optional.of(mockItem));
        when(itemRepository.findById(2L)).thenReturn(java.util.Optional.of(mockItem));
    }
    
    @Test
    public void testAddToCart(){
        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("test");
        request.setItemId(1);
        request.setQuantity(2);
        ResponseEntity<Cart> response = cartController.addTocart(request);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        Cart cart = response.getBody();
        assertNotNull(cart);
        assertEquals(3, cart.getItems().size());
        assertEquals(new BigDecimal("2.97"), cart.getTotal());

        ModifyCartRequest badRequest = new ModifyCartRequest();
        badRequest.setUsername("nullUser");
        badRequest.setItemId(2);
        badRequest.setQuantity(2);
        ResponseEntity<Cart> badResponse = cartController.addTocart(badRequest);
        assertNotNull(badResponse);
        assertEquals(404, badResponse.getStatusCodeValue());

    }


    @Test
    public void removeFromCart(){
        // test for item not found error (404)
        ModifyCartRequest removalRequest = new ModifyCartRequest();
        removalRequest.setUsername("test");
        removalRequest.setItemId(1);
        removalRequest.setQuantity(1);
        ResponseEntity<Cart> response = cartController.removeFromcart(removalRequest);
        assertEquals(200, response.getStatusCodeValue());
        Cart cart = response.getBody();
        assertEquals(0, cart.getItems().size());
        assertEquals(new BigDecimal("0.00"), cart.getTotal());
        ModifyCartRequest badRequest = new ModifyCartRequest();
        badRequest.setUsername("nullUser");
        badRequest.setItemId(1);
        badRequest.setQuantity(1);
        ResponseEntity<Cart> badResponse = cartController.removeFromcart(badRequest);
        assertEquals(404, badResponse.getStatusCodeValue());
    }
}
