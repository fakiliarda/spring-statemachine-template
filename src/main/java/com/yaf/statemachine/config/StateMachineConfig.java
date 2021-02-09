package com.yaf.statemachine.config;

import com.yaf.statemachine.domain.LightGroupEvent;
import com.yaf.statemachine.domain.LightGroupState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import java.util.EnumSet;

@EnableStateMachineFactory
@Slf4j
@Configuration
public class StateMachineConfig extends StateMachineConfigurerAdapter<LightGroupState, LightGroupEvent> {

    @Override
    public void configure(StateMachineStateConfigurer<LightGroupState, LightGroupEvent> states) throws Exception {
        states.withStates()
                .initial(LightGroupState.UNKNOWN)
                .states(EnumSet.allOf(LightGroupState.class))
                .end(LightGroupState.ONE_LIGHT_ON)
                .end(LightGroupState.ALL_LIGHTS_OFF)
                .end(LightGroupState.ONE_LIGHT_ON)
                .end(LightGroupState.ALL_LIGHTS_COLORED);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<LightGroupState, LightGroupEvent> transitions) throws Exception {
        transitions
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
                .withExternal().source(LightGroupState.UNKNOWN).event(LightGroupEvent.TURN_ALL_LIGHTS_ON).target(LightGroupState.ALL_LIGHTS_ON)
                .and()
                .withExternal().source(LightGroupState.UNKNOWN).event(LightGroupEvent.TURN_ALL_LIGHTS_OFF).target(LightGroupState.ALL_LIGHTS_OFF)
                .and()
                .withExternal().source(LightGroupState.UNKNOWN).event(LightGroupEvent.REMIND_SOMETHING).target(LightGroupState.ALL_LIGHTS_ON)
                .and()
                .withExternal().source(LightGroupState.UNKNOWN).event(LightGroupEvent.NOTIFICATION_RECEIVED).target(LightGroupState.ALL_LIGHTS_COLORED);
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
}
