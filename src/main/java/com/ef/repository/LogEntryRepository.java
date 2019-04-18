package com.ef.repository;

import org.springframework.data.repository.CrudRepository;

import com.ef.domain.LogEntry;

public interface LogEntryRepository extends CrudRepository<LogEntry, Long> {

}
