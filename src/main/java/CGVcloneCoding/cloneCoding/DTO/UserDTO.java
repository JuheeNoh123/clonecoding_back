package CGVcloneCoding.cloneCoding.DTO;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

public class UserDTO {
    @Data
    public static class UserCreateRequest {
        public String userId;
        public String password;
    }

    @Data
    public static class UserLoginRequest{
        private String userId;
        private String password;
    }
}
