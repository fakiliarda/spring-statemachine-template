package com.yaf.statemachine.repository;

import com.yaf.statemachine.domain.LightGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LightGroupRepository extends JpaRepository<LightGroup, Long> {
}
