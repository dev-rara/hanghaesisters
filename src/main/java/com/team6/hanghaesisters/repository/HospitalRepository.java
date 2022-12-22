package com.team6.hanghaesisters.repository;

import com.team6.hanghaesisters.entity.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HospitalRepository extends JpaRepository<Hospital, Long> {
    @Query("select h.hospitalName from Hospital h where h.hospitalName like concat('%', :hospitalName, '%')")
    List<String> findByHospitalName(@Param("hospitalName") String hospitalName);
}