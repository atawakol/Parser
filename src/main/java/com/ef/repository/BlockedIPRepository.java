package com.ef.repository;


import org.springframework.data.repository.CrudRepository;

import com.ef.domain.BlockedIP;

public interface BlockedIPRepository extends CrudRepository<BlockedIP, Long>{

}
