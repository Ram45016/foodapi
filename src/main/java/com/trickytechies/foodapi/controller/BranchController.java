package com.trickytechies.foodapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.trickytechies.foodapi.model.Branch;
import com.trickytechies.foodapi.service.BranchService;

import java.util.List;

@RestController
@RequestMapping("/branches")
public class BranchController {

    @Autowired
    private BranchService branchService;

    @PostMapping
    public Branch saveBranch(@RequestBody Branch branch) {
        System.out.println(branch);
        return branchService.saveBranch(branch);
    }

    @GetMapping
    public List<Branch> getAllBranches() {
        return branchService.getAllBranches();
    }

    @GetMapping("/search")
    public List<Branch> searchBranches(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "location", required = false) String location) {
        return branchService.searchBranches(name, location);
    }

    @GetMapping("/{id}")
    public Branch getBranchById(@PathVariable Long id) {
        return branchService.getBranchById(id);
    }
    @PutMapping("/{id}")
    public Branch updateBranch(@PathVariable Long id, @RequestBody Branch branch) {
        return branchService.updateBranch(id, branch);
    }

    // DELETE: Delete a branch by ID
    @DeleteMapping("/{id}")
    public String deleteBranch(@PathVariable Long id) {
        branchService.deleteBranch(id);
        return "Branch with ID " + id + " has been deleted.";
    }
}
