package com.cg.service.user;

import com.cg.domain.dto.user.UserInfoDTO;
import com.cg.domain.entity.User;
import com.cg.domain.entity.UserPrinciple;
import com.cg.domain.entity.staff.Staff;
import com.cg.repository.UserRepository;
import com.cg.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AppUtils appUtils;


    @Override
    public UserInfoDTO getInfo(Model model) {
        String username = appUtils.getPrincipalUsername();
        UserInfoDTO userInfoDTO = getInfoByUsername(username);
        String fullName = userInfoDTO.getFullName();
        String roleCode = userInfoDTO.getRoleCode();
        model.addAttribute("fullName", fullName);
        model.addAttribute("username", username);
        model.addAttribute("ROLE", roleCode);

        return userInfoDTO;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public UserInfoDTO getInfoByUsername(String username) {
        return userRepository.getInfoByUsername(username);
    }

    @Override
    public User getById(String id) {
        return null;
    }

    @Override
    public Optional<User> findById(String id) {
        return userRepository.findById(id);
    }

    @Override
    public Boolean existsUserByUsernameAndIdNot(String username, String id) {
        return userRepository.existsUserByUsernameAndIdNot(username, id);
    }

    @Override
    public Boolean existsStaffByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User getByUsername(String username) {
        return userRepository.getByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (!userOptional.isPresent()) {
            throw new UsernameNotFoundException(username);
        }

        UserInfoDTO userInfoDTO = getInfoByUsername(username);
        String fullName = userInfoDTO.getFullName();

        return UserPrinciple.build(userOptional.get(), fullName);
    }

    @Override
    public void delete(User user) {

    }

    @Override
    public void deleteById(String id) {

    }
    @Override
    public void softDelete(String userId){
        userRepository.softDelete(userId);
    }
}
