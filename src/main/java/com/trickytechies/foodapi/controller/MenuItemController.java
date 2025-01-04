package com.trickytechies.foodapi.controller;

import com.trickytechies.foodapi.model.MenuItem;
import com.trickytechies.foodapi.model.Branch;
import com.trickytechies.foodapi.service.BranchService;
import com.trickytechies.foodapi.service.MenuItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/menu")
public class MenuItemController {

    @Autowired
    private MenuItemService menuItemService;

    @Autowired
    private BranchService branchService;

    // Endpoint to fetch a menu item by branch name and item ID
    @GetMapping("/{branchName}/{itemId}")
    public ResponseEntity<MenuItem> getMenuItemDetails(
            @PathVariable String branchName,
            @PathVariable Long itemId) {

        Optional<MenuItem> menuItem = menuItemService.getMenuItemByIdAndBranchName(itemId, branchName);
        return menuItem.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Endpoint to add a new menu item
    @PostMapping("/{branchName}")
    @PreAuthorize("hasRole('ADMIN') and @branchService.getBranchByName(#branchName).getAdmin().getUsername() == authentication.name")
    public ResponseEntity<MenuItem> addMenuItem(
            @PathVariable String branchName,
            @RequestBody MenuItem menuItem) {

        // Check if branch exists and if the current user is the branch admin
        Branch branch = branchService.getBranchByName(branchName);
        if (branch == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        // Check if the current user is the branch admin
        if (!isAdminForBranch(branch)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        menuItem.setBranch(branch);
        MenuItem savedMenuItem = menuItemService.addMenuItem(menuItem);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMenuItem);
    }

    // Endpoint to update an existing menu item
    @PutMapping("/{branchName}/{itemId}")
    @PreAuthorize("hasRole('ADMIN') and @branchService.getBranchByName(#branchName).getAdmin().getUsername() == authentication.name")
    public ResponseEntity<MenuItem> updateMenuItem(
            @PathVariable String branchName,
            @PathVariable Long itemId,
            @RequestBody MenuItem menuItem) {

        // Check if branch exists and if the current user is the branch admin
        Branch branch = branchService.getBranchByName(branchName);
        if (branch == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        // Check if the current user is the branch admin
        if (!isAdminForBranch(branch)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        menuItem.setBranch(branch);
        Optional<MenuItem> updatedMenuItem = menuItemService.updateMenuItem(itemId, menuItem);
        return updatedMenuItem != null ? ResponseEntity.ok(updatedMenuItem.get())
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // Endpoint to delete a menu item
    @DeleteMapping("/{branchName}/{itemId}")
    @PreAuthorize("hasRole('ADMIN') and @branchService.getBranchByName(#branchName).getAdmin().getUsername() == authentication.name")
    public ResponseEntity<Void> deleteMenuItem(
            @PathVariable String branchName,
            @PathVariable Long itemId) {

        // Check if branch exists and if the current user is the branch admin
        Branch branch = branchService.getBranchByName(branchName);
        if (branch == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // Check if the current user is the branch admin
        if (!isAdminForBranch(branch)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        boolean isDeleted = menuItemService.deleteMenuItem(itemId);
        return isDeleted ? ResponseEntity.noContent().build()
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // Endpoint to get all menu items of a branch
    @GetMapping("/{branchName}")
    public ResponseEntity<List<MenuItem>> getAllMenuItemsByBranch(@PathVariable String branchName) {
        List<MenuItem> menuItems = menuItemService.getMenuItemsByBranchName(branchName);
        return menuItems.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).build()
                : ResponseEntity.ok(menuItems);
    }

    // Helper method to check if the logged-in user is the admin of the branch
    private boolean isAdminForBranch(Branch branch) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        return branch.getAdmin() != null && branch.getAdmin().getUsername().equals(currentUser);
    }
}
