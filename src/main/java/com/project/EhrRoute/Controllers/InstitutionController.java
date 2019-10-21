package com.project.EhrRoute.Controllers;
import com.project.EhrRoute.Payload.App.ProviderAdditionRequest;
import com.project.EhrRoute.Payload.Auth.ApiResponse;
import com.project.EhrRoute.Security.CurrentUser;
import com.project.EhrRoute.Security.UserPrincipal;
import com.project.EhrRoute.Services.InstitutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;


@Controller
@RequestMapping("institution")
public class InstitutionController
{
    private InstitutionService institutionService;

    @Autowired
    public InstitutionController(InstitutionService institutionService) {
        this.institutionService = institutionService;
    }

    @PostMapping("/current/provider")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity addInstitutionProvider(@Valid @RequestBody ProviderAdditionRequest providerAdditionRequest, @CurrentUser UserPrincipal currentUser) {
        institutionService.addInstitutionProvider(providerAdditionRequest.getAddress(), currentUser.getId());
        return ResponseEntity.ok(new ApiResponse(true, "A provider was successfully added to your institution"));
    }
}
