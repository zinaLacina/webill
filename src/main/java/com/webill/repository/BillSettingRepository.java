package com.webill.repository;

import com.webill.domain.BillSetting;
import com.webill.domain.enumeration.TypeBill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * Spring Data  repository for the BillSetting entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BillSettingRepository extends JpaRepository<BillSetting, Long> {

    Page<BillSetting> findAllByEnabledTrue(Pageable pageable);

    List<BillSetting> findAllByTypeBillEquals(TypeBill typeBill);

    //Optional<BillSetting> findFirstBy

}
