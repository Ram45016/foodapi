package com.trickytechies.foodapi.service;

import com.trickytechies.foodapi.model.MenuItem;
import com.trickytechies.foodapi.repository.MenuItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MenuItemService {

    @Autowired
    private MenuItemRepository menuItemRepository;

    // Get MenuItem by ID and Branch Name
    public Optional<MenuItem> getMenuItemByIdAndBranchName(Long id, String branchName) {
        return menuItemRepository.findByIdAndBranchName(id, branchName);
    }

    // Get all MenuItems by Branch Name
    public List<MenuItem> getMenuItemsByBranchName(String branchName) {
        return menuItemRepository.findByBranchName(branchName);
    }

    // Add a new MenuItem
    public MenuItem addMenuItem(MenuItem menuItem) {
        return menuItemRepository.save(menuItem);
    }

    // Update an existing MenuItem
    public Optional<MenuItem> updateMenuItem(Long id, MenuItem menuItem) {
        if (menuItemRepository.existsById(id)) {
            menuItem.setId(id);
            return Optional.of(menuItemRepository.save(menuItem));
        }
        return Optional.empty();
    }

    // Delete a MenuItem by ID
    public boolean deleteMenuItem(Long id) {
        if (menuItemRepository.existsById(id)) {
            menuItemRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
