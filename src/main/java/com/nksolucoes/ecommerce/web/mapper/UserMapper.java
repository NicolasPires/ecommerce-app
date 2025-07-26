package com.nksolucoes.ecommerce.web.mapper;

import com.nksolucoes.ecommerce.domain.User;
import com.nksolucoes.ecommerce.domain.enumerations.RoleEnum;
import com.nksolucoes.ecommerce.web.dto.request.RegisterRequestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "role", source = "role", qualifiedByName = "mapRole")
    User fromRegisterRequest(RegisterRequestDTO dto);

    @Named("mapRole")
    default RoleEnum mapRole(String role) {
        return RoleEnum.valueOf(role.toUpperCase());
    }
}
