package org.fahdpln.backend.schedule.dtos;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.Map;

import org.fahdpln.backend.schedule.Weekday;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleRequest {

    
    @NotNull(message = "Schedule must not be null")
    @Valid
    private Map<Weekday, ScheduleTime> schedule;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ScheduleTime {
        @NotNull(message = "Start hour must not be null")
        private LocalTime startHour;

        @NotNull(message = "End hour must not be null")
        private LocalTime endHour;
    }
}
