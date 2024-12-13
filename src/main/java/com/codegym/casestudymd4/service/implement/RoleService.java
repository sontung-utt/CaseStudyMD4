package com.codegym.casestudymd4.service.implement;

import com.codegym.casestudymd4.model.Role;
import com.codegym.casestudymd4.repository.IRoleRepository;
import com.codegym.casestudymd4.service.IRoleService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService implements IRoleService {
    private final IRoleRepository iRoleRepository;
    public RoleService(IRoleRepository iRoleRepository){
        this.iRoleRepository = iRoleRepository;
    }
    @Override
    public Iterable<Role> findAll() {
        return iRoleRepository.findAll();
    }

    @Override
    public Optional<Role> findById(Long id) {
        return iRoleRepository.findById(id);
    }

    @Override
    public void save(Role role) {
        iRoleRepository.save(role);
    }

    @Override
    public void delete(Long id) {
        iRoleRepository.deleteById(id);
    }

    public boolean existRoleName(String name){
        Long result = iRoleRepository.existRoleName(name);
        return result != null && result > 0;
    }

    public Optional<Role> findByName (String name){
        return iRoleRepository.findByName(name);
    }
}
