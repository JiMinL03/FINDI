package project.capston.Findi.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.capston.Findi.Entity.Calendar;

@Repository
public interface CalendarRepository extends JpaRepository<Calendar, Integer> {
}
