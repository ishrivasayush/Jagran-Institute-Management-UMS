package com.Narainox.User.Management.System.Service;


import com.Narainox.User.Management.System.Model.UserDtls;

public interface UserService {

    public UserDtls createUser(UserDtls user);

    public boolean checkEmail(String email);

}
