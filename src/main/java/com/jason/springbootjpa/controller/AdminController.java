package com.jason.springbootjpa.controller;

import com.jason.springbootjpa.controller.response.model.Response;
import com.jason.springbootjpa.entity.AdminEntity;
import com.jason.springbootjpa.entity.AdminRole;
import com.jason.springbootjpa.repository.AdminRepository;
import com.jason.springbootjpa.repository.AdminRoleRepository;
import com.jason.springbootjpa.controller.request.model.AdminRegisterRequest;
import com.jason.springbootjpa.controller.request.model.AuthenticationRequest;
import com.jason.springbootjpa.security.JWTTokenProvider;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@NoArgsConstructor
@RequestMapping("/admin")
public class AdminController {

   @Autowired
   private AdminRoleRepository adminRoleRepository;

   @Autowired
   private AdminRepository adminRepository;

   @Autowired
   AuthenticationManager authenticationManager;

   @Autowired
   JWTTokenProvider jwtTokenProvider;

   @Autowired
   private UserDetailsService userDetailService;

   @PostMapping("/register")
   public ResponseEntity<Object> signUp(@RequestBody AdminRegisterRequest user) throws Exception {
      Optional<AdminRole> opRole = adminRoleRepository.findById(user.getRoleId());
      if(opRole.isEmpty()) {
         throw new Exception("Role does not exist.");
      } else {
         AdminRole role = opRole.get();
         AdminEntity admin = new AdminEntity();
         BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
         admin.setUsername(user.getUsername());
         admin.setEmail(user.getEmail());
         admin.setRole(role);
         admin.setPassword(encoder.encode(user.getPassword()));
         admin.setPhone(user.getPhone());
         admin.setDefaultData();
         adminRepository.save(admin);
         return ResponseEntity.ok(new Response("Admin account successfully registered."));
      }
   }

   @PostMapping("/login")
   public ResponseEntity<Object> logIn(@RequestBody AuthenticationRequest request) {
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken("admin:" + request.getUsername(), request.getPassword()));
      String token = jwtTokenProvider.createToken("admin:" + request.getUsername(), "ADMIN");
      return ResponseEntity.ok(new Response(token));
   }

}
