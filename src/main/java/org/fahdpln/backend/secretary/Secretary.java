package org.fahdpln.backend.secretary;

import org.fahdpln.backend.user.User;
import org.fahdpln.backend.utils.BasicEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Entity
@Table(name = "secretaries")
@AllArgsConstructor
@NoArgsConstructor
public class Secretary extends BasicEntity {

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;
    
}
