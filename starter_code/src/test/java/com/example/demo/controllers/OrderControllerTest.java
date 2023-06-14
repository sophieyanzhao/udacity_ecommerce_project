package com.example.demo.controllers;

import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.when;

import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class OrderControllerTest {
    private UserRepository userRepository = mock(UserRepository.class);
    private OrderRepository orderRepository = mock(OrderRepository.class);
    private OrderController orderController;


    @Before
    public void setUp(){
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
        User mockUser = TestUtils.createUserObject("test","testPassword");
        Cart mockCart = new Cart();
        mockCart.setUser(mockUser);
        List<Item> items = new ArrayList<Item>();
        Item mockItem = TestUtils.createItemObject("testItem", "testDescription", "0.99");
        mockItem.setId(1L);
        items.add(mockItem);
        mockCart.setItems(items);
        mockCart.setTotal(BigDecimal.valueOf(0.99));
        mockUser.setCart(mockCart);
        when(userRepository.findByUsername("test")).thenReturn(mockUser);

    }


    @Test
    public void testSubmit(){
        ResponseEntity<UserOrder> response = orderController.submit("test");
        assert response.getStatusCodeValue()==200;
        UserOrder order = response.getBody();
        assert order.getItems().size()==1;
        assert order.getTotal().equals(BigDecimal.valueOf(0.99));
    }

    @Test
    public void testGetOrdersForUser(){
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("test");
        assert response.getStatusCodeValue()==200;
        List<UserOrder> orders = response.getBody();
        assert orders.size()==0;
    }


}
