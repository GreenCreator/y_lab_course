package ylab.entity.habit;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Habit {
    private long id;
    private String title;
    private String description;
    private String frequency;
    private boolean completed;
    private LocalDate creationDate;
    private List<LocalDate> completionHistory;
    private long userId;

    public Habit(long id, String title, String description, String frequency, boolean completed, LocalDate creationDate, long userId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.frequency = frequency;
        this.completed = completed;
        this.creationDate = creationDate;
        this.completionHistory = new ArrayList<>();
        this.userId = userId;
    }

    public void markCompleted() {
        this.completed = true;
        completionHistory.add(LocalDate.now());
    }

    public List<LocalDate> getCompletionHistory() {
        return completionHistory;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public long getUserId() {
        return userId;
    }
}
