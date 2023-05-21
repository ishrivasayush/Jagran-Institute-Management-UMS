package com.Narainox.User.Management.System.Controller;

import java.security.Principal;

import com.Narainox.User.Management.System.Model.UserDtls;
import com.Narainox.User.Management.System.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;


@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @ModelAttribute
    private void userDetails(Model m, Principal p) {
        String email = p.getName();
        UserDtls user = userRepo.findByEmail(email);

        m.addAttribute("user", user);

    }

    @GetMapping("/")
    public String home() {
        return "user/home";
    }
    @GetMapping("/profilePage")
    public String profilePage() {
        return "user/profile";
    }
    @GetMapping("/changePass")
    public String loadChangePassword() {
        return "user/change_password";
    }

    @PostMapping("/updatePassword")
    public String changePassword(Principal p, @RequestParam("oldPass")String oldPass, @RequestParam("newPass")String newPass, HttpSession session)
    {
        String email=p.getName();
        UserDtls loginUser=userRepo.findByEmail(email);
        boolean f=passwordEncoder.matches(oldPass,loginUser.getPassword());
        if (f) {
            loginUser.setPassword(passwordEncoder.encode(newPass));
            UserDtls updatePasswordUser=userRepo.save(loginUser);
            if (updatePasswordUser!=null)
            {
                session.setAttribute("msg","Password change Success");
            }
            else {
                session.setAttribute("msg","Something wrong on Server");
            }
        }
        else {
            session.setAttribute("msg","old password Wrong");
        }
        return "redirect:/user/changePass";
    }


}
