package com.project.EMRChain.Controllers;
import com.project.EMRChain.Entities.Auth.User;
import com.project.EMRChain.Payload.UserInfo;
import com.project.EMRChain.Security.CurrentUser;
import com.project.EMRChain.Security.UserPrincipal;
import com.project.EMRChain.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController
{
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public UserInfo getCurrentUser(@CurrentUser UserPrincipal currentUser)
    {
        User user = userService.findUserByUsernameOrEmail(currentUser.getUsername());
        return new UserInfo(user.getId(), user.getUsername(), user.getName(), user.isFirstLogin());
    }

}
