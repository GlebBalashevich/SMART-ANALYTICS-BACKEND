package com.intexsoft.analytics.mapper;

import com.intexsoft.analytics.dto.authentication.AuthenticationDto;
import com.intexsoft.analytics.model.Authentication;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthenticationMapper {

    AuthenticationDto toAuthenticationDto(Authentication user);

}
