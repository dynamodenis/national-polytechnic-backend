package com.mabawa.nnpdairy.repository.mongo;

import com.mabawa.nnpdairy.models.mongo.ConsultantsProfile;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

//@Repository
public interface ConsultantsProfileRepository extends MongoRepository<ConsultantsProfile, String> {
    @Query("{ 'id' : ?0 }")
    List<ConsultantsProfile> getConsultantsProfileById(String id);

    public void deleteConsultantsProfileById(String id);
}
