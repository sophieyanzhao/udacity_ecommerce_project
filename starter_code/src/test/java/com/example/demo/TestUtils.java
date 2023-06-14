package com.example.demo;

import java.lang.reflect.Field;
import java.math.BigDecimal;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;

public class TestUtils {
    public static void injectObjects(Object target, String fieldName, Object toInject) {

        boolean wasPrivate = false;
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            if (!field.isAccessible()){
                field.setAccessible(true);
                wasPrivate = true;
            }
            field.set(target, toInject);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        
    }

    public static User createUserObject(String username, String password){
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setId(0);
        return user;
    }

    public static Item createItemObject(String name, String description, String price){
        Item item = new Item();
        item.setName(name);
        item.setPrice(new BigDecimal(price));
        item.setDescription(description);
        return item;
}
}