package com.cdhorn.Interfaces;

import com.cdhorn.Models.Map;
import com.cdhorn.Models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MapRepository extends CrudRepository<Map, Long> {
    Iterable<Map> findAllBySharedIsTrue();
    Iterable<Map> findAllByUser(User user);
}
