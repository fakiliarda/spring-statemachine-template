package com.yaf.statemachine.service.iml;

import com.yaf.statemachine.domain.LightGroupAction;
import com.yaf.statemachine.domain.LightGroupEvent;
import com.yaf.statemachine.domain.LightGroupState;
import com.yaf.statemachine.repository.LightGroupActionRepository;
import com.yaf.statemachine.service.LightService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;

@RequiredArgsConstructor
public class LightServiceIml implements LightService {

    public static final String LIGHTGROUP_ID_HEADER = "lightgroup_id";

    private final LightGroupActionRepository lightGroupRepository;
    private final StateMachineFactory<LightGroupState, LightGroupEvent> stateMachineFactory;

    @Override
    public LightGroupAction newLightGroupAction(LightGroupAction lightGroupAction) {
        lightGroupAction.setState(LightGroupState.INITIAL);
        return lightGroupRepository.save(lightGroupAction);
    }

    @Override
    public StateMachine<LightGroupState, LightGroupEvent> turnOnLights(Long lightGroupActionId) {
        StateMachine<LightGroupState, LightGroupEvent> sm = build(lightGroupActionId);
        sendEvent(lightGroupActionId, sm, LightGroupEvent.TURN_ALL_LIGHTS_ON);
        return null;
    }

    @Override
    public StateMachine<LightGroupState, LightGroupEvent> turnOffLights(Long lightGroupActionId) {
        StateMachine<LightGroupState, LightGroupEvent> sm = build(lightGroupActionId);
        sendEvent(lightGroupActionId, sm, LightGroupEvent.TURN_ALL_LIGHTS_OFF);
        return null;
    }

    @Override
    public StateMachine<LightGroupState, LightGroupEvent> turnOnOneLight(Long lightGroupActionId) {
        StateMachine<LightGroupState, LightGroupEvent> sm = build(lightGroupActionId);
        sendEvent(lightGroupActionId, sm, LightGroupEvent.TURN_ALL_LIGHTS_ON); //TODO
        return null;
    }

    @Override
    public StateMachine<LightGroupState, LightGroupEvent> notificationReceived(Long lightGroupActionId) {
        StateMachine<LightGroupState, LightGroupEvent> sm = build(lightGroupActionId);
        sendEvent(lightGroupActionId, sm, LightGroupEvent.NOTIFICATION_RECEIVED);
        return null;
    }

    @Override
    public StateMachine<LightGroupState, LightGroupEvent> remind(Long lightGroupActionId) {
        StateMachine<LightGroupState, LightGroupEvent> sm = build(lightGroupActionId);
        sendEvent(lightGroupActionId, sm, LightGroupEvent.REMIND_SOMETHING);
        return null;
    }

    private void sendEvent(Long lightGroupActionId, StateMachine<LightGroupState, LightGroupEvent> sm, LightGroupEvent event) {

        var msg = MessageBuilder.withPayload(event)
                .setHeader(LIGHTGROUP_ID_HEADER, lightGroupActionId)
                .build();

        sm.sendEvent(msg);

    }

    private StateMachine<LightGroupState, LightGroupEvent> build(Long lightGroupActionId) {

        var lightGroup = lightGroupRepository.getOne(lightGroupActionId);
        var sm = stateMachineFactory.getStateMachine(Long.toString(lightGroupActionId));

        sm.getStateMachineAccessor()
                .doWithAllRegions(sma -> {
                    sma.resetStateMachine(new DefaultStateMachineContext<>(lightGroup.getState(), null, null, null));
                });
        sm.start();
        return sm;
    }
}
