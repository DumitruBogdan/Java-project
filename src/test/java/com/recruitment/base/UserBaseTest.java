package com.recruitment.base;

import com.recruitment.domain.User;
import com.recruitment.domain.enums.Role;
import com.recruitment.dto.UserDTO;

public class UserBaseTest {

    public UserDTO dto;
    protected Long id;
    protected String email;
    protected User user;
    protected Long anotherId;
    protected User anotherUser;
    protected UserDTO anotherDTO;

    protected String token;

    public void init() {
        id = 1L;
        email = "ana.popescu@gmail.com";
        user = new User(id, Role.ADMIN,  "ana", "popescu", email,1, "password", null);
        dto = UserDTO.builder()
                .id(id)
                .roleName(Role.ADMIN)
                .departmentId(1)
                .firstName("ana")
                .lastName("popescu")
                .email(email)
                .departmentId(1)
                .password("password")
                .build();

        anotherId = 2L;
        anotherUser = new User(anotherId, Role.PTE, "ion", "mihai", "ion.mihai@gmail.com", 1, "anotherPassword", null);
        anotherDTO = UserDTO.builder()
                .roleName(Role.PTE)
                .departmentId(1)
                .firstName("ion")
                .lastName("mihai")
                .email("ion.mihai@gmail.com")
                .password("anotherPassword")
                .build();

        token = "token";
    }

    protected void makeIdsTheSame() {
        anotherUser.setId(user.getId());
        anotherDTO.setId(dto.getId());
    }
}
