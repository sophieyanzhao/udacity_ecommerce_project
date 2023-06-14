package com.example.demo.repositories;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;

import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.UserRepository;

import java.util.List;
import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.Item;

@DataJpaTest
@RunWith(SpringRunner.class)
public class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ItemRepository itemRepo;        


    @Test
    public void testFindByUser(){
        User user = TestUtils.createUserObject("test","testpassword");
        Cart cart = new Cart();
        Item item = TestUtils.createItemObject("testItem", "test description","1.99");
        itemRepo.save(item);
        
        cart.addItem(item);
        cart.setUser(user);
        user.setCart(cart);
        userRepo.save(user);
        
        UserOrder order = UserOrder.createFromCart(cart);
        orderRepo.save(order);
        List<UserOrder> orders = orderRepo.findByUser(user);
        assertEquals(1,orders.size());
    }
    
    
}
