package com.project.EMRChain.Controllers;
import com.project.EMRChain.Entities.Auth.User;
import com.project.EMRChain.Payload.Auth.ApiResponse;
import com.project.EMRChain.Payload.UserInfo;
import com.project.EMRChain.Security.CurrentUser;
import com.project.EMRChain.Security.UserPrincipal;
import com.project.EMRChain.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController
{
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getCurrentUser(@CurrentUser UserPrincipal currentUser)
    {
        if (currentUser == null) {
            return new ResponseEntity<>(
                    new ApiResponse(false, "User not logged in"),
                    HttpStatus.BAD_REQUEST
            );
        }

        User user = userService.findUserByUsernameOrEmail(currentUser.getUsername());

        if (user == null) {
            return new ResponseEntity<>(
                new ApiResponse(false, "Invalid username"),
                HttpStatus.BAD_REQUEST
            );
        }

        return ResponseEntity.ok (
            new UserInfo (
                user.getId(),
                        user.getUsername(),
                        user.getName(),
                        user.isNonFirstLogin()
                )
        );
    }

}
