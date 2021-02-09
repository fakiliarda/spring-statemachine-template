package com.yaf.statemachine.repository;

import com.yaf.statemachine.domain.LightGroupAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LightGroupActionRepository extends JpaRepository<LightGroupAction, Long> {
}
