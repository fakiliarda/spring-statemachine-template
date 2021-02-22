package com.yaf.statemachine.service;


import com.yaf.statemachine.domain.LightGroupEvent;
import com.yaf.statemachine.domain.LightGroupState;
import com.yaf.statemachine.statemachine.LivingRoomStateMachine;
import org.springframework.statemachine.StateMachine;

public interface LightService {

    LivingRoomStateMachine createLivingRoomStateMachine(LivingRoomStateMachine livingRoomStateMachine);

    StateMachine<LightGroupState, LightGroupEvent> turnOnLights(Long lightGroupId);

    StateMachine<LightGroupState, LightGroupEvent> turnOffLights(Long lightGroupId);

    StateMachine<LightGroupState, LightGroupEvent> turnOnOneLight(Long lightGroupId);

    StateMachine<LightGroupState, LightGroupEvent> notificationReceived(Long lightGroupId);

    StateMachine<LightGroupState, LightGroupEvent> remind(Long lightGroupId);

}
