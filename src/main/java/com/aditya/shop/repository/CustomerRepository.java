package com.aditya.shop.repository;

import com.aditya.shop.entity.Customer;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository // ini optional, tapi kita kasih aja biar konsistentory
@Transactional //karna defaultnya auto commit, jadi kita set transaction harus start commit manual, but this task was done by springBoot
public interface CustomerRepository extends JpaRepository<Customer, String>, JpaSpecificationExecutor<Customer> {

    @Modifying //tambahkan notasi ini tiap merubah data
    @Query(value = "UPDATE m_customer SET status = :status WHERE id = :id", nativeQuery = true)
    void updateStatus(@Param("id") String id, @Param("status") Boolean status);
}
