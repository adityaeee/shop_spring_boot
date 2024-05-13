package com.aditya.shop.specification;

import com.aditya.shop.dto.request.SearchCustomerRequest;
import com.aditya.shop.entity.Customer;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CustomerSpecification {
    public static Specification<Customer> getSpecification(SearchCustomerRequest request) {

//        karna ngereturn new specification, bisa gunakan lamda
//        return new Specification<Customer>() {
//            @Override
//            public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
//                return null;
//            }
//        }

        return ((root, query, criteriaBuilder) -> {
//            String name = "Aditya";
//            String phone = "08123456789";

            List<Predicate> predicates = new ArrayList<>();
            if (request.getName() != null) {
                Predicate namePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + request.getName().toLowerCase() + "%");
                predicates.add(namePredicate);
            }

            if (request.getPhone() != null) {
                Predicate mobilePhoneNoPredicate = criteriaBuilder.equal(root.get("mobilePhoneNo"), request.getPhone()  );
                predicates.add(mobilePhoneNoPredicate);
            }

            if (request.getBirthDate()   != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date parseDate = new Date();
                try {
                    parseDate = sdf.parse(request.getBirthDate());
                }catch (ParseException e) {
                    throw new RuntimeException(e);
                }
//
                Predicate birthDatePredicate = criteriaBuilder.equal(root.get("birthDate"), parseDate);
                predicates.add(birthDatePredicate);
            }

            if (request.getStatus() != null) {
                Predicate statusPredicate = criteriaBuilder.equal(root.get("status"), request.getStatus());
                predicates.add(statusPredicate);
            }

            return query.where(criteriaBuilder.or(predicates.toArray(new Predicate[]{}))).getRestriction();
        });
    }
}
