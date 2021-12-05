package com.roopy.auth.repository;

import com.roopy.auth.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {

}
