package com.yaf.statemachine.statemachine;

import com.yaf.statemachine.domain.LightGroupState;
import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Getter
@Setter
@Table(name = "living_room_sm")
public class LivingRoomStateMachine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private LightGroupState state;

    private String name;

}
