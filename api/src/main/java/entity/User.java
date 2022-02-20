package entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

//@SuppressWarnings("")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain=true)
public class User implements Serializable {
    private Long id;
    private String username;
    private String gender;
    private String phone;
    private Integer age;

    public static void main(String[] args) {
        User user = new User();
        user.setUsername("tom").setAge(1).setGender("boy");
        User u =new User(1L,"","","",1);
    }
}
