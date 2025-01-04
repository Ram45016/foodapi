package com.trickytechies.foodapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.trickytechies.foodapi.model.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface BranchRepository extends JpaRepository<Branch, Long> {

    // Custom search query without pagination, filtering by name or location
    List<Branch> findByNameContainingOrLocationContaining(String name, String location);

    // Custom query for advanced search (without pagination)
    @Query("SELECT b FROM Branch b WHERE " +
            "(:name IS NULL OR b.name LIKE %:name%) AND " +
            "(:location IS NULL OR b.location LIKE %:location%)")
    List<Branch> searchBranches(String name, String location);

    Branch findByName(String name);
}
