package com.cdhorn.Interfaces;


import com.cdhorn.Models.Like;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends CrudRepository<Like, Long>{
}
