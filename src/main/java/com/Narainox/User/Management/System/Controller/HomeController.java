package com.Narainox.User.Management.System.Controller;

import javax.servlet.http.HttpSession;

import com.Narainox.User.Management.System.Model.UserDtls;
import com.Narainox.User.Management.System.Repository.UserRepository;
import com.Narainox.User.Management.System.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@Controller
public class HomeController {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRepository userRepo;

    @ModelAttribute
    private void userDetails(Model m, Principal p) {
        if (p!=null)
        {
            String email=p.getName();
            UserDtls user=userRepo.findByEmail(email);
            m.addAttribute("user",user);
        }

    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/signin")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {


        return "register";
    }

    @PostMapping("/createUser")
    public String createuser(@ModelAttribute UserDtls user, HttpSession session) {

         System.out.println(user);

        boolean f = userService.checkEmail(user.getEmail());

        if (f) {
            session.setAttribute("msg", "Email Id alreday exists");
        }

        else {
            UserDtls userDtls = userService.createUser(user);
            if (userDtls != null) {
                session.setAttribute("msg", "Register Sucessfully");
            } else {
                session.setAttribute("msg", "Something wrong on server");
            }
        }

        return "redirect:/register";
    }

    @GetMapping("/loadForgotPassword")
    public String loadForgotPassword()
    {
        return "forgetPassword";
    }
    @GetMapping("/loadResetPassword/{id}")
    public String loadResetPassword(@PathVariable int id, Model m)
    {
        m.addAttribute("id",id);
        return "ResetPassword";
    }

    @PostMapping("/forgotPassword")
    public String forgotPassword(@RequestParam String email,@RequestParam String mobileNum,HttpSession httpSession)
    {
        UserDtls userDtls=userRepository.findByEmailAndMobileNumber(email, mobileNum);
        if (userDtls!=null)
        {
            return "redirect:/loadResetPassword/" + userDtls.getId();
        }else {
            httpSession.setAttribute("msg","Invalid email & Mobile Number");
            return "forgetPassword";
        }
    }

    @PostMapping("/changePassword")
    public String resetPassword(@RequestParam String psw,@RequestParam Integer id,HttpSession session)
    {
        UserDtls userDtls=userRepository.findById(id).get();
        String encryptPsw=passwordEncoder.encode(psw);
        userDtls.setPassword(encryptPsw);
        UserDtls updateUser=userRepository.save(userDtls);
        if (updateUser!=null)
        {
            session.setAttribute("msg","Password change Successfully.");
        }
        return "redirect:/loadForgotPassword";
    }
}
