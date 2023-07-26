package com.cg.api;

import com.cg.domain.dto.jwt.JwtResponseStaff;
import com.cg.domain.dto.user.UserCreateReqDTO;
import com.cg.domain.dto.user.UserLoginReqDTO;
import com.cg.domain.entity.*;
import com.cg.domain.entity.staff.Staff;
import com.cg.domain.enums.ERole;
import com.cg.exception.DataInputException;
import com.cg.exception.EmailExistsException;
import com.cg.exception.UnauthorizedException;
import com.cg.service.jwt.JwtService;
import com.cg.service.role.IRoleService;
import com.cg.service.staff.IStaffService;
import com.cg.service.user.IUserService;
import com.cg.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;


@RestController
@RequestMapping("/api/auth")
public class AuthAPI {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AppUtils appUtils;

    @Autowired
    private IUserService userService;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IStaffService staffService;



    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginReqDTO userLoginReqDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userLoginReqDTO.getUsername(), userLoginReqDTO.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtService.generateTokenLogin(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        User currentUser = userService.getByUsername(userLoginReqDTO.getUsername());

        if(currentUser.getDeleted()) {
            throw new UnauthorizedException("Tài khoản của bạn đã bị khóa!");
        }

        Staff staff = staffService.findByUser(currentUser).orElseThrow(() -> {
            throw new UnauthorizedException("Tài khoản chưa xác thực");
        });

        Role role = currentUser.getRole();
        ERole eRole = role.getName();

        if (eRole == ERole.ROLE_CUSTOMER) {
            throw new UnauthorizedException("Bạn không đủ quyền để thực hiện xác thực đăng nhập");
        }

        try {
            JwtResponseStaff jwtResponseStaff = new JwtResponseStaff(
                    jwt,
                    currentUser.getId(),
                    userDetails.getUsername(),
                    staff.getFullName(),
                    userDetails.getAuthorities()
            );

            ResponseCookie springCookie = ResponseCookie.from("JWT", jwt)
                    .httpOnly(false)
                    .secure(false)
                    .path("/")
                    .maxAge(appUtils.TOKEN_MAX_AGE)
                    .domain(appUtils.DOMAIN_SERVER)
                    .build();

            return ResponseEntity
                    .ok()
                    .header(HttpHeaders.SET_COOKIE, springCookie.toString())
                    .body(jwtResponseStaff);

        } catch (Exception e) {
            e.printStackTrace();
            DataInputException dataInputException = new DataInputException("Dữ liệu không đúng! Vui lòng kiểm tra lại!");
            return new ResponseEntity<>(dataInputException, HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserCreateReqDTO userDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }

        Boolean existsByUsername = userService.existsStaffByUsername(userDTO.getUsername());

        if (existsByUsername) {
            throw new EmailExistsException("Account already exists");
        }

        Optional<Role> optRole = roleService.findById(userDTO.getRole().getId());

        if (!optRole.isPresent()) {
            throw new DataInputException("Invalid account role");
        }

        try {
            userService.save(userDTO.toUser());

            return new ResponseEntity<>(HttpStatus.CREATED);

        } catch (DataIntegrityViolationException e) {
            throw new DataInputException("Account information is not valid, please check the information again");
        }
    }
}

