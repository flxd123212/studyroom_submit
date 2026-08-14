package org.example.studyroom.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ReservationRule {
    private Integer ruleId;
    private Integer maxPerUser;
    private BigDecimal maxHours;
    private Integer allowCancelMinutes;
    private Integer autoCheckoutMinutes;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public Integer getRuleId() { return ruleId; }
    public void setRuleId(Integer ruleId) { this.ruleId = ruleId; }
    public Integer getMaxPerUser() { return maxPerUser; }
    public void setMaxPerUser(Integer maxPerUser) { this.maxPerUser = maxPerUser; }
    public BigDecimal getMaxHours() { return maxHours; }
    public void setMaxHours(BigDecimal maxHours) { this.maxHours = maxHours; }
    public Integer getAllowCancelMinutes() { return allowCancelMinutes; }
    public void setAllowCancelMinutes(Integer allowCancelMinutes) { this.allowCancelMinutes = allowCancelMinutes; }
    public Integer getAutoCheckoutMinutes() { return autoCheckoutMinutes; }
    public void setAutoCheckoutMinutes(Integer autoCheckoutMinutes) { this.autoCheckoutMinutes = autoCheckoutMinutes; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
}
