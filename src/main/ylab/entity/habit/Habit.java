package ylab.entity.habit;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Habit {
    private String title;
    private String description;
    private String frequency;
    private boolean completed;
    private LocalDate creationDate;
    private List<LocalDate> completionHistory;

    public Habit(String title, String description, String frequency) {
        this.title = title;
        this.description = description;
        this.frequency = frequency;
        this.completed = false;
        this.creationDate = LocalDate.now();
        this.completionHistory = new ArrayList<>();
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
}
