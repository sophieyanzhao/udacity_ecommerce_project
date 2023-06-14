package com.example.demo.repositories;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.runner.RunWith;
import org.junit.Before;
import static org.junit.Assert.assertNotNull;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.persistence.repositories.CartRepository;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import com.example.demo.TestUtils;


@DataJpaTest
@RunWith(SpringRunner.class)
public class CartRepositoryTest {
    @Autowired
    private CartRepository cartRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private EntityManager entityManager;

    @Before
    @Transactional
    public void setUp(){
        cartRepo.deleteAll();
        entityManager.createNativeQuery("ALTER TABLE cart ALTER COLUMN id RESTART WITH 1").executeUpdate();
        userRepo.deleteAll();
        entityManager.createNativeQuery("ALTER TABLE user ALTER COLUMN id RESTART WITH 1").executeUpdate();
    }

    @Test
    public void testFindByUser(){
        User user = TestUtils.createUserObject("test","testpassword");
        Cart cart = new Cart();
        user.setCart(cart);
        userRepo.save(user);
        Cart cartFound = cartRepo.findByUser(user);
        assertNotNull(cart);
        assert(cartFound.getId()==1);
    }
}