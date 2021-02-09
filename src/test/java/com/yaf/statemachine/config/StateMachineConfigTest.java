package com.yaf.statemachine.config;

import com.yaf.statemachine.domain.LightGroupEvent;
import com.yaf.statemachine.domain.LightGroupState;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;

import java.util.UUID;

@SpringBootTest
class StateMachineConfigTest {

    @Autowired
    StateMachineFactory<LightGroupState, LightGroupEvent> factory;

    @Test
    void testStateMachine() {

        StateMachine<LightGroupState, LightGroupEvent> sm = factory.getStateMachine(UUID.randomUUID());

        sm.start();
        System.err.println(sm.getState().toString());
        sm.sendEvent(LightGroupEvent.TURN_ALL_LIGHTS_ON);
        System.err.println(sm.getState().toString());
        sm.sendEvent(LightGroupEvent.NOTIFICATION_RECEIVED);
        System.err.println(sm.getState().toString());

    }

}