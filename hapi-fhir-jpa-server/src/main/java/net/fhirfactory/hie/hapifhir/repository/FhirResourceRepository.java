package net.fhirfactory.hie.hapifhir.repository;

import net.fhirfactory.hie.hapifhir.model.FhirResourceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FhirResourceRepository extends JpaRepository<FhirResourceEntity, Long> {

    Optional<FhirResourceEntity> findByResourceTypeAndFhirId(String resourceType, String fhirId);

    Optional<FhirResourceEntity> findByResourceTypeAndFhirIdAndDeletedFalse(String resourceType, String fhirId);

    List<FhirResourceEntity> findByResourceTypeAndDeletedFalse(String resourceType);

    List<FhirResourceEntity> findByResourceType(String resourceType);

    boolean existsByResourceTypeAndFhirIdAndDeletedFalse(String resourceType, String fhirId);

    long countByResourceTypeAndDeletedFalse(String resourceType);
}
