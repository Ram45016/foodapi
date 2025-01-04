package com.trickytechies.foodapi.repository;

import com.trickytechies.foodapi.model.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {

    @Query("SELECT m FROM MenuItem m WHERE m.id = :id AND m.branch.name = :branchName")
    Optional<MenuItem> findByIdAndBranchName(@Param("id") Long id, @Param("branchName") String branchName);

    @Query("SELECT m FROM MenuItem m WHERE m.branch.name = :branchName")
    List<MenuItem> findByBranchName(@Param("branchName") String branchName);
}
