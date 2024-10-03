package CGVcloneCoding.cloneCoding.controller;

import CGVcloneCoding.cloneCoding.DTO.UserDTO;
import CGVcloneCoding.cloneCoding.domain.User;
import CGVcloneCoding.cloneCoding.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    public final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody UserDTO.UserCreateRequest request) {
        User user = userService.signUp(request.getUserId(),request.getPassword());
        if(user == null) {return ResponseEntity.status(HttpStatus.FORBIDDEN).body("이미 존재하는 아이디 입니다");}
        String token= userService.login(request.getUserId(),request.getPassword());
        return ResponseEntity.status(HttpStatus.CREATED).body(token);
    }

    @PostMapping("/login")
    public String login(@RequestBody UserDTO.UserLoginRequest request){
        return userService.login(request.getUserId(),request.getPassword());
    }
}
