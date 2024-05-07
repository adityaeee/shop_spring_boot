package com.aditya.shop.service.impl;

import com.aditya.shop.entity.Customer;
import com.aditya.shop.repository.CustomerRepository;
import com.aditya.shop.service.CustomerService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final EntityManager entityManager;

    @Override
    public Customer create(Customer customer) {
        return customerRepository.saveAndFlush(customer);
    }

    @Override
    public Customer getById(String id) {
        return findByIdOrThrowNotFound(id);
    }



    @Override
    public List<Customer> getAll() {
        String name = "aditya";
        String mobilePhone = "08123456789";

//        ambil kriteria builder
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

//        ini baru melakkan query "Select" doang
        CriteriaQuery<Customer> query = criteriaBuilder.createQuery(Customer.class);

//        "FROM m_customer"
        Root<Customer> root = query.from(Customer.class);

//        "SELECT * FROM m_customer"
        query.select(root);

        List<Predicate> predicates = new ArrayList<>();
        if (name != null) {
            Predicate namePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
            predicates.add(namePredicate);
        }

        if (mobilePhone != null) {
            Predicate mobilePhoneNoPredicate = criteriaBuilder.equal(root.get("mobilePhoneNo"), mobilePhone);
            predicates.add(mobilePhoneNoPredicate);
        }

        query.where(criteriaBuilder.or(predicates.toArray(new Predicate[]{})));

//        sama halnya dengan ini
//        entityManager.createQuery("SELECT c FROM m_customer c");

        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public Customer update(Customer customer) {
        findByIdOrThrowNotFound(customer.getId());
        return customerRepository.saveAndFlush(customer);
    }

    @Override
    public void delete(String id) {
        Customer customer = findByIdOrThrowNotFound(id);
        customerRepository.delete(customer);
    }

    public Customer findByIdOrThrowNotFound(String id) {
        // artinya, kita findById, kalau enggak ada dilempar atau di Throw,
        // jadi kan sebenernya di bungkus sama optional dan kita pakai.get maka datanya menjadi Customer kayak kemaren. tapi dengan .orElseThrow kita juga gunakan .get ketika ada datanya dan otomatis di lempat throw ketika data null
        return customerRepository.findById(id).orElseThrow(() -> new RuntimeException("customer not found"));
    }


}