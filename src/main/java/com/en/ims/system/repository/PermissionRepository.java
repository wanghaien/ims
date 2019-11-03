package com.en.ims.system.repository;


import com.en.ims.common.repository.CommonRepository;
import com.en.ims.system.entity.SystemPermission;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface PermissionRepository extends CommonRepository<SystemPermission,Integer> {

    @Query(value = "SELECT p.* FROM system_permission p " +
            "LEFT JOIN sys_role_permission rp on rp.permission_id = p.id " +
            "left join sys_user_role ur on ur.role_id = rp.role_id " +
            "LEFT JOIN system_user u on u.id = ur.user_id " +
            "where u.id = ?1",nativeQuery = true)
    List<SystemPermission> findByUserId(Integer id);
}
