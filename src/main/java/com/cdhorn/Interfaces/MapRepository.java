package com.cdhorn.Interfaces;

import com.cdhorn.Models.Map;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MapRepository extends CrudRepository<Map, Long> {
    List<Map> findAllBySharedIsTrue();
}
