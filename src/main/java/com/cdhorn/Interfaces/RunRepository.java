package com.cdhorn.Interfaces;


import com.cdhorn.Models.Run;
import com.cdhorn.Models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RunRepository extends CrudRepository<Run, Long> {
    List<Run> findAllByUser(User user);
}
