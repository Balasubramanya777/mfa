package com.balu.mfa.controller;

import com.balu.mfa.dto.AuthRequest;
import com.balu.mfa.entity.User;
import com.balu.mfa.service.base.UserService;
import com.balu.mfa.service.impl.MfaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final MfaService mfaService;

    @GetMapping("/login")
    public String loginPage() {
        return "login";   // login.html
    }

    @GetMapping("/mfa")
    public String mfaPage() {
        return "mfa";   // mfa.html
    }

    @PostMapping("/mfa")
    public String verifyMfa(@RequestParam String code, Authentication authentication) {
        String username = authentication.getName();
        if (username == null) {
            return "redirect:/login";
        }

        User user = userService.findByUserName(username).orElseThrow();
        boolean isValid = mfaService.verifyCode(user.getMfaSecret(), code);

        if (!isValid) {
            return "mfa";
        }

        userService.completeLogin(username);
        return "redirect:/tada";
    }

    @GetMapping("/tada")
    public String tadaPage() {
        return "tada";   // tada.html
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("request", new AuthRequest());
        return "register"; // register.html
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("request") AuthRequest request, BindingResult result, Model model) {

        if (result.hasErrors()) {
            return "register";
        }

        String qrCode = userService.registerUser(request);

        model.addAttribute("qr", qrCode);
        return "mfa-setup";
    }
}
