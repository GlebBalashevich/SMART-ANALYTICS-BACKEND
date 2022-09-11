package com.intexsoft.analytics.model;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Authentication {

    @Id
    private String email;

    private String password;

    private UUID departmentId;

    private Role role;

}
