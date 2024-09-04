package org.fahdpln.backend.schedule;

import org.fahdpln.backend.employee.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    void deleteAllByEmployee(Employee employee);

}
