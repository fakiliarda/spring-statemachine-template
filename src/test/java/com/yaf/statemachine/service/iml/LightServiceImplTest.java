package com.yaf.statemachine.service.iml;

import com.yaf.statemachine.statemachine.LivingRoomStateMachine;
import com.yaf.statemachine.repository.LivingRoomStateMachineRepository;
import com.yaf.statemachine.service.LightService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

@SpringBootTest
class LightServiceImplTest {

    @Autowired
    LightService lightService;

    @Autowired
    LivingRoomStateMachineRepository livingRoomStateMachineRepository;

    LivingRoomStateMachine livingRoomStateMachine;

    @BeforeEach
    void setUp() {
        livingRoomStateMachine = livingRoomStateMachine.builder().name("action").build();
    }

    @Transactional
    @Test
    void turnOnLights() {

        var savedLivingRoomStateMachine = lightService.createLivingRoomStateMachine(livingRoomStateMachine);
        var sm = lightService.turnOnLights(savedLivingRoomStateMachine.getId());
        var turnOn = livingRoomStateMachineRepository.getOne(savedLivingRoomStateMachine.getId());

        System.err.println(sm.getState().getId());
        System.err.println(turnOn);
    }
}