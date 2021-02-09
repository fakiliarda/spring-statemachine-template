package com.yaf.statemachine.service;


import com.yaf.statemachine.domain.LightGroupAction;
import com.yaf.statemachine.domain.LightGroupEvent;
import com.yaf.statemachine.domain.LightGroupState;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Service;

@Service
public interface LightService {

    LightGroupAction newLightGroupAction(LightGroupAction lightGroup);

    StateMachine<LightGroupState, LightGroupEvent> turnOnLights(Long lightGroupId);

    StateMachine<LightGroupState, LightGroupEvent> turnOffLights(Long lightGroupId);

    StateMachine<LightGroupState, LightGroupEvent> turnOnOneLight(Long lightGroupId);

    StateMachine<LightGroupState, LightGroupEvent> notificationReceived(Long lightGroupId);

    StateMachine<LightGroupState, LightGroupEvent> remind(Long lightGroupId);

}
