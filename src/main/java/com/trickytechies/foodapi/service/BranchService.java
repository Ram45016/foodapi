package com.trickytechies.foodapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trickytechies.foodapi.exception.BadRequestException;
import com.trickytechies.foodapi.exception.BranchNotFoundException;
import com.trickytechies.foodapi.model.Branch;
import com.trickytechies.foodapi.repository.BranchRepository;
import com.trickytechies.foodapi.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class BranchService {

    @Autowired
    private BranchRepository branchRepository;
    @Autowired
    private UserRepository userRepository;

    // Save a branch
    public Branch saveBranch(Branch branch) {
        if (branch == null || branch.getName() == null || branch.getLocation() == null) {
            throw new BadRequestException("Branch name and location cannot be null");
        }
        branch.setAdmin(userRepository.findByUserName(branch.getAdmin().getUsername()).get());
        return branchRepository.save(branch);
    }

    // Get all branches
    public List<Branch> getAllBranches() {
        return branchRepository.findAll();
    }

    // Get a branch by name
    public Branch getBranchByName(String name) {
        Branch branch = branchRepository.findByName(name);
        if (branch == null) {
            throw new BranchNotFoundException("Branch with name " + name + " not found");
        }
        return branch;   
    }

    // Search branches with filtering (without pagination)
    public List<Branch> searchBranches(String name, String location) {
        if (name == null && location == null) {
            throw new BadRequestException("At least one search parameter (name or location) must be provided");
        }
        return branchRepository.searchBranches(name, location);
    }

    // Get a branch by ID with exception handling
    public Branch getBranchById(Long id) {
        return branchRepository.findById(id)
                .orElseThrow(() -> new BranchNotFoundException("Branch with ID " + id + " not found"));
    }

    // Update a branch
    public Branch updateBranch(Long id, Branch branch) {
        Branch existingBranch = getBranchById(id);
        existingBranch.setName(branch.getName());
        existingBranch.setLocation(branch.getLocation());
        existingBranch.setContactInfo(branch.getContactInfo());
        return branchRepository.save(existingBranch);
    }

    // Delete a branch
    public void deleteBranch(Long id) {
        if (branchRepository.existsById(id)) {
            branchRepository.deleteById(id);
        } else {
            throw new BranchNotFoundException("Branch with ID " + id + " not found");
        }
    }
}
