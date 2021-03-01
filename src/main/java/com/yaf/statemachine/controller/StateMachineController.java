package com.yaf.statemachine.controller;

import com.yaf.statemachine.domain.LightGroupEvent;
import com.yaf.statemachine.repository.LivingRoomStateMachineRepository;
import com.yaf.statemachine.service.LightService;
import com.yaf.statemachine.statemachine.LivingRoomStateMachine;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api")
public class StateMachineController {

    private final LightService lightService;
    private final LivingRoomStateMachineRepository livingRoomStateMachineRepository;

    public StateMachineController(LightService lightService, LivingRoomStateMachineRepository livingRoomStateMachineRepository) {
        this.lightService = lightService;
        this.livingRoomStateMachineRepository = livingRoomStateMachineRepository;
    }

    @GetMapping(value = "start")
    public Object start(@RequestHeader(value = "name") String stateMachineName) {

        var stateMachine = LivingRoomStateMachine.builder().name(stateMachineName).build();
        return lightService.createLivingRoomStateMachine(stateMachine);

    }

    @PostMapping(value = "execute")
    public void execute(@RequestHeader(value = "stateMachineId") Long stateMachineId,
                        @RequestHeader(value = "event") String eventName) {

        var event = LightGroupEvent.valueOf(eventName);

        lightService.execute(stateMachineId, event);

        var stateMachine = livingRoomStateMachineRepository.getOne(stateMachineId);
        System.err.println(stateMachine);


    }


}
