package org.fahdpln.backend.schedule;

public enum Weekday {
    MONDAY("Monday"),
    TUESDAY("Tuesday"),
    WEDNESDAY("Wednesday"),
    THURSDAY("Thursday"),
    FRIDAY("Friday"),
    SATURDAY("Saturday"),
    SUNDAY("Sunday");

    private final String displayName;

    Weekday(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }

    public static Weekday fromString(String text) {
        for (Weekday day : Weekday.values()) {
            if (day.displayName.equalsIgnoreCase(text)) {
                return day;
            }
        }
        throw new IllegalArgumentException("No enum constant " + text);
    }
}