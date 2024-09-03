package org.fahdpln.backend.departement;

import java.util.List;
import java.util.Map;

import org.fahdpln.backend.exception.MyAlreadyExistException;
import org.fahdpln.backend.exception.MyNotAcceptableException;
import org.fahdpln.backend.exception.MyNotFoundException;
import org.fahdpln.backend.utils.MyError;
import org.fahdpln.backend.utils.MyErrorResponse;
import org.fahdpln.backend.utils.MyResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DepartementService {

        private final DepartementRepository departementRepository;

        // Get list of departements for select dropdown
        public MyResponse getListOfDepartementsForSelect() {
                List<Departement> departements = departementRepository.findAll();
                List<DepartementDTO> departementsDTO = departements.stream()
                                .map(this::buildDepartementDTO)
                                .toList();
                return MyResponse.builder()
                                .status(HttpStatus.OK)
                                .data(departementsDTO)
                                .build();
        }

        // Delete departement
        public MyResponse deleteDepartement(Long id) throws MyNotFoundException, MyNotAcceptableException {
                Departement departement = this.findDepartementById(id);
                if (departement.getEmployees().size() > 0) {
                        throw new MyNotAcceptableException(MyErrorResponse.builder()
                                        .message("Can't delete departement with employees,Delete the employees first")
                                        .build());
                } else {
                        departementRepository.delete(departement);
                        return MyResponse.builder()
                                        .status(HttpStatus.OK)
                                        .message("Departement deleted successfully")
                                        .build();
                }
        }

        // Update departement
        public MyResponse updateDepartement(Long id, DepartementRequest request)
                        throws MyNotFoundException, MyAlreadyExistException {
                this.isDepartementNameTakenExceptCurrent(request.getName(), id);
                Departement departement = this.findDepartementById(id);
                departement.setName(request.getName());
                departementRepository.save(departement);
                return MyResponse.builder()
                                .status(HttpStatus.OK)
                                .message("Departement updated successfully")
                                .build();
        }

        // Create a new departement
        public MyResponse createDepartement(DepartementRequest request) throws MyAlreadyExistException {
                this.isDepartementNameTaken(request.getName());
                Departement departement = Departement.builder()
                                .name(request.getName())
                                .build();
                departementRepository.save(departement);
                return MyResponse.builder()
                                .status(HttpStatus.CREATED)
                                .message("Departement created successfully")
                                .build();
        }

        // Get Departement by ID
        public MyResponse getDepartementById(Long id) throws MyNotFoundException {
                Departement departement = this.findDepartementById(id);
                DepartementDTO departementDTO = buildDepartementDTO(departement);
                return MyResponse.builder()
                                .status(HttpStatus.OK)
                                .data(departementDTO)
                                .build();
        }

        // Search departement by keyword
        public MyResponse searchDepartements(String keyword, int page, int size) {
                // Get page of departements
                Pageable pageable = PageRequest.of(page - 1, size, Sort.by("id").descending());
                Page<Departement> departementsPage = departementRepository.searchDepatement(keyword, pageable);

                if (departementsPage.isEmpty()) {
                        return MyResponse.builder()
                                        .status(HttpStatus.OK)
                                        .message("No departement found")
                                        .build();
                } else {
                        // Build list of departement DTO
                        List<DepartementDTO> departementsDTO = departementsPage.stream()
                                        .map(this::buildDepartementDTO)
                                        .toList();
                        // Meta data
                        Map<String, Object> meta = Map.of(
                                        "totalPages", departementsPage.getTotalPages(),
                                        "totalElements", departementsPage.getTotalElements(),
                                        "currentPage", departementsPage.getNumber() + 1,
                                        "size", size);
                        // Return Response
                        return MyResponse.builder()
                                        .status(HttpStatus.OK)
                                        .data(departementsDTO)
                                        .meta(meta)
                                        .build();
                }
        }

        // Get list of departement with pagination
        public MyResponse getListOfDepartements(int page, int size) {
                // Get page of departements
                Pageable pageable = PageRequest.of(page - 1, size, Sort.by("id").descending());
                Page<Departement> departementsPage = departementRepository.findAll(pageable);

                if (departementsPage.getContent().isEmpty()) {
                        return MyResponse.builder()
                                        .status(HttpStatus.OK)
                                        .message("No departement found")
                                        .build();
                } else {
                        // Build list of departement DTO
                        List<DepartementDTO> departementsDTO = departementsPage.map(this::buildDepartementDTO)
                                        .getContent();
                        // Meta data
                        Map<String, Object> meta = Map.of(
                                        "totalPages", departementsPage.getTotalPages(),
                                        "totalElements", departementsPage.getTotalElements(),
                                        "currentPage", departementsPage.getNumber() + 1,
                                        "size", size);
                        // Return Response
                        return MyResponse.builder()
                                        .status(HttpStatus.OK)
                                        .meta(meta)
                                        .data(departementsDTO)
                                        .build();

                }

        }

        // Util to build a Departement DTO
        private DepartementDTO buildDepartementDTO(Departement departement) {
                return DepartementDTO.builder()
                                .id(departement.getId())
                                .name(departement.getName())
                                .createdAt(departement.getCreatedAt())
                                .updatedAt(departement.getUpdatedAt())
                                .build();
        }

        // Util to find a departement by id
        public Departement findDepartementById(Long id) throws MyNotFoundException {
                return departementRepository.findById(id).orElseThrow(
                                () -> new MyNotFoundException("Departement not found with ID: " + id));
        }

        // Utils to check if the departement name is already taken
        private void isDepartementNameTaken(String name) throws MyAlreadyExistException {
                if (departementRepository.existsByName(name)) {
                        throw new MyAlreadyExistException(MyErrorResponse.builder()
                                        .errors(List.of(MyError.builder()
                                                        .key("name")
                                                        .message("Departement name with is already taken")
                                                        .build()))
                                        .build());

                }
        }

        // Utils to check if the departement name is already taken except for the
        // current departement
        private void isDepartementNameTakenExceptCurrent(String name, Long id) throws MyAlreadyExistException {
                if (departementRepository.existsByNameAndIdNot(name, id)) {
                        throw new MyAlreadyExistException(MyErrorResponse.builder()
                                        .errors(List.of(MyError.builder()
                                                        .key("name")
                                                        .message("Departement name with is already taken")
                                                        .build()))
                                        .build());

                }
        }

}
