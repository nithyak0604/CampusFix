package com.campusfix.repository;
import com.campusfix.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
public interface LocationRepository extends JpaRepository<Location, Long> {}
