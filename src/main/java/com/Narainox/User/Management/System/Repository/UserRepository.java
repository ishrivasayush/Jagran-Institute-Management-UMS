package com.Narainox.User.Management.System.Repository;

import com.Narainox.User.Management.System.Model.UserDtls;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<UserDtls, Integer> {

    public boolean existsByEmail(String email);

    public UserDtls findByEmail(String email);

    public UserDtls findByEmailAndMobileNumber(String em,String mobno);

}