package dev.oleksii.rotamanagementapp.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Embeddable entity for storing verification token details.
 * Used to verify a user's email during registration.
 */
@Embeddable
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VerificationToken {
    private String token;
    private LocalDateTime expirationDate;
}

