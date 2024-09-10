package org.fahdpln.backend.departement;

import org.fahdpln.backend.utils.MyResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class DepartementController {

    private final DepartementService departementService;

    // Endpoint to get list of departements for select dropdown
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SECRETARY')")
    @GetMapping("/api/v1/departements/dropdown")
    public ResponseEntity<MyResponse> getListOfDepartementsForSelect() {
        MyResponse response = departementService.getListOfDepartementsForSelect();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    // Endpoint to get list of departements with pagination
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/api/v1/departements")
    public ResponseEntity<MyResponse> getListOfDepartements(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size) {
        MyResponse response = departementService.getListOfDepartements(page, size);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    // Endpoint to search departement by keyword
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/api/v1/departements/search")
    public ResponseEntity<MyResponse> searchDepartements(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size) {
        MyResponse response = departementService.searchDepartements(keyword, page, size);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    // Endpoint to get departement by id
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/api/v1/departement/{id}")
    public ResponseEntity<MyResponse> getDepartementById(@PathVariable Long id) {
        MyResponse response = departementService.getDepartementById(id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    // Endpoint to create a new departement
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/api/v1/departement")
    public ResponseEntity<MyResponse> createDepartement(@RequestBody DepartementRequest request) {
        MyResponse response = departementService.createDepartement(request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    // Endpoint to update a departement
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/api/v1/departement/{id}")
    public ResponseEntity<MyResponse> updateDepartement(
            @PathVariable Long id,
            @RequestBody DepartementRequest request) {
        MyResponse response = departementService.updateDepartement(id, request);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    // Endpoint to delete a departement
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/api/v1/departement/{id}")
    public ResponseEntity<MyResponse> deleteDepartement(@PathVariable Long id) {
        MyResponse response = departementService.deleteDepartement(id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    

}
