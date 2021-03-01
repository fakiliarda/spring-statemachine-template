package com.yaf.statemachine.config;

import com.yaf.statemachine.client.HueClient;
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

@EnableStateMachineFactory
@Slf4j
@Configuration
public class StateMachineConfig extends StateMachineConfigurerAdapter<LightGroupState, LightGroupEvent> {

    private final HueClient hueClient;

    public StateMachineConfig(HueClient hueClient) {
        this.hueClient = hueClient;
    }

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
                .withExternal().source(LightGroupState.INITIAL).event(LightGroupEvent.TURN_ALL_LIGHTS_ON).target(LightGroupState.ALL_LIGHTS_ON).action(turnAllLightsOnAction())
                .and()
                .withExternal().source(LightGroupState.ALL_LIGHTS_OFF).event(LightGroupEvent.TURN_ALL_LIGHTS_ON).target(LightGroupState.ALL_LIGHTS_ON)
                .and()
                .withExternal().source(LightGroupState.ALL_LIGHTS_OFF).event(LightGroupEvent.NOTIFICATION_RECEIVED).target(LightGroupState.ALL_LIGHTS_COLORED)
                .and()
                .withExternal().source(LightGroupState.ALL_LIGHTS_OFF).event(LightGroupEvent.REMIND_SOMETHING).target(LightGroupState.ALL_LIGHTS_OFF)
                .and()
                .withExternal().source(LightGroupState.ALL_LIGHTS_ON).event(LightGroupEvent.TURN_ALL_LIGHTS_OFF).target(LightGroupState.ALL_LIGHTS_OFF)
                .and()
                .withExternal().source(LightGroupState.ALL_LIGHTS_ON).event(LightGroupEvent.NOTIFICATION_RECEIVED).target(LightGroupState.ALL_LIGHTS_COLORED).action(notificationReceivedAction())
                .and()
                .withExternal().source(LightGroupState.ALL_LIGHTS_ON).event(LightGroupEvent.REMIND_SOMETHING).target(LightGroupState.ALL_LIGHTS_COLORED).action(remindAction())
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

    private Action<LightGroupState, LightGroupEvent> turnAllLightsOnAction() {
        return stateContext -> {

            try {
                hueClient.changeAllLightStates(true);

                log.info("Success: Turning on all lights");
                stateContext.getStateMachine().sendEvent(MessageBuilder.withPayload(LightGroupEvent.TURN_ALL_LIGHTS_ON)
                        .setHeader(LightServiceImpl.LIGHTGROUP_ID_HEADER, stateContext.getMessageHeader(LightServiceImpl.LIGHTGROUP_ID_HEADER))
                        .build());

            } catch (Exception exception) {

                log.error("FAIL: Turning on all lights");
                stateContext.getStateMachine().sendEvent(MessageBuilder.withPayload(LightGroupEvent.TURN_ALL_LIGHTS_OFF)
                        .setHeader(LightServiceImpl.LIGHTGROUP_ID_HEADER, stateContext.getMessageHeader(LightServiceImpl.LIGHTGROUP_ID_HEADER))
                        .build());

            }


        };

    }

    private Action<LightGroupState, LightGroupEvent> remindAction() {
        return stateContext -> {

            try {
                hueClient.changeAllLightStatesOnAndColor(65535);

                log.info("Success: Turning on all lights and change color");
                stateContext.getStateMachine().sendEvent(MessageBuilder.withPayload(LightGroupEvent.REMIND_SOMETHING)
                        .setHeader(LightServiceImpl.LIGHTGROUP_ID_HEADER, stateContext.getMessageHeader(LightServiceImpl.LIGHTGROUP_ID_HEADER))
                        .build());

            } catch (Exception exception) {

                log.error("FAIL: Turning on all lights and change color");
                stateContext.getStateMachine().sendEvent(MessageBuilder.withPayload(LightGroupEvent.TURN_ALL_LIGHTS_OFF)
                        .setHeader(LightServiceImpl.LIGHTGROUP_ID_HEADER, stateContext.getMessageHeader(LightServiceImpl.LIGHTGROUP_ID_HEADER))
                        .build());

            }

        };

    }


    private Action<LightGroupState, LightGroupEvent> notificationReceivedAction() {
        return stateContext -> {

            try {
                hueClient.changeAllLightStatesOnAndColor(0);
                hueClient.flickLights();
                hueClient.changeAllLightStatesOnAndColor(3000);

                log.info("Success: Flick all lights and change color");
                stateContext.getStateMachine().sendEvent(MessageBuilder.withPayload(LightGroupEvent.NOTIFICATION_RECEIVED)
                        .setHeader(LightServiceImpl.LIGHTGROUP_ID_HEADER, stateContext.getMessageHeader(LightServiceImpl.LIGHTGROUP_ID_HEADER))
                        .build());

            } catch (Exception exception) {

                log.error("FAIL: Flick all lights and change color");
                stateContext.getStateMachine().sendEvent(MessageBuilder.withPayload(LightGroupEvent.TURN_ALL_LIGHTS_OFF)
                        .setHeader(LightServiceImpl.LIGHTGROUP_ID_HEADER, stateContext.getMessageHeader(LightServiceImpl.LIGHTGROUP_ID_HEADER))
                        .build());

            }

        };

    }



}
