package com.example.authservice.infraestructure.rest.mapper;

import com.example.authservice.domain.model.User;
import com.example.authservice.infraestructure.entity.UserEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "email", target = "email"),
            @Mapping(source = "password", target = "password"),
            @Mapping(source = "name", target = "name"),
            @Mapping(source = "role", target = "role")
    })
    User toUser(UserEntity userEntity);

    Iterable<User> toUsers(Iterable<UserEntity> userEntities);

    @InheritInverseConfiguration
    UserEntity toUserEntity(User user);
}
