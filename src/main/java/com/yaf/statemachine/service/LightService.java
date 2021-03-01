package com.yaf.statemachine.service;


import com.yaf.statemachine.domain.LightGroupEvent;
import com.yaf.statemachine.domain.LightGroupState;
import com.yaf.statemachine.statemachine.LivingRoomStateMachine;
import org.springframework.statemachine.StateMachine;

public interface LightService {

    LivingRoomStateMachine createLivingRoomStateMachine(LivingRoomStateMachine livingRoomStateMachine);

    StateMachine<LightGroupState, LightGroupEvent> execute(Long livingRoomStateMachineId, LightGroupEvent event);

//    StateMachine<LightGroupState, LightGroupEvent> turnOnLights(Long livingRoomStateMachineId);
//
//    StateMachine<LightGroupState, LightGroupEvent> turnOffLights(Long livingRoomStateMachineId);
//
//    StateMachine<LightGroupState, LightGroupEvent> turnOnOneLight(Long livingRoomStateMachineId);
//
//    StateMachine<LightGroupState, LightGroupEvent> notificationReceived(Long livingRoomStateMachineId);
//
//    StateMachine<LightGroupState, LightGroupEvent> remind(Long livingRoomStateMachineId);

}
