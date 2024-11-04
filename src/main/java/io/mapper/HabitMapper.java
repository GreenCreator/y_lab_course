package io.mapper;

import io.dto.HabitDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ylab.entity.habit.Habit;

@Mapper
public interface HabitMapper {
    HabitMapper INSTANCE = Mappers.getMapper(HabitMapper.class);

    HabitDTO habitToHabitDTO(Habit habit);

    Habit habitDTOToHabit(HabitDTO habitDTO);
}
