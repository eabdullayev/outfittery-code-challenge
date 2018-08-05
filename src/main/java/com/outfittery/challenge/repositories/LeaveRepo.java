package com.outfittery.challenge.repositories;

import com.outfittery.challenge.models.Leave;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaveRepo extends JpaRepository<Leave, Long> {
}
