package com.example.authservice.infraestructure.rest.mapper;

import com.example.authservice.domain.model.User;
import com.example.authservice.infraestructure.entity.UserEntity;
import java.util.ArrayList;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-09-17T23:38:23+0200",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.9 (GraalVM Community)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User toUser(UserEntity userEntity) {
        if ( userEntity == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.id( userEntity.getId() );
        user.email( userEntity.getEmail() );
        user.password( userEntity.getPassword() );
        user.name( userEntity.getName() );
        user.role( userEntity.getRole() );

        return user.build();
    }

    @Override
    public Iterable<User> toUsers(Iterable<UserEntity> userEntities) {
        if ( userEntities == null ) {
            return null;
        }

        ArrayList<User> iterable = new ArrayList<User>();
        for ( UserEntity userEntity : userEntities ) {
            iterable.add( toUser( userEntity ) );
        }

        return iterable;
    }

    @Override
    public UserEntity toUserEntity(User user) {
        if ( user == null ) {
            return null;
        }

        UserEntity.UserEntityBuilder userEntity = UserEntity.builder();

        userEntity.id( user.getId() );
        userEntity.email( user.getEmail() );
        userEntity.password( user.getPassword() );
        userEntity.name( user.getName() );
        userEntity.role( user.getRole() );

        return userEntity.build();
    }
}
