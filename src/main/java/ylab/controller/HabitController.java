package ylab.controller;

import io.dto.HabitDTO;
import io.service.HabitService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/habits")
public class HabitController {

    private final HabitService habitService;

    public HabitController(HabitService habitService) {
        this.habitService = habitService;
    }

    @PostMapping
    public ResponseEntity<Void> createHabit(@Valid @RequestBody HabitDTO habitDTO) {
        habitService.createHabit(habitDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<HabitDTO>> getAllHabits(@RequestParam Long userId) {
        List<HabitDTO> habits = habitService.getAllHabits(userId);
        return ResponseEntity.ok(habits);
    }

    @GetMapping("/{title}")
    public ResponseEntity<HabitDTO> getHabitByTitle(@PathVariable String title) {
        HabitDTO habit = habitService.getHabitByTitle(title);
        return ResponseEntity.ok(habit);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHabitById(@PathVariable Long id) {
        habitService.deleteHabitById(id);
        return ResponseEntity.noContent().build();
    }
}
