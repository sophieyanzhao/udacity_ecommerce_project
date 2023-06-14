package com.example.demo.repositories;


import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.runner.RunWith;
import org.junit.Before;
import static org.junit.Assert.assertEquals;

import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.UserRepository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@DataJpaTest
@RunWith(SpringRunner.class)
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private EntityManager entityManager;

    @Before
    @Transactional
    public void setUp(){
        userRepo.deleteAll();
        entityManager.createNativeQuery("ALTER TABLE user ALTER COLUMN id RESTART WITH 1").executeUpdate();
    }
    
    @Test
    public void testFindById() {
        User user1=createUserObject("test1","testpassword1");
        userRepo.save(user1);
        User user2 = createUserObject("test2","testpassword2");
        userRepo.save(user2);
        User userResponse = userRepo.findById(2L).get();
        assertEquals("test2",userResponse.getUsername());
        assertEquals("testpassword2",userResponse.getPassword());
    }
    public User createUserObject(String username, String password){
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        return user;
    }

    @Test
    public void testFindByUserName() {
        User user1=createUserObject("test1","testpassword1");
        userRepo.save(user1);
        User user2 = createUserObject("test2","testpassword2");
		userRepo.save(user2);
        User userResponse = userRepo.findByUsername("test2");
        assertEquals(2,userResponse.getId());
        assertEquals("test2",userResponse.getUsername());
    }
    
    @Test
    public void testCreateUser() {
        User user = createUserObject("test","testpassword");
        userRepo.save(user);
        List<User> users=userRepo.findAll();
        assert(users.size()==1);
        assert(users.get(0).getUsername().equals("test"));
        assert(users.get(0).getPassword().equals("testpassword"));
        assert(users.get(0).getId()==1);
    }
    
}
