package com.yaf.statemachine.service;

import com.yaf.statemachine.domain.LightGroupEvent;
import com.yaf.statemachine.domain.LightGroupState;
import com.yaf.statemachine.repository.LivingRoomStateMachineRepository;
import com.yaf.statemachine.service.iml.LightServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LightActionStateChangeInterceptor extends StateMachineInterceptorAdapter<LightGroupState, LightGroupEvent> {

    private final LivingRoomStateMachineRepository livingRoomStateMachineRepository;

    @Override
    public void preStateChange(State<LightGroupState, LightGroupEvent> state, Message<LightGroupEvent> message, Transition<LightGroupState, LightGroupEvent> transition, StateMachine<LightGroupState, LightGroupEvent> stateMachine) {

        Optional.ofNullable(message).ifPresent(msg -> {
            Optional.ofNullable(Long.class.cast(msg.getHeaders().getOrDefault(LightServiceImpl.LIGHTGROUP_ID_HEADER, -1L)))
                    .ifPresent(id -> {
                        var livingRoomStateMachine = livingRoomStateMachineRepository.getOne(id);
                        livingRoomStateMachine.setState(state.getId());
                        livingRoomStateMachineRepository.save(livingRoomStateMachine);
                    });
        });

    }

}
