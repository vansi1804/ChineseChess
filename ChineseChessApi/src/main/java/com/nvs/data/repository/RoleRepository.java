package com.nvs.data.repository;

import com.nvs.data.entity.Role;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer>{

   boolean existsByName(String name);

   Optional<Role> findByName(String name);

}
