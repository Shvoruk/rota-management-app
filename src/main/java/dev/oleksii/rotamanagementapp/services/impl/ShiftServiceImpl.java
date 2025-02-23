package dev.oleksii.rotamanagementapp.services.impl;

import dev.oleksii.rotamanagementapp.domain.entities.Shift;
import dev.oleksii.rotamanagementapp.domain.repos.ShiftRepository;
import dev.oleksii.rotamanagementapp.exceptions.NotFoundException;
import dev.oleksii.rotamanagementapp.services.ShiftService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ShiftServiceImpl implements ShiftService {

    private final ShiftRepository shiftRepository;

    public ShiftServiceImpl(ShiftRepository shiftRepository) {
        this.shiftRepository = shiftRepository;
    }

    @Override
    public Shift getShiftByTeamIdAndShiftId(UUID teamId, UUID shiftId) {
        return shiftRepository.findByTeamIdAndShiftId(teamId, shiftId)
                .orElseThrow(() -> new NotFoundException("Shift with ID " + shiftId + " not found in team with ID " + teamId));
    }

    @Override
    public void saveShift(Shift shift) {
        shiftRepository.save(shift);
    }

    @Override
    public void deleteShiftById(UUID shiftId) {
        shiftRepository.deleteById(shiftId);
    }
}
