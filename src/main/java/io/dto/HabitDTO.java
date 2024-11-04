package io.dto;

import java.time.LocalDate;

public class HabitDTO {
    private long id;
    private String title;
    private String description;
    private String frequency;
    private boolean completed;
    private LocalDate creationDate;
    private long userId;

    public HabitDTO(long id, String title, String description, String frequency, boolean completed, LocalDate creationDate, long userId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.frequency = frequency;
        this.completed = completed;
        this.creationDate = creationDate;
        this.userId = userId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
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

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
