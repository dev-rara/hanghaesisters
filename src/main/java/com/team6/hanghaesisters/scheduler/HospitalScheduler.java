package com.team6.hanghaesisters.scheduler;

import com.team6.hanghaesisters.service.HospitalService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

//@Component
////@EnableScheduling
//@RequiredArgsConstructor
//public class HospitalScheduler {
//
//	private final HospitalService hospitalService;
//
//	@Transactional
////	@Scheduled(cron = "0 0 1 * * *")
//	public void hospitalDataScheduling() {
//		hospitalService.saveHospitalApiData();
//	}
//}
