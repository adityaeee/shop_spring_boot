package com.aditya.shop.service.impl;

import com.aditya.shop.dto.request.SearchCustomerRequest;
import com.aditya.shop.entity.Customer;
import com.aditya.shop.entity.UserAccount;
import com.aditya.shop.repository.CustomerRepository;
import com.aditya.shop.service.CustomerService;
import com.aditya.shop.service.UserService;
import com.aditya.shop.specification.CustomerSpecification;
import com.aditya.shop.utils.ValidationUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final EntityManager entityManager;
    private final ValidationUtil validationUtil;
    private final UserService userService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Customer create(Customer customer) {
        return customerRepository.saveAndFlush(customer);
    }

    @Transactional(readOnly = true)
    @Override
    public Customer getById(String id) {
        return findByIdOrThrowNotFound(id);
    }



    public List<Customer> getAllAwal() {
        String name = "aditya";
        String phone = "08123456789";

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

        if (phone != null) {
            Predicate mobilePhoneNoPredicate = criteriaBuilder.equal(root.get("mobilePhoneNo"), phone);
            predicates.add(mobilePhoneNoPredicate);
        }

        query.where(criteriaBuilder.or(predicates.toArray(new Predicate[]{})));

//        sama halnya dengan ini
//        entityManager.createQuery("SELECT c FROM m_customer c");

        return entityManager.createQuery(query).getResultList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Customer> getAll(SearchCustomerRequest request){
        Specification<Customer> specification = CustomerSpecification.getSpecification(request);

        if(request.getName() ==null && request.getPhone()==null && request.getBirthDate()==null && request.getStatus() == null) {
            return customerRepository.findAll();
        }

        return customerRepository.findAll(specification);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Customer update(Customer customer) {
        validationUtil.validate(customer);

        Customer curentCustomer = findByIdOrThrowNotFound(customer.getId());

        UserAccount userAccount = userService.getByContent();

        //hanya bisa diupdate dari diri sendiri untuk level ROLE_CUSTOMER
        if (!userAccount.getId().equals(curentCustomer.getUserAccount().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not found");
        }

        curentCustomer.setName(customer.getName());
        curentCustomer.setMobilePhoneNo(customer.getMobilePhoneNo());
        curentCustomer.setAddress(customer.getAddress());
        curentCustomer.setBirthDate(customer.getBirthDate());
        customerRepository.saveAndFlush(curentCustomer);

        return curentCustomer;
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(String id) {
        Customer customer = findByIdOrThrowNotFound(id);
        customerRepository.delete(customer);
    }

    private Customer findByIdOrThrowNotFound(String id) {
        // artinya, kita findById, kalau enggak ada dilempar atau di Throw,
        // jadi kan sebenernya di bungkus sama optional dan kita pakai.get maka datanya menjadi Customer kayak kemaren. tapi dengan .orElseThrow kita juga gunakan .get ketika ada datanya dan otomatis di lempat throw ketika data null
        return customerRepository.findById(id).orElseThrow(() -> new RuntimeException("customer not found"));
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateStatusById(String id, Boolean status) {
        findByIdOrThrowNotFound(id);
        customerRepository.updateStatus(id, status);
    }

}