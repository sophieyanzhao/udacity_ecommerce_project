package com.example.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/item")
public class ItemController {

	@Autowired
	private ItemRepository itemRepository;

	private final Logger logger = LoggerFactory.getLogger(ItemController.class);
	
	@GetMapping
	public ResponseEntity<List<Item>> getItems() {
		try {
			return ResponseEntity.ok(itemRepository.findAll());
		} catch (Exception e) {
			logger.error("Error getting all items", e);
			return ResponseEntity.badRequest().build();
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Item> getItemById(@PathVariable Long id) {
		try {
			return ResponseEntity.of(itemRepository.findById(id));
		} catch (Exception e) {
			logger.error("Error getting item by id: {} " + id, e);
			return ResponseEntity.notFound().build();
		}
	}
	
	@GetMapping("/name/{name}")
	public ResponseEntity<List<Item>> getItemsByName(@PathVariable String name) {
		try {
			List<Item> items = itemRepository.findByName(name);
			return items == null || items.isEmpty() ? ResponseEntity.notFound().build()
					: ResponseEntity.ok(items);
		} catch (Exception e) {
			logger.error("Error getting items by name: {} " + name, e);
			return ResponseEntity.badRequest().build();
		}
	}	
}
