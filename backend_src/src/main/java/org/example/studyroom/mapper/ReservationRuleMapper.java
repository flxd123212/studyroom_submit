package org.example.studyroom.mapper;

import org.apache.ibatis.annotations.*;
import org.example.studyroom.entity.ReservationRule;

@Mapper
public interface ReservationRuleMapper {

    @Select("SELECT rule_id ruleId, max_per_user maxPerUser, max_hours maxHours, " +
            "allow_cancel_minutes allowCancelMinutes, auto_checkout_minutes autoCheckoutMinutes " +
            "FROM reservation_rule LIMIT 1")
    ReservationRule getRule();

    @Update("UPDATE reservation_rule SET max_per_user=#{maxPerUser}, max_hours=#{maxHours}, " +
            "allow_cancel_minutes=#{allowCancelMinutes}, auto_checkout_minutes=#{autoCheckoutMinutes} " +
            "WHERE rule_id=#{ruleId}")
    int updateRule(ReservationRule rule);
}
