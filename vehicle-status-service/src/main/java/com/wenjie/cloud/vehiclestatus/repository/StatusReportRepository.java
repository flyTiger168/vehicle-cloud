package com.wenjie.cloud.vehiclestatus.repository;

import com.wenjie.cloud.vehiclestatus.entity.StatusReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * 状态上报数据访问层
 */
public interface StatusReportRepository extends JpaRepository<StatusReport, Long> {

    /**
     * 按 VIN 和时间范围分页查询
     */
    Page<StatusReport> findByVinAndReportTimeBetween(String vin, Instant start, Instant end, Pageable pageable);

    /**
     * 查询某辆车最新一条状态
     */
    Optional<StatusReport> findFirstByVinOrderByReportTimeDesc(String vin);

    /**
     * 查询所有车辆各自最新一条状态
     */
    @Query("SELECT sr FROM StatusReport sr "
         + "WHERE sr.reportTime = ("
         + "  SELECT MAX(sr2.reportTime) "
         + "  FROM StatusReport sr2 "
         + "  WHERE sr2.vin = sr.vin"
         + ")")
    List<StatusReport> findLatestForAllVehicles();
}
