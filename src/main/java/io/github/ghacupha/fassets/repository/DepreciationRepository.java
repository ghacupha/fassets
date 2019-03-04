package io.github.ghacupha.fassets.repository;

import io.github.ghacupha.fassets.domain.Depreciation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Depreciation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DepreciationRepository extends JpaRepository<Depreciation, Long>, JpaSpecificationExecutor<Depreciation> {

}
