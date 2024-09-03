package org.fahdpln.backend.departement;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DepartementRepository extends JpaRepository<Departement,Long> {

    @Query("SELECT d FROM Departement d WHERE d.name LIKE %?1%")
    Page<Departement> searchDepatement(String keyword, Pageable pageable);

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);
    
}
