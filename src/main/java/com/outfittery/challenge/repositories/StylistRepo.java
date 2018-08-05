package com.outfittery.challenge.repositories;

import com.outfittery.challenge.models.Stylist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StylistRepo extends JpaRepository<Stylist, Long> {
}
