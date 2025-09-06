package jason.parser;

import jason.model.Deadline;
import jason.model.Event;
import jason.model.TaskList;
import jason.model.Todo;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

class DateTimeUtilTest {

class TaskListTest {

    @Test
    void parseIsoDate_onlyDate_ok() {
        LocalDateTime ldt = DateTimeUtil.parseIsoDateOrDateTime("2025-09-04");
        assertEquals(LocalDateTime.of(2025, 9, 4, 0, 0), ldt);
    }

    @Test
    void parseIsoDate_withTime_ok() {
        LocalDateTime ldt = DateTimeUtil.parseIsoDateOrDateTime("2025-09-04 14:30");
        assertEquals(LocalDateTime.of(2025, 9, 4, 14, 30), ldt);
    }

    @Test
    void parseIsoDate_invalid_throws() {
        Exception e = assertThrows(DateTimeParseException.class,
                () -> DateTimeUtil.parseIsoDateOrDateTime("09-04-2025"));
        assertEquals(DateTimeParseException.class, e.getClass());
    }

    /* ---------------- Day/Month/Year with time ---------------- */

    @Test
    void parseDayMonthYearWithTime_preferDmy_ok() {
        LocalDateTime ldt = DateTimeUtil.parseDayMonthYearWithTime("04/09/25 09:15", true);
        assertEquals(LocalDateTime.of(2025, 9, 4, 9, 15), ldt);
    }

    @Test
    void parseDayMonthYearWithTime_preferMdy_ok() {
        LocalDateTime ldt = DateTimeUtil.parseDayMonthYearWithTime("04/09/25 09:15", false);
        assertEquals(LocalDateTime.of(2025, 4, 9, 9, 15), ldt);
    }

    @Test
    void parseDayMonthYearWithTime_disambiguateByValue_ok() {
        LocalDateTime ldt = DateTimeUtil.parseDayMonthYearWithTime("13/02/25 08:00", false);
        assertEquals(LocalDateTime.of(2025, 2, 13, 8, 0), ldt); // must be 13th Feb
    }

    @Test
    void parseDayMonthYearWithTime_twoDigitYear_ok() {
        LocalDateTime ldt = DateTimeUtil.parseDayMonthYearWithTime("01/01/05 00:00", true);
        assertEquals(LocalDateTime.of(2005, 1, 1, 0, 0), ldt);
    }

    @Test
    void parseDayMonthYearWithTime_invalid_throws() {
        Exception e = assertThrows(DateTimeParseException.class,
                () -> DateTimeUtil.parseDayMonthYearWithTime("invalid", true));
        assertEquals(DateTimeParseException.class, e.getClass());
    }

    /* ---------------- Time only ---------------- */

    @Test
    void parseTimeHm_ok() {
        LocalTime t = DateTimeUtil.parseTimeHm("07:45");
        assertEquals(LocalTime.of(7, 45), t);
    }

    /* ---------------- Formatting ---------------- */

    @Test
    void formatHuman_midnight_onlyDate() {
        LocalDateTime ldt = LocalDateTime.of(2025, 9, 4, 0, 0);
        assertEquals("Sep 04 2025", DateTimeUtil.formatHuman(ldt));
    }

    @Test
    void formatHuman_withTime() {
        LocalDateTime ldt = LocalDateTime.of(2025, 9, 4, 14, 30);
        assertEquals("04-09-2025 14:30", DateTimeUtil.formatHuman(ldt));
    }

    @Test
    void formatIso_ok() {
        LocalDateTime ldt = LocalDateTime.of(2025, 9, 4, 14, 30);
        assertEquals("2025-09-04T14:30", DateTimeUtil.formatIso(ldt));
    }

    @Test
    void formatIsoWithSpace_ok() {
        LocalDateTime ldt = LocalDateTime.of(2025, 9, 4, 14, 30);
        assertEquals("2025-09-04 14:30", DateTimeUtil.formatIsoWithSpace(ldt));
    }

    /* ---------------- ISO date-only ---------------- */

    @Test
    void parseIsoDateOnly_ok() {
        LocalDate d = DateTimeUtil.parseIsoDateOnly("2025-09-04");
        assertEquals(LocalDate.of(2025, 9, 4), d);
    }

    /* ---------------- Loose parsing ---------------- */

    @Test
    void tryParseLoose_validIso_ok() {
        LocalDateTime ldt = DateTimeUtil.tryParseLoose("2025-09-04T10:00");
        assertEquals(LocalDateTime.of(2025, 9, 4, 10, 0), ldt);
    }

    @Test
    void tryParseLoose_validLegacy_ok() {
        LocalDateTime ldt = DateTimeUtil.tryParseLoose("4/9/2025 10:00");
        assertEquals(LocalDateTime.of(2025, 9, 4, 10, 0), ldt);
    }
}
