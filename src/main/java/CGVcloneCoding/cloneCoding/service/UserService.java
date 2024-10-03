package CGVcloneCoding.cloneCoding.service;

import CGVcloneCoding.cloneCoding.domain.User;
import CGVcloneCoding.cloneCoding.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final JWTUtility jwtUtility;

    public User tokenToUser(String token) {
        return userRepository.findByUserId(jwtUtility.validateToken(token).getSubject());
    }

    @Transactional
    public User signUp(String userId, String password){
        User user = userRepository.findByUserId(userId);
        //이미 멤버가 있다는 것
        if(user != null) return null;
        //없으면 회원가입 저장
        return  userRepository.save(new User(userId, password));
    }

    public String login(String userId, String passwd){
        User user = userRepository.findByUserId(userId);
        if(user != null && user.checkPassword(passwd)){
            return jwtUtility.generateJwtToken(user.getUserId());
        }
        return "로그인 실패";
    }
}
