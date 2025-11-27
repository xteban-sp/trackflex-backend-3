package com.rutaflex.trackflexv3.repository.security;

import com.rutaflex.trackflexv3.entity.security.GrantedPermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GrantedPermissionRepository extends JpaRepository<GrantedPermission, Long> {

    List<GrantedPermission> findByRolId(Long rolId);
}