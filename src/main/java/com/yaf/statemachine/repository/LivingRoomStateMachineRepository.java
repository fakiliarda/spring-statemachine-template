package com.yaf.statemachine.repository;

import com.yaf.statemachine.statemachine.LivingRoomStateMachine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LivingRoomStateMachineRepository extends JpaRepository<LivingRoomStateMachine, Long> {
}
