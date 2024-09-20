package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.mpa.MpaDto;
import ru.yandex.practicum.filmorate.dto.mpa.NewMpaRequest;
import ru.yandex.practicum.filmorate.dto.mpa.UpdateMpaRequest;
import ru.yandex.practicum.filmorate.model.Mpa;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MpaMapper {

    public static MpaDto mapToMpaDto(Mpa mpa) {
        MpaDto dto = new MpaDto();
        dto.setId(mpa.getId());
        dto.setName(mpa.getName());
        return dto;
    }

    public static Mpa mapToMpa(NewMpaRequest request) {
        Mpa mpa = new Mpa();
        mpa.setName(request.getName());
        mpa.setDescription(request.getDescription());

        return mpa;
    }

    public static Mpa updateMpaFields(Mpa mpa, UpdateMpaRequest request) {
        mpa.setName(request.getName());

        if (request.hasDescription()) {
            mpa.setDescription(request.getDescription());
        }

        return mpa;
    }
}
