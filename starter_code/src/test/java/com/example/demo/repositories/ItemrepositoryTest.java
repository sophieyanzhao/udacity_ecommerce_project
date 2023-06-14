package com.example.demo.repositories;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;
import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;

import java.math.BigDecimal;
import java.util.List;

@DataJpaTest
@RunWith(SpringRunner.class)
public class ItemrepositoryTest {
    @Autowired
    private ItemRepository itemRepo;

    @Test
    public void testFindByName(){
        Item item = TestUtils.createItemObject("test item", "test description","1.99");
        itemRepo.save(item);
        List<Item> items = itemRepo.findByName("test item");
        assertEquals(1,items.size());
        assert !items.get(0).equals(null);
        Item item2 = TestUtils.createItemObject("test item", "test description","1.99");
        item2.setId(0L);
        assert !items.get(0).equals(item2);
        assert item.hashCode()==items.get(0).hashCode();
        assert items.get(0).equals(item);
        assert items.get(0).getName() == "test item";
        assert items.get(0).getDescription() == "test description";
        assert items.get(0).getPrice().equals(BigDecimal.valueOf(1.99));
        

    }
    

}
