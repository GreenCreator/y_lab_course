package ylab.utils;

import ylab.entity.habit.Habit;
import ylab.entity.user.User;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class HabitStatistics {

    public static long getCompletionCountForDay(Habit habit, LocalDate day) {
        var completionHistory = habit.getCompletionHistory();
        return completionHistory.stream()
                .filter(completionDate -> completionDate.equals(day))
                .count();
    }

    public static long getCompletionCountForWeek(Habit habit, LocalDate weekStart) {
        List<LocalDate> completionHistory = habit.getCompletionHistory();
        LocalDate weekEnd = weekStart.plus(1, ChronoUnit.WEEKS);
        return completionHistory.stream()
                .filter(completionDate -> !completionDate.isBefore(weekStart) && !completionDate.isAfter(weekEnd.minusDays(1)))
                .count();
    }


    public static long getCompletionCountForMonth(Habit habit, LocalDate monthStart) {
        List<LocalDate> completionHistory = habit.getCompletionHistory();
        LocalDate monthEnd = monthStart.plus(1, ChronoUnit.MONTHS);
        return completionHistory.stream()
                .filter(completionDate -> !completionDate.isBefore(monthStart) && completionDate.isBefore(monthEnd))
                .count();
    }

    public static int getCurrentStreak(Habit habit) {
        List<LocalDate> completionHistory = habit.getCompletionHistory();
        if (completionHistory.isEmpty()) {
            return 0;
        }

        completionHistory.sort(Collections.reverseOrder());

        int streak = 0;
        LocalDate currentDate = LocalDate.now();

        for (LocalDate completionDate : completionHistory) {
            long daysBetween = ChronoUnit.DAYS.between(completionDate, currentDate);

            if (daysBetween == streak) {
                streak++;
            } else {
                break;
            }
        }

        return streak;
    }


    public static double getSuccessPercentage(Habit habit, LocalDate startDate, LocalDate endDate) {
        List<LocalDate> completionHistory = habit.getCompletionHistory();

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date.");
        }

        long totalDays = ChronoUnit.DAYS.between(startDate, endDate) + 1;

        if (totalDays <= 0) {
            return 0.0;
        }

        long successfulDays = completionHistory.stream()
                .filter(date -> !date.isBefore(startDate) && !date.isAfter(endDate))
                .count();

        return (successfulDays / (double) totalDays) * 100;
    }

    public void generateProgressReport(User user, LocalDate startDate, LocalDate endDate) {
        Map<Long, Habit> habits = user.getHabitManager().listHabits(user.getId());

        if (habits.isEmpty()) {
            System.out.println("No habits found for the user.");
            return;
        }

        System.out.println("Progress Report from " + startDate + " to " + endDate);
        System.out.println("-----------------------------------------------------");

        for (Habit habit : habits.values()) {
            int streak = getCurrentStreak(habit);
            double successPercentage = getSuccessPercentage(habit, startDate, endDate);
            long totalSuccessfulDays = habit.getCompletionHistory().stream()
                    .filter(date -> !date.isBefore(startDate) && !date.isAfter(endDate))
                    .count();

            System.out.printf("Habit: %s%n", habit.getTitle());
            System.out.printf("Current Streak: %d days%n", streak);
            System.out.printf("Success Percentage: %.2f%%%n", successPercentage);
            System.out.printf("Total Successful Days: %d%n", totalSuccessfulDays);
            System.out.println("-----------------------------------------------------");
        }
    }

}
