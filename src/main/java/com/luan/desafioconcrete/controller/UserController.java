package com.luan.desafioconcrete.controller;

import com.luan.desafioconcrete.model.ResponseMessage;
import com.luan.desafioconcrete.utils.ConversionUtil;
import com.luan.desafioconcrete.utils.JwtTokenUtil;
import com.luan.desafioconcrete.domain.User;
import com.luan.desafioconcrete.dto.UserDTO;
import com.luan.desafioconcrete.service.JwtUserDetailsService;
import com.luan.desafioconcrete.service.UserService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;


    @PostMapping("/create")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "User created"),
        @ApiResponse(code = 400, message = "Email already exists")
    })
    public ResponseEntity<?> cadastro(@RequestBody User user){

        if(userService.existsByEmail(user.getEmail())){
            return ResponseEntity.badRequest().body(new ResponseMessage("Email already exists"));
        }else{
            user.changePassword();
            user.create();
            user.setToken(jwtTokenUtil.generateToken(user));
            user = userService.save(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(new UserDTO(user));
        }
    }


    @PostMapping("/login")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Login effected"),
            @ApiResponse(code = 400, message = "Email or password incorrect"),
            @ApiResponse(code = 400, message = "User don't exists")
    })
    public ResponseEntity<?> login(@RequestBody UserDTO userDTO) {

        try {
            authenticate(userDTO.getEmail(), userDTO.getPassword());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new ResponseMessage("Email e/ou senha inválidos"));
        }

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(userDTO.getEmail());

        User user = userService.findByEmail(userDetails.getUsername());

        if(user == null){
            return ResponseEntity.badRequest().body(new ResponseMessage("Usuário inexistente"));
        }else if(!ConversionUtil.matches(userDTO.getPassword(), user.getPassword())){
            return ResponseEntity.badRequest().body(new ResponseMessage("Email e/ou senha inválidos"));
        }else{
            final String token = jwtTokenUtil.generateToken(user);
            user.setToken(token);
            user.login();
            user = userService.save(user);
            return ResponseEntity.status(HttpStatus.OK).body(new UserDTO(user));
        }
    }

    @GetMapping("/{uuid}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User found"),
            @ApiResponse(code = 404, message = "User not found"),
            @ApiResponse(code = 401, message = "Session invalid"),
            @ApiResponse(code = 401, message = "UNAUTHORIZED")
    })
    public ResponseEntity<?> perfil(@PathVariable("uuid") String uuid, HttpServletRequest request){

        String token = request.getHeader("Authorization");
        User user = userService.findById(UUID.fromString(uuid));
        token = token.replace("Bearer ","");

        if(user == null){
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage("Usuario não encontrado"));
       } else if(!user.getToken().equals(token)){
           return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseMessage("Não autorizado"));
       }else if(!user.isSessionValid()){
           return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseMessage("Sessão Invalida"));
       }else{
            return ResponseEntity.ok(new UserDTO(user));
       }
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

}
