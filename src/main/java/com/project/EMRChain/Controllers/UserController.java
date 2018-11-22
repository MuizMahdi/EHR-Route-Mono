package com.project.EMRChain.Controllers;
import com.project.EMRChain.Entities.Auth.User;
import com.project.EMRChain.Payload.Auth.ApiResponse;
import com.project.EMRChain.Payload.Auth.UserInfo;
import com.project.EMRChain.Security.CurrentUser;
import com.project.EMRChain.Security.UserPrincipal;
import com.project.EMRChain.Services.ClustersContainer;
import com.project.EMRChain.Services.UserService;
import com.project.EMRChain.Utilities.UuidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.websocket.server.PathParam;
import java.io.IOException;


@RestController
@RequestMapping("/users")
public class UserController
{
    private UserService userService;
    private ClustersContainer clustersContainer;
    private UuidUtil uuidUtil;

    @Autowired
    public UserController(ClustersContainer clustersContainer, UserService userService, UuidUtil uuidUtil) {
        this.userService = userService;
        this.clustersContainer = clustersContainer;
        this.uuidUtil = uuidUtil;
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
