package com.yaf.statemachine.config;

import com.yaf.statemachine.domain.LightGroupEvent;
import com.yaf.statemachine.domain.LightGroupState;
import com.yaf.statemachine.service.iml.LightServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import java.util.EnumSet;
import java.util.Random;

@EnableStateMachineFactory
@Slf4j
@Configuration
public class StateMachineConfig extends StateMachineConfigurerAdapter<LightGroupState, LightGroupEvent> {

    @Override
    public void configure(StateMachineStateConfigurer<LightGroupState, LightGroupEvent> states) throws Exception {
        states.withStates()
                .initial(LightGroupState.INITIAL)
                .states(EnumSet.allOf(LightGroupState.class))
                .end(LightGroupState.ONE_LIGHT_ON)
                .end(LightGroupState.ALL_LIGHTS_OFF)
                .end(LightGroupState.ONE_LIGHT_ON)
                .end(LightGroupState.ALL_LIGHTS_COLORED);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<LightGroupState, LightGroupEvent> transitions) throws Exception {
        transitions
                .withExternal().source(LightGroupState.INITIAL).event(LightGroupEvent.TURN_ALL_LIGHTS_ON).target(LightGroupState.ALL_LIGHTS_ON).action(turnOnAction())
                .and()
                .withExternal().source(LightGroupState.ALL_LIGHTS_OFF).event(LightGroupEvent.TURN_ALL_LIGHTS_ON).target(LightGroupState.ALL_LIGHTS_ON)
                .and()
                .withExternal().source(LightGroupState.ALL_LIGHTS_OFF).event(LightGroupEvent.NOTIFICATION_RECEIVED).target(LightGroupState.ALL_LIGHTS_COLORED)
                .and()
                .withExternal().source(LightGroupState.ALL_LIGHTS_OFF).event(LightGroupEvent.REMIND_SOMETHING).target(LightGroupState.ALL_LIGHTS_OFF)
                .and()
                .withExternal().source(LightGroupState.ALL_LIGHTS_ON).event(LightGroupEvent.TURN_ALL_LIGHTS_OFF).target(LightGroupState.ALL_LIGHTS_OFF)
                .and()
                .withExternal().source(LightGroupState.ALL_LIGHTS_ON).event(LightGroupEvent.NOTIFICATION_RECEIVED).target(LightGroupState.ALL_LIGHTS_COLORED)
                .and()
                .withExternal().source(LightGroupState.ALL_LIGHTS_ON).event(LightGroupEvent.REMIND_SOMETHING).target(LightGroupState.ALL_LIGHTS_ON)
                .and()
                .withExternal().source(LightGroupState.INITIAL).event(LightGroupEvent.TURN_ALL_LIGHTS_ON).target(LightGroupState.ALL_LIGHTS_ON)
                .and()
                .withExternal().source(LightGroupState.INITIAL).event(LightGroupEvent.TURN_ALL_LIGHTS_OFF).target(LightGroupState.ALL_LIGHTS_OFF)
                .and()
                .withExternal().source(LightGroupState.INITIAL).event(LightGroupEvent.REMIND_SOMETHING).target(LightGroupState.ALL_LIGHTS_ON)
                .and()
                .withExternal().source(LightGroupState.INITIAL).event(LightGroupEvent.NOTIFICATION_RECEIVED).target(LightGroupState.ALL_LIGHTS_COLORED);
    }

    @Override
    public void configure(StateMachineConfigurationConfigurer<LightGroupState, LightGroupEvent> config) throws Exception {
        StateMachineListenerAdapter<LightGroupState, LightGroupEvent> adapter = new StateMachineListenerAdapter<>() {
            @Override
            public void stateChanged(State<LightGroupState, LightGroupEvent> from, State<LightGroupState, LightGroupEvent> to) {
                log.info(String.format("stateChanged(from: %s to: %s)", from, to));
            }
        };

        config.withConfiguration().listener(adapter);
    }

    public Action<LightGroupState, LightGroupEvent> turnOnAction() {
        return stateContext -> {
            if (new Random().nextInt(10) < 8) {
                System.err.println("Turn on SUCCESS");
                stateContext.getStateMachine().sendEvent(MessageBuilder.withPayload(LightGroupEvent.TURN_ALL_LIGHTS_ON)
                        .setHeader(LightServiceImpl.LIGHTGROUP_ID_HEADER, stateContext.getMessageHeader(LightServiceImpl.LIGHTGROUP_ID_HEADER))
                        .build());

            } else {
                System.err.println("Turn on FAIL");
                stateContext.getStateMachine().sendEvent(MessageBuilder.withPayload(LightGroupEvent.TURN_ALL_LIGHTS_OFF)
                        .setHeader(LightServiceImpl.LIGHTGROUP_ID_HEADER, stateContext.getMessageHeader(LightServiceImpl.LIGHTGROUP_ID_HEADER))
                        .build());

            }
        };

    }
}
