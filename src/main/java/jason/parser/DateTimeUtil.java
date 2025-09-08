package jason.parser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Utility class for parsing and formatting date and time.
 */
public final class DateTimeUtil {
    private DateTimeUtil() {
    }

    /**
     * Tie‑breaker when both day and month ≤ 12. true = prefer dd/MM/yy; false =
     * prefer MM/dd/yy.
     */
    public static final boolean PREFER_DMY = true;

    /**
     * Parses an ISO 8601 date or date-time string.
     * @param s the input string
     * @return the parsed LocalDateTime
     */
    public static LocalDateTime parseIsoDateOrDateTime(String s) {
        assert s != null; // caller should ensure non-null
        s = s.trim();
        try {
            // Try with time first using optional section
            return LocalDateTime.parse(s, DateTimeFormatter.ofPattern("uuuu-MM-dd[ HH:mm]"));
        } catch (DateTimeParseException ignored) {
            // Ignored
        }
        try {
            return LocalDate.parse(s, DateTimeFormatter.ISO_LOCAL_DATE).atStartOfDay();
        } catch (DateTimeParseException e) {
            throw new DateTimeParseException("Expected yyyy-MM-dd or yyyy-MM-dd HH:mm", s, 0);
        }
    }

    /**
     * Parses a date-time string in the format dd/MM/yy HH:mm or MM/dd/yy HH:mm.
     * @param input the input string
     * @param preferDmy true to prefer dd/MM/yy, false to prefer MM/dd/yy
     * @return the parsed LocalDateTime
     */
    public static LocalDateTime parseDayMonthYearWithTime(String input, boolean preferDmy) {
        assert input != null; // caller should ensure non-null
        String s = input.trim();
        String[] parts = s.split("\\s+", 2);
        if (parts.length != 2) {
            throw new DateTimeParseException("Expected date time", s, 0);
        }

        String[] dateParts = parts[0].split("[/-]");
        if (dateParts.length != 3) {
            throw new DateTimeParseException("Expected dd/MM/yy or MM/dd/yy", s, 0);
        }
        int a = Integer.parseInt(dateParts[0]);
        int b = Integer.parseInt(dateParts[1]);
        int year = Integer.parseInt(dateParts[2]);
        if (year < 100) {
            year += 2000; // normalize two‑digit years
        }
        int day;
        int month;

        if (a > 12 && b <= 12) {
            day = a;
            month = b;
        } else if (b > 12 && a <= 12) {
            day = b;
            month = a;
        } else {
            if (preferDmy) {
                day = a;
                month = b;
            } else {
                day = b;
                month = a;
            }
        }

        assert month >= 1 && month <= 12;
        assert day >= 1 && day <= 31;
        // Note: LocalDate.of() will throw DateTimeException if day is invalid for month

        LocalDate d = LocalDate.of(year, month, day);
        LocalTime t = LocalTime.parse(parts[1], DateTimeFormatter.ofPattern("H:mm"));
        return LocalDateTime.of(d, t);
    }

    /**
     * Parses a time string in the format HH:mm.
     * @param hm the input string
     * @return the parsed LocalTime
     */
    public static LocalTime parseTimeHm(String hm) {
        assert hm != null; // caller should ensure non-null
        return LocalTime.parse(hm.trim(), DateTimeFormatter.ofPattern("H:mm"));
    }

    /**
     * Formats a LocalDateTime object into a human-readable string.
     * @param ldt the LocalDateTime to format
     * @return the formatted string
     */
    public static String formatHuman(LocalDateTime ldt) {
        assert ldt != null; // caller should ensure non-null
        if (ldt.toLocalTime().equals(LocalTime.MIDNIGHT)) {
            return ldt.format(DateTimeFormatter.ofPattern("MMM dd uuuu"));
        }
        return ldt.format(DateTimeFormatter.ofPattern("dd-MM-uuuu HH:mm"));
    }

    /**
     * Formats a LocalDateTime object into an ISO 8601 string.
     * @param ldt the LocalDateTime to format
     * @return the formatted string
     */
    public static String formatIso(LocalDateTime ldt) {
        assert ldt != null; // caller should ensure non-null
        return ldt.toString();
    }

    /**
     * Formats a LocalDateTime object into an ISO 8601 string with a space between date and time.
     * @param ldt the LocalDateTime to format
     * @return the formatted string
     */
    public static String formatIsoWithSpace(LocalDateTime ldt) {
        assert ldt != null; // caller should ensure non-null
        // yyyy-MM-dd HH:mm (space between date and time)
        return ldt.format(DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm"));
    }

    /**
     * Parses an ISO 8601 date-only string.
     * @param s the input string
     * @return the parsed LocalDate
     */
    public static LocalDate parseIsoDateOnly(String s) {
        assert s != null; // caller should ensure non-null
        return LocalDate.parse(s.trim(), DateTimeFormatter.ISO_LOCAL_DATE);
    }

    /**
     * Best‑effort parser for legacy human strings in files. Falls back to now if
     * failed.
     */
    public static LocalDateTime tryParseLoose(String s) {
        assert s != null; // caller should ensure non-null
        try {
            return LocalDateTime.parse(s);
        } catch (Exception ignored) {
            // Ignored
        }
        try {
            return LocalDateTime.parse(s, DateTimeFormatter.ofPattern("d/M/uuuu H:mm"));
        } catch (Exception ignored) {
            // Ignored
        }
        return LocalDateTime.now();
    }
}