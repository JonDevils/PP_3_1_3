package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @PersistenceContext
    private EntityManager em;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userNameLogin = userRepository.findByUserNameAndFetchRoles(username);
        if (userNameLogin == null) {
            throw new UsernameNotFoundException("User not found");
        }
            return userNameLogin;
    }

    public User findOne(Long userId) {
        Optional<User> userFromDb = userRepository.findById(userId);
        return userFromDb.orElse(new User());
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }
    public List<Role> listRoles() {
        return roleRepository.findAll();
    }
    @Transactional
    public boolean saveUser(User user) {
        User userFromDB = userRepository.findByUserNameAndFetchRoles(user.getUsername());
        if (userFromDB != null) {
            return false;
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(user.getRoles());
        userRepository.save(user);
        return true;
    }

    @Transactional
    public void update(Long id, User updateUser) {
        User updateNewUser = userRepository.findById(id).get();

        Set<Role> roles = updateNewUser.getRoles();
        roles.clear();
        for (Role x : updateUser.getRoles()) {
            long z = x.getId();
            roles.add(roleRepository.getById(z));
        }
        updateNewUser.setRoles(roles);


        updateNewUser.setFirstName(updateUser.getFirstName());
        updateNewUser.setLastName(updateUser.getLastName());
        updateNewUser.setAge(updateUser.getAge());
        updateNewUser.setEmail(updateUser.getEmail());
        updateNewUser.setUsername(updateUser.getUsername());

        updateNewUser.setPassword(passwordEncoder.encode(updateUser.getPassword()));

        userRepository.save(updateNewUser);
    }

    @Transactional
    public boolean delete(Long userId) {
        if (userRepository.findById(userId).isPresent()) {
            userRepository.deleteById(userId);
            return true;
        }
        return false;
    }


}







