package io.github.ghacupha.fassets.repository;

import io.github.ghacupha.fassets.domain.ServiceOutlet;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ServiceOutlet entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ServiceOutletRepository extends JpaRepository<ServiceOutlet, Long>, JpaSpecificationExecutor<ServiceOutlet> {

}
