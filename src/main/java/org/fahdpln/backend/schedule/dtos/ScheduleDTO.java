package org.fahdpln.backend.schedule.dtos;


import org.fahdpln.backend.schedule.Weekday;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleDTO {

    private Long id;
    private Weekday weekday;

    private ScheduleTime scheduleTime;

}
